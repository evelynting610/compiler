package minijava.BackEnd.RegAlloc;

import minijava.BackEnd.Graph.*;
import minijava.BackEnd.FlowGraph.*;
import minijava.Temp.*;
import java.util.BitSet;
import java.util.LinkedList;

public class Liveness extends InterferenceGraph {

    private java.util.HashMap<Temp,RegNode>  tnodeMap
	= new java.util.HashMap<Temp,RegNode> ();
    private java.util.HashMap<Node,NodeInfo> nodeInfo = new java.util.HashMap<Node,NodeInfo>();
    private RegNodeList nodeSet = new RegNodeList("nodeSet");
    private NodeList<AssemNode> workList = new NodeList<AssemNode>();
    private BitSet zeroes, ones;
    private AssemFlowGraph flow;
    private TempMap preMap;

  public Liveness (AssemFlowGraph flow, TempSet precolored,
		   TempMap preMap) {

    this.flow = flow;
    this.preMap = preMap;

               // We make a linked list of all the temps
               // Precolored nodes are at the end of the list.

    createNodes (precolored);
    initialRegCount = regCount;

    //    System.out.println ("Done, we think, with precolored nodes");
    for (AssemNode n : flow.nodes()) {
	try {
	    createNodes(flow.use(n));
	    createNodes(flow.def(n));
	}
	catch (NullPointerException e) {
	    System.err.println ("Exception while processing: " + n.instr.assem);
	    throw e;
	}
    }

               // we put them in an array... precolored nodes come last

    node = new RegNode[regCount];
    int count = 0;
    ordinaryRegCount = regCount - initialRegCount;

    for (RegNode r : nodeSet) {
      r.index = count;
      node[count] = r;
      node[count].adjBitSet = new BitSet(regCount);
      count++;
    }

             // now we process the nodes of the flow graph, creating 
             // nodeInfo and enqueueing those that are sinks
    
    zeroes = makeBits(null);
    ones = flipBits(zeroes);

    for (AssemNode n : flow.nodes()) {
      NodeInfo info = new NodeInfo(n);
      nodeInfo.put(n, info);
      info.use = makeBits(flow.use(n));
      info.def = makeBits(flow.def(n));
      info.defsFlipped = flipBits(info.def);
      info.out = makeBits(null);
      info.in = makeBits(null);
      if (n.outDegree() == 0) enqueueNode(info);
    }
    
    {
      NodeInfo info;
      while ((info = nextInQueue()) != null) {
	BitSet live = (BitSet) info.out.clone();
	live.and(info.defsFlipped);
	live.or(info.use);
	if (!live.equals(info.in)) {
	  info.in = live;

	  for (AssemNode a : info.node.pred()) {
	    NodeInfo li = nodeInfo.get(a);
	    BitSet newOut = (BitSet)li.out.clone();
	    newOut.or(info.in);
	    if (!newOut.equals(li.out)) {
	      li.out = newOut;
	      enqueueNode(li);
	    }

	  }
	}
      }	
    }
    // printOuts();
    build();
  }

  private void build() { // assumes one statement in block

    moves = new MoveSet ("activeMoves");

    for (AssemNode n : flow.nodes()) {
      NodeInfo info = nodeInfo.get(n);
      BitSet live = info.out;
      
      if (flow.isMove(n)) {
	  MoveNode m = new MoveNode(n, tnode(flow.use(info.node).getFirst()),
				    tnode(flow.def(info.node).getFirst()));
	  moves.add(m);

	  live.and(flipBits(info.use));
	  moveListAdd (info.def, m);
	  moveListAdd (info.use, m);
      }

      live.or(info.def);
      addEdges(info.def, live);
    }
  }

  private void addEdges (BitSet b, BitSet l) {
    for (int i=0; i < regCount; ++i)
      if (b.get(i))
	for (int j=0; j < regCount; ++j)
	  if (l.get(j))
	    addEdge (i,j);
  }

  void addEdge (RegNode a, RegNode b) {
    addEdge (a.index, b.index);
  }

  void addEdge (int i, int j) {
    if ((i == j) || node[i].isEdge(j, this)) return;

    node[i].addEdge(j, this);
    node[j].addEdge(i, this);
  }

  private void moveListAdd (BitSet b, MoveNode move) {

    for (int i=0; i < regCount; ++i)
	if (b.get(i) && !(node[i].moveSet.contains(move)))
	    node[i].moveSet.add(move);
  }

    LinkedList<Temp> getOut(AssemNode n) {
	LinkedList<Temp> list = new LinkedList<Temp>();
	NodeInfo info = nodeInfo.get(n);

	for (int i=0; i < regCount; ++i)
	    if (info.out.get(i)) list.addLast(node[i].temp);
	return list;
    }

  private void printOuts() 
    {
      
      java.io.PrintStream out = System.out;

      for (AssemNode n : flow.nodes()) {
	NodeInfo info = nodeInfo.get(n);
	for (int i=0; i < regCount; ++i)
	  if (info.out.get(i)) out.print (node[i].temp.toString() + " ");
	out.println();
      }
    }


  private void enqueueNode(NodeInfo info) {
    if (info.waiting) return;
    info.waiting = true;
    workList.addFirst(info.node);
  }

  private NodeInfo nextInQueue() {
      if (workList.isEmpty()) return null;
      else {
	  NodeInfo info = nodeInfo.get(workList.removeFirst());
	  info.waiting= false;
	  return info;
      }
  }

  private BitSet makeBits(TempList t) {
      BitSet bits = new BitSet(regCount);
      if (t != null)
	  for (Temp temp : t)
	      bits.set(tnode(temp).index);
      return bits;
  }

  private BitSet flipBits(BitSet s) {
    BitSet t = new BitSet(regCount);

    for (int i=0; i<regCount; ++i)
      if (!s.get(i)) t.set(i);
    return t;
  }

  private void createNodes (Iterable<Temp> t) {
    
      for (Temp temp : t) {
	  if (tnode(temp) == null) {
	      RegNode n = new RegNode(temp,regCount,preMap.tempMap(temp));
	      nodeSet.add(n);
	      tnodeMap.put (temp, n);
	      regCount++;
	  }
      }
  }

  public RegNode tnode (Temp temp) {
    return tnodeMap.get(temp);
  }

}
