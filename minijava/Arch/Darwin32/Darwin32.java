package minijava.Arch.Darwin32;

import java.util.*;

import minijava.Frame.*;
import minijava.Machine.Machine;
import minijava.Temp.*;
import minijava.Arch.Linux32.Linux32;

/**
 * An implementation of minijava.Machine.Machine.
 */

public class Darwin32 extends Linux32 {

    /**
     * Class constructor
     */
    public Darwin32() {
    }
    
    public String getName() {
	return "Darwin32";
    }

    public Label makeLabel (String s) {
	Integer count = labels.get(s);
	if (count == null) {
	    labels.put (s, 1);
	    return new Label("_" + s + "_0");
	}
	else {
	    labels.put(s, count+1);
	    return new Label("_" + s + "_" + count);
	}
    }
}