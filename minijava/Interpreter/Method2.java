package minijava.Interpreter;

import minijava.Temp.*;
import minijava.Tree.*;
import minijava.Frame.Frame;
import minijava.Arch.Simple.PFrame;
import java.util.*;

public class Method2 extends Method {

    protected StmList list;

    public Method2 (Label l, StmList b, Frame f) {
	super(l, f);
	list = b;
    }

    Value simulate(Value... args) {
	return (new Activation(this, args)).doIt(list);
    }

    Value simulate(Iterable<Value> args) {
	return (new Activation(this, args)).doIt(list);
    }

}