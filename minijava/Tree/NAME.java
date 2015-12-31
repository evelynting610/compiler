package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.Label;
import minijava.Temp.TempMap;

/** A NAME is an expression associated with a labelled address in the
 * assembly code.  The value of the expression is effectively a
 * pointer.  To access the value of a global variable, use "new MEM(new
 * NAME(...))".  To refer to a function, use "new NAME(...)" without
 * using MEM.
 */
public class NAME extends Exp {

    /**
     * The label
     */
    public Label label;

    /** Class constructor
     * @param l the label
     */
    public NAME(Label l) {label=l;}
    
    public String toString() {
	return "NAME(" + label.toString() + ")";
    }

    public String icode(TempMap t) {
	return idString + " : NAME " + label;
    }
}

