package minijava.BackEnd.RegAlloc;

abstract public class InterferenceGraph {

  int regCount;
  int initialRegCount;      // number of precolored nodes
  int ordinaryRegCount;

  RegNode[] node;           // ordinary regs come first
  MoveSet moves;

  abstract void addEdge (RegNode a, RegNode b);
}
