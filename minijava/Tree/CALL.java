package minijava.Tree;

import minijava.Temp.Temp;
import minijava.Temp.TempMap;
import minijava.Temp.Label;
import java.util.*;

/**
   A CALL represents a function call.
*/

public class CALL extends Exp {
    public Exp func;
    public ExpList args;
    
    /**
     * Class constructor
     *  @param f an Exp yielding a pointer to the function.  The
     * pointer is often given by using a NAME node referencing the
     * function's label.
     * @param a an ExpList giving expressions for the arguments.
     */
    public CALL(Exp f, ExpList a) { 
	func = f; args = a; 
    }

    public String toString () {
	return args.makeLine() + func.makeLine() 
	    + idString + ": CALL " + func.idString + " (" + args.idString() + ") ";
    }
    
    public String icode(TempMap t) {
	StringBuffer b = new StringBuffer();
	b.append (idString + " : CALL " + func.idString + " ");
	for (Exp e2 : args)
	    b.append(e2.idString + " ");
	return b.toString();
    }

    public String makeLine() {
	return toString() + "\n";
    }

}

