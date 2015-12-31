package minijava.BackEnd.Graph;
import java.util.LinkedList;

public class NodeList<E> extends LinkedList<Node<E>> {

    public String toString() {
	StringBuffer b = new StringBuffer();

	for (Node<E> n : this)
	    b.append (n + " ");
	return b.toString();
    }
}



