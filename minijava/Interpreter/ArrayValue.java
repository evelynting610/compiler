package minijava.Interpreter;

public class ArrayValue extends Value {

    Value[] value;

    public ArrayValue (int size) {
	value = new Value[size];
    }
    
    public String toString() {
	return "array";
    }

    public String shortName() {
	return "array";
    }

    public int getLength() {
	return value.length;
    }

    public Value doBinop (Value v, int binop) {
	if (v instanceof IntValue) {
	    if (binop == 0)
		return new ArrayPosValue(this,v.getInt());
	    if (binop == 1)
		return new ArrayPosValue(this,-v.getInt());
	}
	return super.doBinop(v, binop);
    }
}
