package minijava.Translate;

import minijava.node.Token;
import minijava.Tree.*;
import minijava.Temp.*;

public class CxRel extends Expr {

    Expr left;
    Expr right;
    Token token;

    public CxRel (Token t, Expr l, Expr r) {
	token = t;
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
	String s = token.getText();
	if (s.equals("<"))
	    return new CJUMP(CJUMP.LT, left.unEx(), right.unEx(), t, f);
	else if (s.equals(">"))
	    return new CJUMP(CJUMP.GT, left.unEx(), right.unEx(), t, f);
	else if (s.equals("<="))
	    return new CJUMP(CJUMP.LE, left.unEx(), right.unEx(), t, f);
	else if (s.equals(">="))
	    return new CJUMP(CJUMP.GE, left.unEx(), right.unEx(), t, f);
	else if (s.equals("=="))
	    return new CJUMP(CJUMP.EQ, left.unEx(), right.unEx(), t, f);
	else if (s.equals("!="))
	    return new CJUMP(CJUMP.NE, left.unEx(), right.unEx(), t, f);
	else
	    throw new RuntimeException("Internal error, unrecognized relop");
    }

    public Stm unNx() {
	return new SEQ (left.unNx(), right.unNx());
    }
}