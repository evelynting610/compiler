package minijava.Translate;

import minijava.Tree.*;
import minijava.Temp.*;

public class Ex extends Expr {

    private Exp exp;

    public Ex (Exp e) {
	exp = e;
    }

    public Exp unEx() {
	return exp;
    }

    public Stm unCx(Label t, Label f) {
	return new CJUMP (CJUMP.NE, exp, new CONST(0), t, f);
    }

    public Stm unNx() {
	return new ESTM(exp);
    }
}