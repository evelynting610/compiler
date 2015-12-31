package minijava.Tree;

import minijava.Temp.Temp;
import minijava.Temp.TempMap;

/**
 * A TEMP represents a register.
 */
public class TEMP extends Exp {

    /**
     * The Temp
     */
    public Temp temp;

    /**
     * Class constructor
     * @param t the Temp
     */

    public TEMP(Temp t) {temp=t;}
    
    public String toString() { return "TEMP(" + temp.toString() + ")"; }

    public String icode(TempMap map) {
	String s = map.tempMap(temp);
	if (s == null) s = temp.toString();
 	return idString + " : TEMP " + s;
    }
}

