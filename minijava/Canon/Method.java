package minijava.Canon;

import minijava.Temp.*;
import minijava.Tree.*;
import minijava.Frame.Frame;
import java.util.*;

public class Method {

    Label label;
    Stm   body;
    Frame frame;

    Method (Label l, Stm b, Frame f) {
	label = l;
	body = b;
	frame = f;
    }

    String createCanonicalCode() {

	return "method " + label + " " 
	    + frame.getFrameInfo() + "\n" 
	    + canonicalize()
	    + "endMethod\n\n";
    }

    String canonicalize() {

	body = canonicalize(body);

	StmList stms = new StmList();
	appendToList(body, stms);
	BasicBlocks b = new BasicBlocks(stms);
	StmList traced = (new TraceSchedule(b)).stms;

	StringBuffer buf = new StringBuffer();
	minijava.Translate.ICode ic = new minijava.Translate.ICode(null, frame);

	for (Stm s : traced)
	    if (s != null)
		buf.append (ic.process(s));

	return buf.toString();
    }

    /**
     * Canonicalizes this statement.
     *
     * @return an Stm.  The intermediate tree rooted at the Stm
     * contains no ESEQs.  CALL nodes appear only in ESTMs or in
     * MOVEs that go to TEMPs.
     */
    public Stm canonicalize(Stm s) {

	if (s instanceof CJUMP) return canonicalize((CJUMP) s);
	else if (s instanceof ESTM) return canonicalize((ESTM) s);
	else if (s instanceof JUMP) return canonicalize((JUMP) s);
	else if (s instanceof LABEL) return canonicalize((LABEL) s);
	else if (s instanceof MOVE) return canonicalize((MOVE) s);
	else if (s instanceof SEQ)  return canonicalize((SEQ) s);
	else throw new RuntimeException ("Error in Canon/Method.java");
    }

    public Stm canonicalize(CJUMP s) {
	ESEQ l = rewriteCall(canonicalize(s.left));
	ESEQ r = rewriteCall(canonicalize(s.right));
	ESEQ n = reorder(l.exp, r.stm);
	return seq(seq(l.stm,n.stm), new CJUMP(s.relop, n.exp, r.exp, s.iftrue, s.iffalse));
    }

    private SEQ seq(Stm s, Stm t) {
	return new SEQ(s,t);
    }

    public Stm canonicalize(ESTM s) {
	ESEQ e = canonicalize(s.exp);
	if (e.exp instanceof CALL) {
	    if (e.stm == null) 
		return new ESTM(e.exp);
	    else
		return seq(e.stm, new ESTM(e.exp));
	}
	else {
	    if (e.stm == null)
		return new ESTM(new CONST(0));
	    else
		return e.stm;
	}
    }

    public Stm canonicalize(JUMP s) {
	return s;
    }

    public Stm canonicalize(LABEL s) {
	return s;
    }

    public Stm canonicalize(MOVE s) {
	ESEQ l = rewriteCall(canonicalize(s.dst));
	ESEQ r = rewriteCall(canonicalize(s.src));

	if (l.exp instanceof TEMP) {
	    return seq(seq(l.stm,r.stm), new MOVE(l.exp, r.exp));
	}
	else if (l.exp instanceof MEM) {
	    r = rewriteCall(r);
	    Temp ptr = new Temp();
	    Stm  s1  = new MOVE(new TEMP(ptr), ((MEM)l.exp).exp);
	    return seq(seq(seq(l.stm, s1), r.stm), new MOVE(new MEM(new TEMP(ptr)),r.exp));
	}
	else
	    throw new RuntimeException("Bad target in MOVE");
    }

    public Stm canonicalize(SEQ s) {
	return seq(canonicalize(s.left), canonicalize(s.right));
    }

    /**
     * Canonicalizes this expression.
     *
     * @return an ESEQ.  The intermediate tree rooted at the ESEQ
     * contains no other ESEQs.  CALL nodes appear only in ESTMs, in
     * MOVEs that go to TEMPs, or at the root of the expression part
     * of the ESEQ.  The statement part may be null.
     */

