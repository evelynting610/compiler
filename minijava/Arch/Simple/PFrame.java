package minijava.Arch.Simple;

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
    protected static final Temp RAX = new Temp();
    protected static final Temp IN0 = new Temp();
    protected static final Temp IN1 = new Temp();
    protected static final Temp IN2 = new Temp();
    protected static final Temp IN3 = new Temp();
    protected static final Temp IN4 = new Temp();
    protected static final Temp IN5 = new Temp();

    protected static final Temp RV_REG = RAX;
    protected static final Temp SP_REG = RSP;
    protected static final Temp FP_REG = RBP;

    public static final Temp[] inregs = { IN0, IN1, IN2, IN3, IN4, IN5 };

    protected static HashMap<Temp,String> map;
    protected static HashMap<String,Temp> invMap;

    static {
	map = new HashMap<Temp,String>();
	invMap = new HashMap<String,Temp>();

	addToMaps (RBP, "%rbp");
	addToMaps (RSP, "%rsp");
	addToMaps (RAX, "%rax");
	addToMaps (IN0, "%i0");
	addToMaps (IN1, "%i1");
	addToMaps (IN2, "%i2");
	addToMaps (IN3, "%i3");
	addToMaps (IN4, "%i4");
	addToMaps (IN5, "%i5");
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
	return invMap.get(s);
    }

    //////////////////////////////////////////////////////////////////////////
    // makeAccessList(): The parameter is the number of parameters.  
    // A list of Access objects for the parameters is returned.


    public List<Access> createParameterAccesses(int pCount) {
	List<Access> list = new LinkedList<Access>();
	paramCount = pCount;

	if (pCount > 6) 
	    throw new RuntimeException 
		("Sorry: the simple architecture doesn't allow more than six parameters.");

	int i=0;
	for (; i<pCount; ++i)
	    list.add(new InReg(inregs[i]));
	return list;
    }

    //////////////////////////////////////////////////////////////////////////
    // allocLocal allocates a new local variable and returns an access.

    public Access allocLocal () {
	return new InReg (new Temp());
    }

    //////////////////////////////////////////////////////////////////////////
    // procEntryExit1 does the view shift and register save/restores.

    public Stm procEntryExit1(Stm body) {
	// in the simple architecture, there's nothing to do here

	return body;
    }

    public String getFrameInfo() {
	return paramCount + " " + spillArea;
    }

    //////////////////////////////////////////////////////////////////////////
    // These inner classes give the various forms of access used in this architecture

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
