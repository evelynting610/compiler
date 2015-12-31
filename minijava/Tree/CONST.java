package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.Label;
import minijava.Temp.TempMap;

/**
 * A CONST is a constant expression.
 */

public class CONST extends Exp {
    
    /**
     * The value
     */

    public int value;

    /**
     * Class constructor
     * @param v the value
     */
    public CONST(int v) {value=v;}
    
    public String toString() { return "CONST(" + value + ")"; }

    public String icode(TempMap t) {
	return idString + " : CONST " + value;
    }

}

