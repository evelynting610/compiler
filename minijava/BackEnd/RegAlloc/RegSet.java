package minijava.BackEnd.RegAlloc;

import java.util.*;

class RegSet extends LinkedList<RegNode> {
  String name;
  RegAlloc alloc;

  RegSet (String name, RegAlloc alloc) {
    this.name = name;
    this.alloc = alloc;
  }

  void check() {
      for (RegNode r : this)
	  if (r.set != this) System.out.println ("ERROR in RegSet " + name);
  }

  void remove(RegNode n) {
    if (n.set != this) throw new RuntimeException ("Error in RegSet remove");
    n.set = null;
    super.remove(n);
  }

  RegNode get() {
      return removeFirst();
  }

  public boolean add(RegNode n) {
      throw new RuntimeException ("Call to add() in RegNode");
  }

  public void addFirst(RegNode n) {
      //      System.out.println ("Adding " + n.name + " to " + name);
      n.set = this;
      super.addFirst(n);
  }
}
