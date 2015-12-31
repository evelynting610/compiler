package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.Label;
import minijava.Temp.TempMap;

/**
 * A CJUMP represents a conditional branch statement.
 */

public class CJUMP extends Stm {
    public final static int EQ=0, NE=1, LT=2, GT=3, LE=4, GE=5,
	ULT=6, ULE=7, UGT=8, UGE=9;

    public final static String[] opStrings = {"EQ", "NE", "LT", "GT", "LE", "GE",
					      "ULT", "ULE", "UGT", "UGE"};

    public int relop;
    public Exp left, right;
    public Label iftrue, iffalse;

    /**
     * Class constructor
     * @param rel an int representing a relational operator.  The
     * possible codes are available via constant field values.
     * @param l an Exp for the left operand.
     * @param r an Exp for the right operand.
     * @param t a Label to which execution should jump if the condition is true.
     * @param f a Label to which execution should jump if the condition is false.
     */

    public CJUMP(int rel, Exp l, Exp r, Label t, Label f) {
	relop=rel; left=l; right=r; iftrue=t; iffalse=f;
    }
    
    public CJUMP(String rel, Exp l, Exp r, Label t, Label f) {
	left=l; right=r; iftrue=t; iffalse=f;
	
	for (int i=0; i<opStrings.length; ++i)
	    if (rel.equals(opStrings[i])) {
		relop = i;
		return;
	    }
	throw new RuntimeException ("Bad opcode " + rel + " in CJUMP");
    }
    
    public String toString() {
	return left.makeLine() + right.makeLine() + icode();
    }

    public String icode() {
	String op;

	if (relop < 0 || relop > 9) op = "badop";
	else op = opStrings[relop];

	return super.toString() + "CJUMP " + op + " " +
	    left.idString + " " + right.idString + " " + 
	    iftrue.toString() + " " + iffalse.toString();
    }
}

