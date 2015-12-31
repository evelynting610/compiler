package minijava.Translate;

import minijava.Tree.*;
import minijava.Temp.*;

public class ICode {

    TempMap map;
    Stm     code;

    static class SimpleMap implements TempMap {
	public String tempMap(Temp t) {
	    return t.toString();
	}
    }

    public ICode (Stm stm, TempMap m) {
	code = stm;
	map = m;
    }

    public ICode (Stm stm) {
	this (stm, new DefaultMap());
    }

    public String toString() {
	return process(code);
    }

    public String process(Stm s) {

	if (s instanceof CJUMP) return process((CJUMP) s);
	else if (s instanceof ESTM) return process((ESTM) s);
	else if (s instanceof JUMP) return process((JUMP) s);
	else if (s instanceof LABEL) return process((LABEL) s);
	else if (s instanceof MOVE) return process((MOVE) s);
	else if (s instanceof SEQ)  return process((SEQ) s);
	else throw new RuntimeException ("Error in ICode.java: " + s);
    }

    public String process(CJUMP s) {
	return process(s.left) + process(s.right) + s.icode() + "\n";
    }

    public String process(ESTM s) {
	return process(s.exp) + s.icode() + "\n";
    }

    public String process(JUMP s) {
	return s.icode() + "\n";
    }

    public String process(LABEL s) {
	return s.icode() + "\n";
    }

    public String process(MOVE s) {
	return process(s.dst) + process(s.src) + s.icode() + "\n";
    }

    public String process(SEQ s) {
	return process(s.left) + process(s.right) + s.icode() + "\n";
    }

    public String process(Exp e) {
	if (e instanceof BINOP)      return process((BINOP)e);
	else if (e instanceof CALL)  return process((CALL)e);
	else if (e instanceof CONST) return process((CONST)e);
	else if (e instanceof ESEQ)  return process((ESEQ)e);
	else if (e instanceof MEM)   return process((MEM)e);
	else if (e instanceof NAME)  return process((NAME)e);
	else if (e instanceof TEMP)  return process((TEMP)e);
	else throw new RuntimeException ("Error in ICode.java");
    }

    public String process(BINOP e) {
	return process(e.left) + process(e.right) + e.icode(map) + "\n";
    }

    public String process(CALL e) {
	StringBuffer b = new StringBuffer();
	b.append (process(e.func));
	for (Exp e2 : e.args)
	    b.append(process(e2));
	b.append (e.icode(map) + "\n");
	return b.toString();
    }

    public String process(CONST e) {
	return e.icode(map) + "\n";
    }

    public String process(ESEQ e) {
	return process(e.stm) + process(e.exp) + e.icode(map) + "\n";
    }

    public String process(MEM e) {
	return process(e.exp) + e.icode(map) + "\n";
    }

    public String process(NAME e) {
	return e.icode(map) + "\n";
    }

    public String process(TEMP e) {
	return e.icode(map) + "\n";
    }

}
