package minijava.BackEnd.RegAlloc;

import java.util.*;

class MoveNodeSet implements Iterable<MoveNode> {

    private LinkedList<MoveNode> list = new LinkedList<MoveNode>();

    public void add (MoveNode m) {
	list.add(m);
    }

    public boolean contains (MoveNode m) {
	return list.contains(m);
    }

    public Iterator<MoveNode> iterator() { return list.iterator(); }
}
