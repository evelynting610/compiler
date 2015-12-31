package minijava.Interpreter;

public class IntValue extends Value {

    int value;

    public IntValue (int v) {
	value = v;
    }
    
    public String toString() {
	return value + "";
    }

    public String shortName() {
	return "int";
    }

    public int getInt() {
	return value;
    }

    public Value doBinop (Value v, int binop) {
	if (v instanceof ArrayValue) {
	    if (binop == 0) 
		return new ArrayPosValue((ArrayValue)v, value);
	}
	if (v instanceof IntValue) {
	    int b = ((IntValue)v).value;

	    switch (binop) {
	    case 0: return new IntValue(value+b);
	    case 1: return new IntValue(value-b);
	    case 2: return new IntValue(value*b);
	    case 3: return new IntValue(value/b);
	    case 4: return new IntValue(value&b);
	    case 5: return new IntValue(value|b);
	    case 6: return new IntValue(value<<b);
	    case 7: return new IntValue(value>>b);
	    case 8: return new IntValue(value>>>b);
	    case 9: return new IntValue(value^b);
	    case 10: return new IntValue(value%b);
	    default: throw new ICodeException("strange binop value: " + binop);
	    }
	}
	return super.doBinop(v, binop);
    }

    public boolean compare (Value v, int relop) {
	if (v instanceof IntValue) {
	    int b = ((IntValue)v).value;

	    switch (relop) {
	    case 0: return value == b;
	    case 1: return value != b;
	    case 2: return value < b;
	    case 3: return value > b;
	    case 4: return value <= b;
	    case 5: return value >= b;
	    default: throw new ICodeException("strange relop value: " + relop);
	    }
	}
	throw new ICodeException ("Can't compare an int and " + v.shortName());
    }		
}
