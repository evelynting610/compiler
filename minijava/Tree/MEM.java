package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.TempMap;

/** 
 * A MEM is a memory access.
 */

public class MEM extends Exp {
    /**
     * An expression for the memory address
     */
    public Exp exp;

    /**
     * Class constructor
     * @param e the expression for the address
     */
    public MEM(Exp e) {exp=e;}

    public String toString() { return "MEM(" + exp.toString() + ")"; }

    public String icode(TempMap t) {
	return idString + " : MEM " + exp.idString;
    }
}

