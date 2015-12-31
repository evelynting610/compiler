package minijava.BackEnd.Graph;
import java.util.*;

public class Node<E> {
    
    private int mykey;
    List<E> succs = new LinkedList<E>();
    List<E> preds = new LinkedList<E>();
    List<E> adjs  = new LinkedList<E>();

    private Node(){}

    @SuppressWarnings("unchecked")
    public Node(Graph<E> g) {
	mykey = g.addNode((E)this);
    }

    public List<E> succ() {return succs;}
    public List<E> pred() {return preds;}
    public List<E> adj()  {return adjs;}

    public int inDegree()     {return preds.size();}
    public int outDegree()    {return succs.size();}
    public int degree()       {return adjs.size();}

    public boolean goesTo(E n) {
	return succs.contains(n);
    }

    public boolean comesFrom(E n) {
	return preds.contains(n);
    }

    public boolean adj(E n) {
	return goesTo(n) || comesFrom(n);
    }

    public String toString() {return String.valueOf(mykey);}

    public void show(java.io.PrintStream out) {
	out.print(mykey + ": ");
	for(E q : succs)
	    out.print(q + " ");
	out.println();
    }
}
