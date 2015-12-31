package minijava.Arch.Simple;

import java.util.*;

import minijava.Frame.*;
import minijava.Machine.Machine;
import minijava.Temp.*;

/**
 * An implementation of Machine.Machine for a simple architecture
 */

public class Simple implements Machine {

    /**
     * Class constructor
     */

    public Simple() {
    }

    public String getName() {
	return "simple";
    }

    public int wordSize() {
	return 8;
    }

    protected HashMap<String,Integer> labels = new HashMap<String,Integer>();

    public Frame makeFrame(Label name) {
	return new PFrame(name);
    }

    public Frame makeFrame(Label name, String s) {
	return new PFrame(name, s);
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