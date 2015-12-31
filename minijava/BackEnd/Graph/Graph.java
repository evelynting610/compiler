package minijava.BackEnd.Graph;
import java.util.*;

public class Graph<E> {

    private int nodecount=0;
    private List<E> mynodes = new LinkedList<E>();
    public  List<E> nodes() { return mynodes;} 

    public int addNode(E node) {
	mynodes.add(node);
	return nodecount++;
    }

    @SuppressWarnings("unchecked")
    public void addEdge(E f, E t) {
	Node<E>from = (Node<E>) f;
	Node<E>to   = (Node<E>) t;
	from.succs.add(t);
	from.adjs.add(t);
	to.preds.add(f);
	to.adjs.add(f);
    }

    @SuppressWarnings("unchecked")
    public void show(java.io.PrintStream out) {
	for (E n : mynodes)
	    ((Node<E>)n).show(out);
    }

    public String nodeListToString(List<E> list) {
	StringBuffer b = new StringBuffer();
	for (E e : list)
	    b.append(e + " ");
	return b.toString();
    }
}