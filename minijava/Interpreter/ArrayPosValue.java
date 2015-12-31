package minijava.Interpreter;

public class ArrayPosValue extends Value {

    ArrayValue array;
    int offset;

    public ArrayPosValue (ArrayValue a, int o) {
	array = a;
	offset = o;
	if ((offset & 7) != 0)
	    throw new ICodeException("Strange byte offset " + offset + " in ArrayPosValue");
    }
    
    public Value loadFromMemory() {
	if (offset == -8)
	    return new IntValue(array.getLength());
	if (offset < 0 || offset >= array.getLength()*8)
	    throw new ICodeException("Array offset " + offset + " is out of bounds");
	return array.value[offset>>3];
    }

    public void storeInMemory(Value v) {
	if (offset < 0 || offset >= array.getLength()*8)
	    throw new ICodeException("Array offset " + offset + " is out of bounds");
	array.value[offset>>3] = v;
    }

    public String toString() {
	return "arrayelement";
    }

    public String shortName() {
	return "arrayelement";
    }
}
