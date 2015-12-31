package minijava.BackEnd.RegAlloc;

class MoveSet {
  MoveNode first, last;
  String name;

  MoveSet (String name) {
    this.name = name;
  }

  boolean isEmpty() {
    return first == null;
  }

  MoveNode get() {
    MoveNode result = first;
    first = result.next;
    if (first != null) first.prev = null;
    return result;
  }

  void remove(MoveNode n) {
    if ((first == n) && (last == n)) {
      first = last = null;
    }
    else if (first == n) {
      first = n.next;
      first.prev = null;
    }
    else if (last == n) {
      last = n.prev;
      last.next = null;
    }
    else {
      n.prev.next = n.next;
      n.next.prev = n.prev;
    }
  }

  void add(MoveNode n) {
    n.set = this;

    if (first == null) {
      first = last = n;
      n.next = n.prev = null;
    }
    else {
      n.next = first;
      first.prev = n;
      n.prev = null;
      first = n;
    }
  }
}
