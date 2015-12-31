package minijava.Arch.Linux64;

import java.util.*;

import minijava.Frame.*;
import minijava.Machine.Machine;
import minijava.Temp.*;

/**
 * An implementation of Machine.Machine for the 64-bit Linux architecture.
 */

public class Linux64 implements Machine {

    /**
     * Class constructor
     */

    public Linux64() {
    }

    public String getName() {
	return "linux64";
    }

    public int wordSize() {
	return 8;
    }

    protected HashMap<String,Integer> labels = new HashMap<String,Integer>();

    public Frame makeFrame(Label name) {
	return new PFrame(name);
    }

    public Frame makeFrame(Label name, String string) {
	return new PFrame(name, string);
    }

    public Label makeLabel (String s) {
	Integer count = labels.get(s);
	if (count == null) {
	    labels.put (s, 1);
	    return new Label(s + "_0");
	}
	else {
	    labels.put(s, count+1);
	    return new Label(s + "_" + count);
	}
    }
}