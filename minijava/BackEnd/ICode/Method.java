package minijava.BackEnd.ICode;

import minijava.Temp.*;
import minijava.Tree.Stm;
import minijava.Tree.StmList;
import minijava.BackEnd.Assem.*;
import minijava.BackEnd.RegAlloc.*;
import minijava.BackEnd.Machine.Frame;

public class Method {

    Label label;
    StmList   body;
    Frame frame;

    Method (Label l, StmList b, Frame f) {
	label = l;
	body = b;
	frame = f;
    }

    String createFinalCode() {

	StmList traced = body;

	TempMap tempmap= new CombineMap(frame,new DefaultMap());
	
	InstrList instrs = new InstrList();
	for (Stm s : traced) {
	    instrs.addAll(frame.codegen(s));
	}

	instrs = frame.procEntryExit2 (instrs);
	
	RegAlloc regmap;
	do {
	    //	    System.out.println ("About to call RegAlloc for following code----");
	    //	    for (Instr i : instrs)
	    //		System.out.print (i.format(tempmap));
	
	    regmap = new RegAlloc (frame, instrs, tempmap);
	} while (!regmap.isOK());

	StringBuffer sb = new StringBuffer();

	sb.append(frame.getPrologue());
	
	for (Instr i : instrs)
	    sb.append(i.format(regmap));

	sb.append(frame.getEpilogue());

	return sb.toString();
    }
}