    public ESEQ canonicalize(Exp e) {
	if (e instanceof BINOP)      return canonicalize((BINOP)e);
	else if (e instanceof CALL)  return canonicalize((CALL)e);
	else if (e instanceof CONST) return canonicalize((CONST)e);
	else if (e instanceof ESEQ)  return canonicalize((ESEQ)e);
	else if (e instanceof MEM)   return canonicalize((MEM)e);
	else if (e instanceof NAME)  return canonicalize((NAME)e);
	else if (e instanceof TEMP)  return canonicalize((TEMP)e);
	else throw new RuntimeException ("Error in Canon/Method.java");
    }

    public ESEQ canonicalize(BINOP e) {
	ESEQ l = rewriteCall(canonicalize(e.left));
	ESEQ r = rewriteCall(canonicalize(e.right));
	ESEQ n = reorder(l.exp,r.stm);
	return new ESEQ(seq(l.stm, n.stm), new BINOP(e.binop,n.exp,r.exp));
    }

    public ESEQ canonicalize(CALL e) {
	ESEQ f = rewriteCall(canonicalize(e.func));
	ExpList newargs = new ExpList();

	if (!e.args.isEmpty()) {
	    LinkedList<ESEQ> a = new LinkedList<ESEQ>();  // a will be reversed

	    for (Exp e2 : e.args)
		a.addFirst(rewriteCall(canonicalize(e2)));

	    Iterator<ESEQ> it = a.iterator();
	    ESEQ next = it.next();
	    newargs.addFirst(next.exp);
	    Stm stm = next.stm;

	    while (it.hasNext()) {
		next = it.next();
		ESEQ n = reorder(next.exp,stm);
		newargs.addFirst(n.exp);
		stm = seq(next.stm,n.stm);
	    }
	    ESEQ n = reorder(f.exp,stm);
	    f = new ESEQ(seq(f.stm,n.stm), f.exp);
	}
	return new ESEQ(f.stm,new CALL(f.exp,newargs));
    }

    public ESEQ canonicalize(CONST e) {
	return new ESEQ(null, e);
    }

    public ESEQ canonicalize(ESEQ e) {
	e.stm = canonicalize(e.stm);
	ESEQ e2 = rewriteCall(canonicalize(e.exp));
	if (e.stm == null) return e2;
	else return new ESEQ(seq(e.stm, e2.stm),e2.exp);
    }

    public ESEQ canonicalize(MEM e) {
	ESEQ e2 = rewriteCall(canonicalize(e.exp));
	e2.exp = new MEM(e2.exp);
	return e2;
    }

    public ESEQ canonicalize(NAME e) {
	return new ESEQ(null, e);
    }

    public ESEQ canonicalize(TEMP e) {
	return new ESEQ(null, e);
    }

    public ESEQ rewriteCall(ESEQ e) {
	if (e.exp instanceof CALL) {
	    Temp t = new Temp();
	    e.stm = seq(e.stm, new MOVE(new TEMP(t),e.exp));
	    e.exp = new TEMP(t);
	}
	return e;
    }

    /**
     * Reorders code to make the value of this expression available
     * after a statement is executed, rather than before.  In the
     * default case, this can be done by evaluating the expression,
     * saving the value in a Temp, executing the statement, and then
     * accessing the Temp.  This method should be used only if both
     * this expression and the statement have been canonicalized, and it should not be used if this
     * expression is an ESEQ.
     * @param s an Stm, which may be null, representing code to be executed immediately after
     * the evaluation of this expression.
     * @return an ESEQ containing the reordered code.  The tree rooted
     * at the ESEQ is canonicalized, assuming that both this
     * expression and s were previously canonicalized.
     * @throws RuntimeException If this expression is an ESEQ
     */

    public ESEQ reorder(Exp e, Stm s) {
	if (e instanceof CONST || e instanceof NAME)
	    return new ESEQ(s, e);
	else if (e instanceof ESEQ) 
	    throw new RuntimeException("Calling reorderWith on an ESEQ");
	else {
	    if (s == null) return new ESEQ(s, e);
	    Temp t = new Temp();
	    return new ESEQ(seq(new MOVE(new TEMP(t), e), s), new TEMP(t));
	}
    }

    public void appendToList(Stm body, StmList list) {
	if (body instanceof SEQ) {
	    SEQ s = (SEQ) body;
	    appendToList(s.left, list);
	    appendToList(s.right, list);
	}
	else if (body instanceof ESTM) {
	    ESTM e = (ESTM) body;
	    if (e.exp instanceof CALL)
		list.add(e);
	}
	else
	    list.addLast(body);
    }

}