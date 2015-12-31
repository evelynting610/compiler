package minijava.BackEnd.RegAlloc;

import java.util.*;
import minijava.BackEnd.Assem.*;
import minijava.BackEnd.FlowGraph.*;
import minijava.BackEnd.Machine.Frame;
import minijava.Temp.*;

public class RegAlloc implements TempMap {

  Frame frame;
  public InstrList instrs;
  public TempMap tMap;
  AssemFlowGraph flow;
  Liveness ig;
  int K;          // number of colors
  boolean ok;     // true if coloring is complete

  RegSet simplifyWorkList;
  RegSet freezeWorkList  ;
  RegSet spillWorkList   ;
  RegSet selectStack     ;
  RegSet coalescedNodes  ;
  RegSet precolored      ;
  RegSet coloredNodes    ;
  RegSet spilledNodes    ;

  MoveSet coalescedMoves  = new MoveSet("coalescedMoves");
  MoveSet constrainedMoves= new MoveSet("constrainedMoves");
  MoveSet frozenMoves     = new MoveSet("frozenMoves");
  MoveSet activeMoves     = new MoveSet("activeMoves");
  MoveSet workListMoves;

  public String tempMap(Temp temp) {
      System.out.println ("Called tempMap for " + temp);
      System.out.println ("  color is " + ig.tnode(temp).color.name);
    return ig.tnode(temp).color.name;
  }

    public boolean isOK() {
	return ok;
    }

  public RegAlloc (Frame f, InstrList il, TempMap origTempMap) {

      instrs = il;
      frame = f;

      flow = new AssemFlowGraph (instrs);
      ig = new Liveness (flow, f.registers(), origTempMap);
      
      simplifyWorkList = new RegSet("simplifyWorkList",this);
      freezeWorkList   = new RegSet("freezeWorkList",this);
      spillWorkList    = new RegSet("spillWorkList",this);
      selectStack      = new RegSet("selectStack",this);
      coalescedNodes   = new RegSet("coalescedNodes",this);
      precolored       = new RegSet("precolored",this);
      coloredNodes     = new RegSet("coloredNodes", this);
      spilledNodes     = new RegSet("spilledNodes", this);
      
      workListMoves    = ig.moves;
      makeWorkList();
      
      //      display();
      
      while (true) {
	  check();
	  if (!simplifyWorkList.isEmpty()) simplify();
	  else if (!workListMoves.isEmpty()) coalesce();
	  else if (!freezeWorkList.isEmpty()) freeze();
	  else if (!spillWorkList.isEmpty()) selectSpill();
	  else break;
      }
      check();
      //      display();
      
      assignColors();
      
      if (spilledNodes.isEmpty()) {
	  fixInstrs();
	  ok = true;
      }
      else {
	  rewriteProgram();
	  ok = false;
      }
  }

  void fixInstrs() {      // remove coalesced moves
                          // assumes at least one instruction will be left

      ListIterator<Instr> it = instrs.listIterator();

      while (it.hasNext()) {
	  Instr i = it.next();
	  if (removable(i)) {
	      it.remove();
	  }
      }
  }

  boolean removable(Instr i) {
      if (i instanceof MOVEInstr) {
	  MOVEInstr m = (MOVEInstr)i;
	  return ig.tnode(m.getDst()).color == ig.tnode(m.getSrc()).color;
      }
      else
	  return false;
  }

  void assignColors() {

    int time = 0;
    for (RegNode l : precolored) {
      l.color = l;
      l.timeStamp = 0;
    }

      assignLoop: 
    while (!selectStack.isEmpty()) {

      RegNode l = selectStack.get();
      time++;

      for (RegNode m : l.adjList) {
	  RegNode r = m.getAlias();
	  if ((r.set == coloredNodes) || (r.set == precolored))
	      r.color.timeStamp = time;
      }

      for (RegNode m : precolored) {
	  if (m.timeStamp < time) {
	      l.color = m;
	      coloredNodes.addFirst(l);
	      continue assignLoop;
	  }
      }
      spilledNodes.addFirst(l);
    }

    //    display();

    for (RegNode l : coalescedNodes) {
	l.color = l.getAlias().color;
	System.out.println (l.temp + " is coalesced... color is " 
				    + ((l.color==null) ? "null" : l.color.name));
    }
  }
    
