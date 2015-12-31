package minijava.BackEnd.RegAlloc;
import java.util.*;

class RegNodeList implements Iterable<RegNode> {

    // adds are always to the front, so the iterator gives most recent adds first.

    private LinkedList<RegNode> list = new LinkedList<RegNode>();
    private String name;

    public RegNodeList (String name) {
	this.name = name;
    }

    public RegNodeList () {}
    public Iterator<RegNode> iterator() {
	return list.iterator();
    }

    public void add(RegNode r) {
	//	if (name != null)
	//	    System.out.println ("Adding " + r.name + " to " + name);
	list.addFirst(r);
    }
}
