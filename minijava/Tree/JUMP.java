package minijava.Tree;

import minijava.Temp.Label;
import minijava.Temp.TempMap;

/**
 * A JUMP is an unconditional branch statement.
 */

public class JUMP extends Stm {
    public Label target;

    /**
     * Class constructor
     * @param target the target label for the branch
     */
    public JUMP(Label target) {
	this.target = target;
    }

    public String toString() {
	return super.toString() + "JUMP " + target.toString();
    }

    public String icode() {
	return super.toString() + "JUMP " + target.toString();
    }
}

