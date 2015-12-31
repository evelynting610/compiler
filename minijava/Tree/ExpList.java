package minijava.Tree;
import java.util.*;

/**
 * An ExpList holds a list of Exps.  This class is used to hold
 * the intermediate code for a parameter list.
 */

public class ExpList implements Iterable<Exp> {

    LinkedList<Exp> list = new LinkedList<Exp>();

    /**
     * Class constructor
     *
     * @param args an initial list of params
     */
    public ExpList(Exp... args) {
	for (Exp a : args)
	    list.add(a);
    }

    /**
     * Adds an Exp to this end of this list.
     * @param e the Exp
     */
    public void addLast(Exp e) {
	list.add(e);
    }

    /**
     * Adds an Exp to the beginning of this list.
     * @param e the Exp
     */
    public void addFirst(Exp e) {
	list.addFirst(e);
    }

    /**
     * Tests if this list is empty.
     * @return true if the list is empty
     */

    public boolean isEmpty() {
	return list.isEmpty();
    }

    /**
     * Returns length of list.
     * @return the length of the list
     */

    public int length() {
	return list.size();
    }

    /**
     * Returns an iterator for this list.
     * @return an iterator
     */
    public Iterator<Exp> iterator() {
	return list.iterator();
    }

    /**
     * Returns a list iterator for this list.
     * @return a list iterator that is initially set to the end of
     * this list
     */
    public ListIterator<Exp> listIteratorAtEnd() {
	return list.listIterator(list.size());
    }

    /**
     * Creates a string presenting this list in one or more complete lines.
     * @return the string
     */
    public String makeLine() {
	StringBuffer b = new StringBuffer();
	for (Exp e : list)
	    b.append(e.makeLine());
	return b.toString();
    }

    /**
     * Returns a string describing this list.
     *
     * @return the string
     */
    public String toString() {
	if (list.isEmpty())
	    return "";
	else {
	    StringBuffer b = new StringBuffer();
	    
	    Iterator<Exp> it = list.iterator();
	    b.append(it.next().makeLine());
	    
	    while (it.hasNext())
		b.append(it.next());
	    return b.toString();
	}
    }
    
    public String idString() {
	StringBuffer b = new StringBuffer();
	for (Exp e : list) 
	    b.append (e.idString + " ");
	return b.toString();
    }
}



