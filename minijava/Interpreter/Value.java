package minijava.Interpreter;

import minijava.Temp.*;

public abstract class Value {

    static String[] opStrings = {"PLUS", "MINUS", "MUL", "DIV", "AND", "OR",
				 "LSHIFT", "RSHIFT", "ARSHIFT", "XOR", "MOD"};

    static String[] relStrings = {"EQ", "NE", "LT", "GT", "LE", "GE",
				  "ULT", "ULE", "UGT", "UGE"};

    public Value doBinop(Value v, int binop) {
	throw new ICodeException("Can't do " + opStrings[binop] + " on " +
				   this.shortName() + " and " + v.shortName());
    }

    public boolean compare(Value v, int relop) {
	throw new ICodeException("Can't do " + relStrings[relop] + " on " +
				   this.shortName() + " and " + v.shortName());
    }

    public Label getFunctionLabel() {
	throw new ICodeException("Can't make a call based on a function named via " + shortName());
    }

    public int getInt() {
	throw new ICodeException("Can't get an int from a " + shortName());
    }

    public String getString() {
	throw new ICodeException("Can't get a String from a " + shortName());
    }

    public Value loadFromMemory() {
	throw new ICodeException("Can't load from memory at an address named by a " + shortName());
    }

    public void storeInMemory(Value v) {
	throw new ICodeException("Can't store in memory at an address named by a " + shortName());
    }

    public abstract String shortName();
}

