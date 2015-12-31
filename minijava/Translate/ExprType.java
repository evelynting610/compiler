package minijava.Translate;

import minijava.Type.Type;

public class ExprType {
    public Expr expr;
    Type type;
    
    public ExprType (Expr e, Type t) {
	expr = e;
	type = t;
    }
    
    public Type getType(){
    	return type;
    }
}
