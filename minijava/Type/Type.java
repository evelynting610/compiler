package minijava.Type;

public abstract class Type {

    public final static Type intType = new IntType();
    public final static Type stringType = new StringType();
    public final static Type nullType = new NullType();
    public final static Type voidType = new VoidType();
    public final static Type booleanType = new BooleanType();

    abstract public String toString();
    public boolean canAssignTo(Type t) { return this.equals(t);}
    public boolean equals(Object o) { return this == o;}
    public boolean sameType(Type t){ return this.toString().equals(t.toString());}
    public boolean primitive(){ return true;}	

    static class IntType extends Type {
	public String toString() {
	    return "int";
	}
    }

    static class StringType extends Type {
	public String toString() {
	    return "String";
	}
	public boolean primitive(){
		return false;
	}
    }

    static class BooleanType extends Type {
	public String toString() {
	    return "boolean";
	}
    }

    static class NullType extends Type {
	public String toString() {
	    return "null";
	}
	public boolean canAssignTo (Type t) {
		
	    return t == this || t instanceof ArrayType || t == Type.stringType;
	}
    }

    static class VoidType extends Type {
	public String toString() {
	    return "void";
	}
	public boolean canAssignTo (Type t) {
	    return false;
	}
    }
}
