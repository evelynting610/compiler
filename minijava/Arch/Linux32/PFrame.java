package minijava.Arch.Linux32;

import minijava.Temp.*;
import minijava.Frame.*;
import minijava.Tree.*;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A class extending the abstract class minijava.Frame.Frame.
 */

public class PFrame implements Frame {

    protected static final Temp EBP = new Temp();
    protected static final Temp ESP = new Temp();
    protected static final Temp EBX = new Temp();
    protected static final Temp ESI = new Temp();
    protected static final Temp EDI = new Temp();
    public    static final Temp EAX = new Temp();
    protected static final Temp ECX = new Temp();
    public    static final Temp EDX = new Temp();

    protected static final Temp FP_REG = EBP;
    protected static final Temp SP_REG = ESP;
    protected static final Temp RV_REG = EAX;

    public    static final TempList callersaves = new TempList(ECX, EDX, EAX);
    protected static final TempList calleesaves = new TempList(EBX, ESI, EDI);
    protected static final TempList specialregs = new TempList(EBP, ESP);

    protected static final TempList returnSink  = new TempList(specialregs, calleesaves);
    protected static final TempSet  regs        = new TempSet (callersaves, returnSink);
    
    protected static HashMap<Temp,String> map;
    protected static HashMap<String,Temp> invMap;

    static {
	map = new HashMap<Temp,String>();
	invMap = new HashMap<String,Temp>();

	addToMaps (EBP, "%ebp");
	addToMaps (ESP, "%esp");
	addToMaps (EBX, "%ebx");
	addToMaps (ESI, "%esi");
	addToMaps (EDI, "%edi");
	addToMaps (EAX, "%eax");
	addToMaps (ECX, "%ecx");
	addToMaps (EDX, "%edx");
    }

    protected static void addToMaps(Temp t, String s) {
	map.put(t,s);
	invMap.put(s,t);
    }

    protected int paramCount = 0;                        // how many incoming parameters?
    protected int spillArea = 0;

    protected Label label;

    /**
     * Class constructor.
     * @param label the Label for the method
     */

    protected PFrame (Label label) {
	this.label = label;
    }
    
    PFrame (Label label, String info) {
	this.label = label;
	Scanner s = new Scanner(info);
	paramCount = s.nextInt();
	spillArea = s.nextInt();
    }
    
    public Label getLabel() { return label; }
    public Temp FP() { return FP_REG; }
    public Temp RV() { return RV_REG; }

    public String tempMap (Temp temp) {
	return (String) map.get(temp);
    }

    public Temp unMap (String s) {
	return (Temp) invMap.get(s);
    }

    //////////////////////////////////////////////////////////////////////////
    // makeAccessList(): The parameter is the number of parameters.  
    // It doesn't matter if they escape
    // or not, because Linux32 keeps all parameters on the stack. A list
    // of Access objects for the parameters is returned.

    public List<Access> createParameterAccesses(int pCount) {
	List<Access> list = new LinkedList<Access>();
	paramCount = pCount;
	for (int i=0; i<pCount; ++i)
	    list.add(new InFrame(8 + i*4));
	return list;
    }

    //////////////////////////////////////////////////////////////////////////
    // allocLocal allocates a new local variable and returns an access.

    public Access allocLocal () {
	return new InReg (new Temp());
    }

    //////////////////////////////////////////////////////////////////////////
    // procEntryExit1 does the view shift and register save/restores.  There is
    // no view shift for Linux32

    public Stm procEntryExit1(Stm body) {
	Temp t1 = new Temp();
	Temp t2 = new Temp();
	Temp t3 = new Temp();
	Stm  s1 = new MOVE (new TEMP(t1), new TEMP(EBX));
	Stm  s2 = new MOVE (new TEMP(t2), new TEMP(ESI));
	Stm  s3 = new MOVE (new TEMP(t3), new TEMP(EDI));
	Stm  s4 = body;
	Stm  s5 = new MOVE (new TEMP(EDI), new TEMP(t3));
	Stm  s6 = new MOVE (new TEMP(ESI), new TEMP(t2));
	Stm  s7 = new MOVE (new TEMP(EBX), new TEMP(t1));

	Stm s = new SEQ(s1, s2);
	s = new SEQ(s, s3);
	s = new SEQ(s, s4);
	s = new SEQ(s, s5);
	s = new SEQ(s, s6);
	s = new SEQ(s, s7);
	return s;
    }

    public String getFrameInfo() {
	return paramCount + " " + spillArea;
    }

    //////////////////////////////////////////////////////////////////////////
    // These inner classes give the various forms of access used in Linux32

    protected class InFrame extends Access {
	int offset;

	InFrame (int o) {
	    offset=o;
	}

	public Exp exp (Exp fp) {
	    Exp address = new BINOP (BINOP.PLUS, fp, new CONST(offset));
	    return new MEM(address);
	}

	public String toString() {
	    return "in method " + label + " at offset " + offset;
	}
    }

    protected class InReg extends Access {
	Temp reg;

	InReg(Temp t) {
	    reg = t;
	}

	public Exp exp (Exp fp) {
	    return new TEMP(reg);
	}

	public String toString() {
	    return "in " + reg;
	}
    }
}
