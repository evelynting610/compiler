package minijava.Tree;

import minijava.Temp.Temp;
import minijava.Temp.TempMap;

/**
   Exp is the abstract base class for tree nodes representing expressions.
*/
abstract public class Exp {

    /**
     * Number of Exp nodes that have been created
     */
    static int count = 0;

    /**
     * Identifier for this node
     */
    public String idString;
    
    /**
     * Class constructor
     */
    Exp() {
	idString = "Exp" + (++count);
    }

    /**
     * Returns a string describing this expression.
     *
     * @return the string
     */
    public abstract String toString();
    
    /**
     * Creates a string presenting this expression in one or more complete lines.
     * @return the string
     */
    String makeLine() {
	return idString + ": " + toString() + "\n";
    }

    public abstract String icode(TempMap t);
}
