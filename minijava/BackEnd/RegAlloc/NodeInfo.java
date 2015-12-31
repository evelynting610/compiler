package minijava.BackEnd.RegAlloc;

import java.util.BitSet;
import minijava.BackEnd.FlowGraph.AssemNode;

class NodeInfo {

  BitSet use, def, in, out;
  BitSet defsFlipped;

  boolean waiting;
  AssemNode node;     // actual node from the flow graph

  NodeInfo (AssemNode n) {
    node = n;
  }
}
