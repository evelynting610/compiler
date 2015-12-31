package minijava.Tree;

import minijava.Temp.Temp;
import minijava.Temp.Label;
import minijava.Temp.TempMap;

/**
 * A LABEL represents a label in executable.  It is technically a statement, but no actions are associated with it.
 */

public class LABEL extends Stm { 
    public Label label;

    /**
     * Class constructor
     * @param l the label
     */

    public LABEL(Label l) {label=l;}

    public String toString() {
	return super.toString() + "LABEL " + label.toString();
    }

    public String icode() {
	return super.toString() + "LABEL " + label.toString();
    }
}

