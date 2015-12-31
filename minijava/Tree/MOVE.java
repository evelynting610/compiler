package minijava.Tree;
import minijava.Temp.Temp;
import minijava.Temp.Label;
import minijava.Temp.TempMap;

/**
 * A MOVE is a statement that copies a value.
 */

public class MOVE extends Stm {
    public Exp dst, src;

    /**
     * Class constructor.
     * @param d an expression for the destination.  This should be either a TEMP or a MEM.
     * @param s an expression for the source value.
     */
    public MOVE(Exp d, Exp s) {dst=d; src=s;}


    public String toString() {
	return dst.makeLine() + src.makeLine() +
	    super.toString() + "MOVE " + dst.idString + " " + src.idString;
    }

    public String icode() {
	return  super.toString() + "MOVE " + dst.idString + " " + src.idString;
    }

}

