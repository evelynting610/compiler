package minijava.BackEnd.RegAlloc;

import minijava.BackEnd.Graph.Node;

class MoveNode {

  MoveSet set;
  MoveNode next, prev;

  Node instr;
  RegNode from, to;

  MoveNode (Node i, RegNode from, RegNode to) {
    instr = i;
    this.from  = from;
    this.to    = to;
  }
}

  
