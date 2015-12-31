package minijava.Interpreter;

public class StringValue extends Value {

    String value;

    public StringValue (String v) {
	value = v;
    }
    
    public String toString() {
	return value;
    }

    public String getString() {
	return value;
    }

    public String shortName() {
	return "string";
    }
}