  void check() {
    for (int i=0; i < ig.regCount; ++i)
      ig.node[i].check();
    simplifyWorkList.check();
    freezeWorkList  .check();
    spillWorkList   .check();
    selectStack     .check();
    coalescedNodes  .check();
    precolored      .check();

  }
    
  void display() {

      for (int i=0; i < ig.regCount; ++i)
	  ig.node[i].display();
  }

  void coalesce() {

    RegNode u,v;

    MoveNode m = workListMoves.get();
    RegNode x = m.from.getAlias();
    RegNode y = m.to  .getAlias();

    //    System.out.println ("Trying coalesce for " + x.name + " and " + y.name);

    if (y.set == precolored) {
      u = y;
      v = x;
    }
    else {
      u = x;
      v = y;
    }

    // if either is precolored, then u is.

    if (u == v) {
      coalescedMoves.add(m);
      addWorkList(u);  // added 5/04
    }
    else if ((v.set==precolored) || adj(v,u)) {
      constrainedMoves.add(m);
      addWorkList(u);
      addWorkList(v);
    }
    else if (((u.set == precolored) && adjTest(u,v)) ||          // 5/04
	     ((u.set != precolored) && conserveTest(u,v))) {     // 5/04
      coalescedMoves.add(m);
      //      System.out.println ("Coalescing " + u.name + " and " + v.name );
      combine(u,v);
      addWorkList(u);
    }
    else
      activeMoves.add(m);
  }

  boolean adjTest (RegNode u, RegNode v) {
      for (RegNode r : v.adjList) {
	  if (alive(r) && (!ok(r,u))) return false;
      }
      return true;
  }

  boolean conserveTest (RegNode u, RegNode v) {
      int k = 0;

      for (RegNode r : u.adjList)
	  if (alive(r))
	      if ((r.set == precolored) || (r.degree >= K)) k++;
      
      for (RegNode r : v.adjList)
	  if (alive(r) && (!adj(u,r)))
	      if ((r.set == precolored) || (r.degree >= K)) k++;
      
      return k < K;
  }

  boolean adj (RegNode u, RegNode v) {
    if (u.set != precolored) return u.adjBitSet.get(v.index);
    else if (v.set != precolored) return v.adjBitSet.get(u.index);
    else return true;
  }

  boolean ok (RegNode t, RegNode u) {
    return ((t.degree < K) || (t.set == precolored) || adj(t,u));
  }

  void combine (RegNode u, RegNode v) {
      //      System.out.println ("Combine of " + u.name + " and " + v.name);
    if (v.set == freezeWorkList)
      freezeWorkList.remove(v);
    else
      spillWorkList.remove(v);
    coalescedNodes.addFirst(v);
    v.alias = u;

    for (MoveNode m : v.moveSet)
      if (m.from.getAlias() != m.to.getAlias())
	  u.moveSet.add(m);

    enableMoves(v);

    for (RegNode r : v.adjList) {
      if (alive(r)) {
	ig.addEdge(r, u);
	if ((r.degree >= K) && (r.set == freezeWorkList)) {
	    freezeWorkList.remove(r);
	    spillWorkList.addFirst(r);
	}
	decrementDegree(r);
      }
    }

    if ((u.degree >= K) && (u.set == freezeWorkList)) {
      freezeWorkList.remove(u);
      spillWorkList.addFirst(u);
    }
  }

  void addWorkList(RegNode u) {
      if ((u.set != precolored) && !moveRelated(u) && (u.degree < K)) {  // 5/04
	  freezeWorkList.remove(u);
	  simplifyWorkList.addFirst(u);
      }
  }

  void simplify() {

    RegNode n = simplifyWorkList.get();
    selectStack.addFirst(n);

    for (RegNode m : n.adjList) {
	if (alive(m)) 
	    decrementDegree(m);
    }
  }

  boolean alive (RegNode m) {
    if (m.set == selectStack) return false;
    if (m.set == coalescedNodes) return false;
    return true;
  }

