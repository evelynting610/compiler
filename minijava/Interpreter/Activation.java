package minijava.Interpreter;

import minijava.Tree.*;
import minijava.Temp.*;
import minijava.Arch.Simple.PFrame;

import java.util.*;

public class Activation {

    Method method;

    HashMap<Temp,Value> tm = new HashMap<Temp,Value>();
    HashMap<Exp, Value> em = new HashMap<Exp,Value>();
    HashMap<MEM, Value> mm = new HashMap<MEM,Value>();
    
    Label  seekingLabel = null;
    Stm    startingPoint;
    
    Temp[] params = PFrame.inregs;

    Iterable<Value> args;

    Activation(Method m, Value... args) {
	LinkedList<Value> a = new LinkedList<Value>();

	method = m;
	int i=0;
	for (Value v : args) {
	    tm.put (params[i++], v);
	    a.addLast(v);
	}

	this.args = a;
	method.sim.newActivation(this, args);
    }

    Activation(Method m, Iterable<Value> args) {
	method = m;
	this.args = args;

	int i=0;
	for (Value v : args)
	    tm.put (params[i++], v);
	method.sim.newActivation(this, args);
    }

	Value getTempValue (Temp t) {
	    Value v = tm.get(t);
	    if (v != null) return v;
	    else return new UninitializedValue();
	}

	Value peekExpValue (Exp e) {
	    Value v = em.get(e);
	    if (v != null) return v;
	    else return new UninitializedValue();
	}

	Value getExpValue (Exp e) {
	    Value v = em.remove(e);
	    if (v != null) return v;
	    else return new UninitializedValue();
	}

	Value getMemLocation (MEM e) {
	    Value v = mm.remove(e);
	    if (v != null) return v;
	    else return new UninitializedValue();
	}

	Value doIt(Stm body) {
	    do {
		process(body);
	    } while (seekingLabel != null);
	    
	    method.sim.endActivation(getTempValue(method.frame.RV()));
	    return getTempValue(method.frame.RV());
	}

	Value doIt(StmList list) {
	    do {
		for (Stm s : list)
		    process(s);
	    } while (seekingLabel != null);

	    method.sim.endActivation(getTempValue(method.frame.RV()));
	    return getTempValue(method.frame.RV());
	}

	void process(Stm s) {

	    if (seekingLabel==null)
		method.sim.checkIn(s);

	    //	    System.out.println ("Processing " + s.idString());

	    if (s instanceof CJUMP)  process((CJUMP) s);
	    else if (s instanceof ESTM)  process((ESTM) s);
	    else if (s instanceof JUMP)  process((JUMP) s);
	    else if (s instanceof LABEL)  process((LABEL) s);
	    else if (s instanceof MOVE)  process((MOVE) s);
	    else if (s instanceof SEQ)   process((SEQ) s);
	    else throw new ICodeException (s.idString() + " is an unexpected kind of statement");

	    if (seekingLabel==null)
		method.sim.checkOut(s);
	}

	public void process(CJUMP s) {
	    process(s.left);
	    process(s.right);

	    if (seekingLabel != null) {
		if (startingPoint == s) throw new ICodeException("Couldn't find label " + seekingLabel 
								 + ", used in " + s.idString());
	    }
	    else {
		if (getExpValue(s.left).compare(getExpValue(s.right), s.relop))
		    seekingLabel = s.iftrue;
		else
		    seekingLabel = s.iffalse;
		startingPoint = s;
	    }
	}

	public void process(ESTM s) {
	    process(s.exp);
	}
	
	public void process(JUMP s) {
	    if (seekingLabel != null) {
		if (startingPoint == s) throw new ICodeException("Couldn't find label " + seekingLabel
								 + ", used in " + s.idString());
	    }
	    else {
		seekingLabel = s.target;
		startingPoint = s;
	    }
	}

	public void process(LABEL s) {
	    if (seekingLabel == s.label)
		seekingLabel = null;
	}

