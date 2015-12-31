package minijava.Interpreter;

import minijava.Temp.*;
import minijava.Tree.*;
import minijava.Frame.Frame;
import minijava.Arch.Simple.PFrame;
import java.util.*;

public class Method {

    protected Label label;
    protected Stm   body;
    protected PFrame frame;
    protected Simulator sim;

    public Method (Label l, Frame f) {
	label = l;
	frame = (PFrame) f;
    }

    public Method (Label l, Stm b, Frame f) {
	this(l, f);
	body = b;
    }

    String createICode() {

	minijava.Translate.ICode ic = new minijava.Translate.ICode(body, frame);

	return "method " + label + " " 
	    + body.idString() + " "
	    + frame.getFrameInfo() + "\n" 
	    + ic
	    + "endMethod\n\n";
    }

    void initializeSimulator(Simulator s) {
	sim = s;
    }

    Value simulate(Value... args) {
	return (new Activation(this, args)).doIt(body);
    }

    Value simulate(Iterable<Value> args) {
	return (new Activation(this, args)).doIt(body);
    }

}