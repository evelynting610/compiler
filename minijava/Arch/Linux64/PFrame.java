package minijava.Arch.Linux64;

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

    protected static final Temp RBP = new Temp();
    protected static final Temp RSP = new Temp();
    protected static final Temp RBX = new Temp();
    protected static final Temp RSI = new Temp();
    protected static final Temp RDI = new Temp();
    public    static final Temp RAX = new Temp();
    protected static final Temp RCX = new Temp();
    public    static final Temp RDX = new Temp();
    protected static final Temp R8  = new Temp();
    protected static final Temp R9  = new Temp();
    protected static final Temp R10 = new Temp();
    protected static final Temp R11 = new Temp();
    protected static final Temp R12 = new Temp();
    protected static final Temp R13 = new Temp();
    protected static final Temp R14 = new Temp();
    protected static final Temp R15 = new Temp();

    protected static final Temp RV_REG = RAX;
    protected static final Temp SP_REG = RSP;
    protected static final Temp FP_REG = RBP;

    public    static final TempList callersaves = new TempList(RDI, RSI, RDX, RCX, R8, R9, R10, R11, RAX);
    protected static final TempList calleesaves = new TempList(RBX, R12, R13, R14, R15);
    protected static final TempList specialregs = new TempList(RSP, RBP);

    protected static final TempList returnSink  = new TempList(specialregs, calleesaves);
    protected static final TempSet  regs        = new TempSet (callersaves, returnSink);
    
    protected static HashMap<Temp,String> map;
    protected static HashMap<String,Temp> invMap;

    static {
	map = new HashMap<Temp,String>();
	invMap = new HashMap<String,Temp>();

	addToMaps (RBP, "%rbp");
	addToMaps (RSP, "%rsp");
	addToMaps (RBX, "%rbx");
	addToMaps (RSI, "%rsi");
	addToMaps (RDI, "%rdi");
	addToMaps (RAX, "%rax");
	addToMaps (RCX, "%rcx");
	addToMaps (RDX, "%rdx");
	addToMaps (R8,  "%r8" );
	addToMaps (R9,  "%r9" );
	addToMaps (R10, "%r10");
	addToMaps (R11, "%r11");
	addToMaps (R12, "%r12");
	addToMaps (R13, "%r13");
	addToMaps (R14, "%r14");
	addToMaps (R15, "%r15");
    }

    protected static void addToMaps(Temp t, String s) {
	map.put(t,s);
	invMap.put(s,t);
    }

    protected int paramCount = 0;                        // how many incoming parameters?
    public    int spillArea = 0;

    protected Label label;

    /**
     * Class constructor.
     * @param label the Label for the method
     */

    protected PFrame (Label label) {
	this.label = label;
    }
    
    public PFrame (Label label, String info) {
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
    // A list of Access objects for the parameters is returned.

    public Temp[] parameterRegs = {RDI, RSI, RDX, RCX, R8, R9};
    public Temp[] paramTemps    = new Temp[6];

    public List<Access> createParameterAccesses(int pCount) {
	List<Access> list = new LinkedList<Access>();
	paramCount = pCount;

	int i=0;
	for (i=0; i<pCount && i<6; ++i) {
	    paramTemps[i] = new Temp();
	    list.add(new InReg(paramTemps[i]));
	}
	for (; i<pCount; ++i)
	    list.add(new InFrame(16 + (i-6)*8));
	return list;
    }

    //////////////////////////////////////////////////////////////////////////
    // allocLocal allocates a new local variable and returns an access.

    public Access allocLocal () {
	return new InReg (new Temp());
    }

    //////////////////////////////////////////////////////////////////////////
    // procEntryExit1 does the view shift and register save/restores.  There is
    // no view shift for Linux64

    public Stm procEntryExit1(Stm body) {

	Stm s = body;

	for (int i=0; i<paramCount && i<6; ++i)
	    s = new SEQ(new MOVE (new TEMP(paramTemps[i]), new TEMP(parameterRegs[i])),
			s);

	for (Temp t : calleesaves) {
	    Temp tt = new Temp();
	    s = new SEQ(new MOVE (new TEMP(tt), new TEMP(t)), s);
	    s = new SEQ(s, new MOVE (new TEMP(t), new TEMP(tt)));
	}

	return s;
    }

    public String getFrameInfo() {
	return paramCount + " " + spillArea;
    }

    //////////////////////////////////////////////////////////////////////////
    // These inner classes give the various forms of access used in Linux64

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