	public void process(MOVE s) {
	    if (s.dst instanceof MEM) {
		MEM m = (MEM)s.dst;
		process(m.exp);

		if (seekingLabel == null) {
		    Value v = getExpValue(m.exp);
		    mm.put(m, v);
		}
	    }
		
	    process(s.src);

	    if (seekingLabel == null) {
		Value sv = getExpValue(s.src);
		if (s.dst instanceof TEMP) {
		    Temp t = ((TEMP)s.dst).temp;
		    tm.put (t, sv);
		    //		    System.out.println ("Moved " + sv + " into " + t);
		}
		else if (s.dst instanceof MEM) {
		    getMemLocation((MEM)s.dst).storeInMemory(sv);
		}
		else
		    throw new ICodeException (s.idString() + ": destination of a MOVE must be TEMP or MEM");
	    }
	}
	
	public void process(SEQ s) {
	    process(s.left);
	    process(s.right);
	}

	public void process(Exp e) {

	    if (seekingLabel==null)
		method.sim.checkIn(e);

	    //	    System.out.println ("Processing " + e.idString);

	    if (e instanceof BINOP)       process((BINOP)e);
	    else if (e instanceof CALL)   process((CALL)e);
	    else if (e instanceof CONST)  process((CONST)e);
	    else if (e instanceof ESEQ)   process((ESEQ)e);
	    else if (e instanceof MEM)    process((MEM)e);
	    else if (e instanceof NAME)   process((NAME)e);
	    else if (e instanceof TEMP)   process((TEMP)e);
	    else throw new ICodeException ("Unexpected kind of expression in " + e.idString);

	    if (seekingLabel==null)
		method.sim.checkOut(e);
	}

	public void process(BINOP e) {
	    process(e.left);
	    process(e.right);

	    if (seekingLabel == null) {
		Value lv = getExpValue(e.left);
		Value rv = getExpValue(e.right);
		em.put(e, lv.doBinop(rv, e.binop));
	    }
	}

	public void process(CALL e) {
	    process(e.func);
	    for (Exp ex : e.args)
		process(ex);

	    if (seekingLabel == null) {
		Label funcLabel = getExpValue(e.func).getFunctionLabel();
		List<Value> values = new LinkedList<Value>();
		for (Exp ex : e.args)
		    values.add(getExpValue(ex));

		String funcName = funcLabel.toString();

		if (funcName.equals("b_intToString_0")) {
		    if (values.size() != 1)
			throw new ICodeException(e.idString + ": wrong number of arguments in call to " + funcName);
		    em.put(e,new StringValue(values.get(0).getInt() + ""));
		}
		else if (funcName.equals("b_stringConcatenate_0")) {
		    if (values.size() != 2)
			throw new ICodeException(e.idString + ": wrong number of arguments in call to " + funcName);
		    em.put(e,new StringValue(values.get(0).getString() + 
					     values.get(1).getString()));
		}
		else if (funcName.equals("b_string_length_0")) {
		    if (values.size() != 1)
			throw new ICodeException(e.idString + ": wrong number of arguments in call to " + funcName);
		    em.put(e,new IntValue(values.get(0).getString().length()));
		}
		else if (funcName.equals("b_createArray_0")) {
		    if (values.size() != 1)
			throw new ICodeException(e.idString + ": wrong number of arguments in call to " + funcName);
		    em.put(e, new ArrayValue(values.get(0).getInt()));
		}
		else if (funcName.equals("b_printString_0")) {
		    if (values.size() != 1)
			throw new ICodeException(e.idString + ": wrong number of arguments in call to " + funcName);
		    System.out.print(values.get(0).getString());
		}
		else {
		    Method m = method.sim.methodMap.get(funcLabel);
		    if (m == null)
			throw new ICodeException(e.idString + ": trying to call non-existent function " + funcLabel);
		    em.put(e, m.simulate(values));
		}
	    }
	}

	public void process(CONST e) {
	    if (seekingLabel == null)
		em.put(e, new IntValue(e.value));
	}

	public void process(ESEQ e) {
	    process(e.stm);
	    process(e.exp);

	    if (seekingLabel == null)
		em.put(e, getExpValue(e.exp));
	}

	public void process(MEM e) {
	    process (e.exp);

	    if (seekingLabel == null) {
		Value v = getExpValue(e.exp);
		em.put(e, v.loadFromMemory());
		mm.put(e, v);
	    }
	}

	public void process(NAME e) {
	    if (seekingLabel == null)
		em.put(e, new NameValue(e.label,method.sim));
	}

	public void process(TEMP e) {
	    if (seekingLabel == null)
		em.put(e, getTempValue(e.temp));
	}
}
