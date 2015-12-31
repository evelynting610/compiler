package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.Label;
import minijava.Temp.TempMap;

/**
 * An ESEQ is an expression that consists of a statement followed by an expression.
 */

public class ESEQ extends Exp {

    /**
     * The statement part of this ESEQ 
     */
    public Stm stm;

    /**
     * The expression part of this ESEQ
     */
    public Exp exp;

    /**
     * Class constructor
     * @param s A statement.  This parameter must never be null if an
     * ESEQ is created outside the Tree package. 
     * @param e An expression.  This parameter must never be null.
     */

    public ESEQ(Stm s, Exp e) {stm=s; exp=e;}

    public String toString() {
	return stm + "\n" + exp.makeLine() +
	    idString +  " : ESEQ " + ((stm==null) ? "null" : stm.idString) + " " + exp.idString;
    }
    String makeLine() {
	return toString() + "\n";
    }

    public String icode(TempMap t) {
	return idString +  " : ESEQ " + ((stm==null) ? "null" : stm.idString) + " " + exp.idString;
    }
}

