package minijava.Tree;
import java.util.*;

/**
 * An StmList holds a list of statements.
 */

public class StmList extends LinkedList<Stm> {

    public String toString() {
	if (size() == 0) 
	    return "";
	else {
	    StringBuffer b = new StringBuffer();
	    Iterator it = iterator();
	    b.append(it.next());
	    while (it.hasNext()) {
		b.append ("\n");
		b.append (it.next());
	    }
	    return b.toString();
	}
    }
}



