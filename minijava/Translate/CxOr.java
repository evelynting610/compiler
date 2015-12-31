package minijava.Translate;

import minijava.node.Token;
import minijava.Tree.*;
import minijava.Temp.*;

public class CxOr extends Expr {

    Expr left;
    Expr right;

    public CxOr (Expr l, Expr r) {
	left  = l;
	right = r;
    }

    public Exp unEx() {
	Label tl = new Label();
	Label fl = new Label();
	Label jl = new Label();
	Temp t  = new Temp();
	Stm r = new SEQ(unCx(tl,fl),new LABEL(tl));
	r = new SEQ(r, new MOVE(new TEMP(t), new CONST(1)));
	r = new SEQ(r, new JUMP(jl));
	r = new SEQ(r, new LABEL(fl));
	r = new SEQ(r, new MOVE(new TEMP(t), new CONST(0)));
	r = new SEQ(r, new LABEL(jl));
	return new ESEQ(r, new TEMP(t));
    }

    public Stm unCx(Label t, Label f) {
	Label l = new Label();
	Stm r = new SEQ(left.unCx(t,l), new LABEL(l));
	r = new SEQ(r, right.unCx(t,f));
	return r;
    }

    public Stm unNx() {
	Label l = new Label();
	return new SEQ (unCx(l, l), new LABEL(l));
    }
}