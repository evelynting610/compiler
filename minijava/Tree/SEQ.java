package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.Label;
import minijava.Temp.TempMap;
import java.util.List;

/**
 * A SEQ is a statement that is the concatenation of two other statements.
 */

public class SEQ extends Stm {
    public Stm left, right;
    
    /**
     * Class constructor
     * @param l the first statement to be used in the concatenation
     * @param r the second statement to be used in the concatenation
     */
    public SEQ(Stm l, Stm r) { left=l; right=r; }

    public String toString() {
	return left.toString() + "\n" + right.toString() + "\n"
	    + super.toString() + "SEQ " + left.idString + " " + right.idString;
    }

    public String icode() {
	return super.toString() + "SEQ " + left.idString + " " + right.idString;
    }
}

