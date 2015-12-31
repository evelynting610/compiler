package minijava.Temp;
import java.util.*;

/** An unordered set of Temps
 */

public class TempSet implements Iterable<Temp> {


    private LinkedList<Temp> set = new LinkedList<Temp>();

    /**
     * Class constructor
     * @param args a sequence of zero or more Temp variables
     */

    public TempSet(Temp... args) {
	for (Temp t : args)
	    set.add(t);
    }

    /**
     * Class constructor.  Creates a TempSet that is the union of two other sets.  
     * The intersection of the two sets should be empty.
     * @param a the first set
     * @param b the second set
     */
    public TempSet (Iterable<Temp> a, Iterable<Temp> b) {
	for (Temp t : a)
	    set.add(t);
	for (Temp t : b)
	    set.add(t);
    }

    /**
     * Returns an iterator for the set.
     * @return Iterator<Temp>
     */

    public Iterator<Temp> iterator() {
	return set.iterator();
    }

    /** Returns a string that lists the Temps in the set.
     * @return String
     */
    public String toString() {
	StringBuffer b = new StringBuffer();
	for (Temp t : set)
	    b.append(t + " ");
	return b.toString();
    }
}

