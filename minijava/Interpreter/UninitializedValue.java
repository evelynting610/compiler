package minijava.Interpreter;

public class UninitializedValue extends Value {

    public String toString() {
	return "uninitialized";
    }

    public String shortName() {
	return "uninitialized value";
    }
}
