package minijava.BackEnd.RegAlloc;

import java.util.BitSet;
import minijava.BackEnd.Graph.Graph;
import minijava.BackEnd.Graph.NodeList;
import minijava.BackEnd.Graph.Node;
import minijava.Temp.*;

class RegNode {      // one for each temp

  RegSet set;
  RegNode next, prev;    // next and prev within set

  Temp temp;
  String name;
  RegNode color;
  int timeStamp;         // time this color was used assignColors

  int index;             
  RegNodeList adjList = new RegNodeList();     // adjacent nodes (not used if precolored)
  BitSet adjBitSet;         // bit vector     (not used if precolored)
  int degree;

  MoveNodeSet moveSet = new MoveNodeSet();
    // move instructions with which this node is associated

  RegNode alias;

  RegNode (Temp temp, int index, String name) {
    this.temp = temp;
    this.index = index;
    this.name = name;
  }

  void check() {
    if ((set == set.alloc.simplifyWorkList) ||
	(set == set.alloc.freezeWorkList) ||
	(set == set.alloc.spillWorkList)) {

      int count = 0;

      for (RegNode r : adjList)
	  if (set.alloc.alive(r))
	      count++;
      if (count != degree) System.out.println ("ERROR IN ADJLIST: " + name);

      count = 0;
      for (int i= 0; i < set.alloc.ig.regCount; ++i)
	if (adjBitSet.get(i)) 
	  if (set.alloc.alive(set.alloc.ig.node[i])) count++;
      if (count != degree) System.out.println ("ERROR IN ADJSET: " + name);
    }
  }

  void display() {

    String s = name;
    s += ' ' + set.name + ' ';

    if (set == set.alloc.coalescedNodes) 
	s += alias.name;
    else if ((set == set.alloc.simplifyWorkList) ||
	     (set == set.alloc.freezeWorkList) ||
	     (set == set.alloc.spillWorkList)) {
	s += ' ' + "Conflicts with ";
	for (RegNode r : adjList)
	    s += r.name + ' ';
	s += "Moves with ";
	
	for (MoveNode m : moveSet)
	    if (m.from == this)
		s += m.to.name + ' ';
	    else
		s += m.from.name + ' ';
    }
    System.out.println (s);
  }

  RegNode getAlias() {
    if (set == set.alloc.coalescedNodes) {
      alias = alias.getAlias();
      return alias;
    }
    return this;
  }

  boolean isEdge (int toNode, InterferenceGraph g) {
    if (index < g.ordinaryRegCount)
      return adjBitSet.get(toNode);
    else if (toNode >= g.ordinaryRegCount)
      return true;
    else return g.node[toNode].isEdge (index, g);
  }
      
  void addEdge (int toNode, InterferenceGraph g) {
      //      System.out.println ("Adding edge from " + name + " to " + g.node[toNode].name);

    if (index < g.ordinaryRegCount) {
	adjList.add(g.node[toNode]);
	degree++;
	adjBitSet.set(toNode);
    }
  }
}


