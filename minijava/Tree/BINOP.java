package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.TempMap;
import minijava.Temp.Label;

/**
 * A BINOP represents a binary operation.
 */

public class BINOP extends Exp {

    public final static int PLUS=0, MINUS=1, MUL=2, DIV=3, 
	AND=4,OR=5,LSHIFT=6,RSHIFT=7,ARSHIFT=8,XOR=9, MOD=10;

    public final static String[] opStrings = {"PLUS", "MINUS", "MUL", "DIV", "AND", "OR",
				       "LSHIFT", "RSHIFT", "ARSHIFT", "XOR", "MOD"};

    public int binop;
    public Exp left, right;

    /**
     * Class constructor
     * @param b an int representing the operation.  The possible codes are
     * available via constant field values.
     * @param l an Exp for the left operand.
     * @param r an Exp for the right operand.
     */
    public BINOP(int b, Exp l, Exp r) {
	binop=b; left=l; right=r; 
    }

    public BINOP(String s, Exp l, Exp r) {

	left = l; right = r;

	for (int i = 0; i<opStrings.length; ++i) {
	    if (s.equals(opStrings[i])) {
		binop = i;
		return;
	    }
	}
	throw new RuntimeException ("Bad opcode " + s + " in BINOP");
    }
	
    public String toString () {
	String op;
	
	if (binop < 0 || binop > MOD) op = "badop";
	else op = opStrings[binop];
	
	return "BINOP " + op + "(" + left.toString() + "," + right.toString() + ")";
    }

    public String icode(TempMap t) {
	String op;
	
	if (binop < 0 || binop > MOD) op = "badop";
	else op = opStrings[binop];
	
	return idString + " : BINOP " + op + " " + left.idString + " " + right.idString;
    }
}


