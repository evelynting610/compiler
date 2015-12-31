package minijava.Temp;
import java.util.*;

/** An ordered list of Temps
 */

public class TempList implements Iterable<Temp> {


    private LinkedList<Temp> list = new LinkedList<Temp>();

    /**
     * Class constructor
     * @param args a sequence of zero or more Temp variables
     */

    public TempList(Temp... args) {
	for (Temp t : args)
	    list.addLast(t);
    }

    /**
     * Class constructor.  Creates a TempList that is the concatenation of two other lists.
     * @param a the first list
     * @param b the second list
     */
    public TempList (TempList a, TempList b) {
	for (Temp t : a)
	    list.add(t);
	for (Temp t : b)
	    list.add(t);
    }

    /**
     * Returns an iterator for the list.
     * @return Iterator<Temp>
     */

    public Iterator<Temp> iterator() {
	return list.iterator();
    }

    /**
     * Returns the Temp at a given index.
     * @param i the index
     * @return the value at that index
     */
    public Temp get(int i) {
	return list.get(i);
    }

    /**
     * Returns the first Temp in the list.
     * @return the first Temp
     */
    public Temp getFirst() {
	return list.getFirst();
    }

    /**
     * Determines if the list contains a given Temp.
     * @param t a Temp
     * @return true, iff the element is in the list
     */

    public boolean contains (Temp t) {
	return list.contains(t);
    }

    /**
     * Replaces a given Temp in the list by another one.
     * @param oldt a Temp that is the list
     * @param newt a replacement Temp
     * @throws RuntimeException if the Temp is not in the list.
     */

    public void replace(Temp oldt, Temp newt) {
	ListIterator<Temp> it = list.listIterator();
	while (it.hasNext()) {
	    if (it.next() == oldt) {
		it.set(newt);
		return;
	    }
	}
	throw new RuntimeException ("Replace failed in TempList");
    }

    /** Returns a string that lists the Temps in the set.
     * @return String
     */
    public String toString() {
	StringBuffer b = new StringBuffer();
	for (Temp t : list)
	    b.append(t + " ");
	return b.toString();
    }
}

