package minijava.Tree;
import minijava.Temp.*;
import minijava.Temp.TempMap;
import java.util.List;

/** An ESTM is a statement consisting of an expression.  At runtime,
 * the expression is computed, but the value is discarded.  ESTM are
 * generally used in two cases: when the expression is a CALL in which the return
 * value is ignored, and when the expression is a CONST.  The latter
 * case is effectively a no-op.
 */

public class ESTM extends Stm {
    public Exp exp;

    /*
     * Class constructor
     * @param e the expression
     */

    public ESTM(Exp e) {
	exp = e;
    }

    public String toString() {
	return exp.makeLine() + super.toString() + "ESTM " + exp.idString;
    }

    public String icode() {
	return super.toString() + " ESTM " + exp.idString;
    }
}

