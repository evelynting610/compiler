package minijava.Arch.Darwin64;

import java.util.*;

import minijava.Frame.*;
import minijava.Machine.Machine;
import minijava.Temp.*;
import minijava.Arch.Linux64.*;


/**
 * An implementation of Machine.Machine for the 64-bit Darwin architecture.
 */

public class Darwin64 extends Linux64 {

    /**
     * Class constructor
     */

    public Darwin64() {
    }

    public String getName() {
	return "Darwin64";
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