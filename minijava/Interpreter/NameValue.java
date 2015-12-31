package minijava.Interpreter;

import minijava.Temp.*;

public class NameValue extends Value {

    Label label;
    Simulator sim;

    public NameValue (Label l, Simulator s) {
	label = l;
	sim = s;
    }
    
    public String toString() {
	return label.toString();
    }

    public String getString() {
	String s = sim.stringMap.get(label);
	if (s == null)
	    throw new ICodeException(label + " isn't the label for a string");
	s = s.substring(1, s.length()-1);
	s = s.replace("\\\\", "\\");
	s = s.replace("\\n", "\n");
	s = s.replace("\\t", "\t");
	return s;
    }

    public Value loadFromMemory() {
	Value v = sim.globalMap.get(label);
	if (v == null)
	    throw new ICodeException("Trying to load from memory: " + label + " isn't the label for a global variable");
	return v;
    }

    public void storeInMemory(Value v) {
	if (!sim.globalMap.containsKey(label))
	    throw new ICodeException("Trying to store in memory: " + label + " isn't the label for a global variable");
	sim.globalMap.put(label, v);
    }

    public String shortName() {
	return "label";
    }

    public Label getFunctionLabel() {
	return label;
    }
}