  void decrementDegree (RegNode m) {

      //      System.out.println ("Doing decrementDegree on " + m.name + " , degree was " + m.degree);
      //      System.out.println ("  in " + m.set.name);
      //if (m.degree == K && m.set != spillWorkList) display();

    if (m.degree-- == K) {
      enableMoves (m);

      for (RegNode r : m.adjList) {
	if (alive(r)) enableMoves(r);
      }
      spillWorkList.remove(m);
      if (moveRelated(m))
	freezeWorkList.addFirst(m);
      else
	simplifyWorkList.addFirst(m);
    }
  }

  void enableMoves (RegNode n) {
      for (MoveNode m : n.moveSet) {
	  if (m.set == activeMoves) {
	      activeMoves.remove(m);
	      workListMoves.add(m);
	  }
      }
  }    
  
  boolean moveRelated (RegNode n) {

      for (MoveNode m :  n.moveSet) {
	  if (m.set == activeMoves) return true;
	  if (m.set == workListMoves) return true;
      }
      return false;
  }

  void makeWorkList() {
    
    K = ig.initialRegCount;

    for (int i = 0; i < ig.ordinaryRegCount; ++i) {
      RegNode n = ig.node[i];

      if (n.degree >= K)
	spillWorkList.addFirst(n);
      else if (moveRelated(n))
	freezeWorkList.addFirst(n);
      else 
	simplifyWorkList.addFirst(n);
    }
    for (int i=ig.ordinaryRegCount; i < ig.regCount; ++i) {
      RegNode n = ig.node[i];
      precolored.addFirst(n);
    }
  }

    void freeze() {
	RegNode u = freezeWorkList.get();
	simplifyWorkList.addFirst(u);
	freezeMoves(u);
    }

    void freezeMoves(RegNode u) {
	
	for (MoveNode m : u.moveSet) {
	    if (m.set != activeMoves && m.set != workListMoves) continue;

	    RegNode x = m.from.getAlias();
	    RegNode y = m.to.getAlias();
	    RegNode v;
	    if (y == u.getAlias())
		v = x;
	    else
		v = y;

	    //	    System.out.println ("Freezing move between " + x.name + " and " + y.name);

	    activeMoves.remove(m);
	    frozenMoves.add(m);

	    if (v.set == freezeWorkList && !moveRelated(v)) {
		freezeWorkList.remove(v);
		simplifyWorkList.addFirst(v);
	    }
	}
    }

    void selectSpill() {
	for (RegNode l : spillWorkList) {
	    Temp t = l.temp;
	    if (t instanceof SpillTemp) continue;
	    spillWorkList.remove(l);
	    //	    System.out.println ("Marking " + l.name + "as potential spill");
	    simplifyWorkList.addFirst(l);
	    freezeMoves(l);
	    break;
	}
    }

    private class SpillTemp extends Temp {}

    void rewriteProgram() {
	for (RegNode l : spilledNodes) {
	    int spillOffset = frame.allocSpill();
	    
	    //	    System.out.println (l.name +" : spilled to offset " + spillOffset);
	    
	    Temp t = l.temp;

	    ListIterator<Instr> it = instrs.listIterator();
	    Instr h = it.next();

	    while (true) {
		TempList d = h.def();

		if (d.contains(t)) {
		    if (h instanceof MOVEInstr) {
			it.set(frame.storeSpill(((MOVEInstr)h).getSrc(), spillOffset));
		    }
		    else {
			Temp newt = new SpillTemp();
			d.replace(t,newt);
			it.add(frame.storeSpill(newt, spillOffset));
		    }
		}

		if (!(it.hasNext())) break;

		h = it.next();
		TempList u = h.use();
		if (u.contains(t)) {
		    if (h instanceof MOVEInstr) {	
			it.set(h = frame.restoreSpill(((MOVEInstr)h).getDst(), spillOffset));
		    }
		    else {
			Temp newt = new SpillTemp();
			u.replace(t,newt);
			it.set(frame.restoreSpill(newt, spillOffset));
			it.add(h);
		    }
		}
	    }
	}
    }
}
