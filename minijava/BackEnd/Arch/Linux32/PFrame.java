package minijava.BackEnd.Arch.Linux32;

import minijava.Temp.*;
import minijava.BackEnd.Machine.Frame;
import minijava.Tree.*;
import minijava.BackEnd.Assem.*;
import minijava.Frame.Access;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A class extending the abstract class minijava.Frame.Frame.
 */

public class PFrame extends minijava.Arch.Linux32.PFrame implements Frame {

    PFrame (Label label, String info) {
	super(label);
    }
    
    //////////////////////////////////////////////////////////////////////////
    // allocSpill allocates space for a spilled temp

    public int allocSpill() {
	
	spillArea += 4;
	return spillArea-4;
    }

    public Instr storeSpill (Temp t, int offset) {
	return new OPERInstr ("\t" + "movl `s0," + offset + "(`s1)\n",
			       null, new TempList(t, SP_REG));
    }

    public Instr restoreSpill (Temp t, int offset) {
	return new OPERInstr ("\t" + "movl " + offset + "(`s0),`d0\n",
			       new TempList(t), new TempList(SP_REG));
	
    }

    //////////////////////////////////////////////////////////////////////////
    // procEntryExit2 sets up the liveness information at the end of a procedure

    public InstrList procEntryExit2 (InstrList body) {
	body.add(new OPERInstr ("", null, returnSink));
	return body;
    }

    public String getPrologue() {
	String prologue = "";
	prologue += "\t.text\n" + "\t.align 4\n";
	prologue += label + ":\n" + "\tpushl\t%ebp\n" + "\tmovl\t%esp,%ebp\n";

	int stackBytesNeeded = spillArea+8;
	int blockOverrun = stackBytesNeeded & 15;
	if (blockOverrun != 0)
	    stackBytesNeeded += (16-blockOverrun);
	prologue += "\tsubl\t$" + (stackBytesNeeded-8) + ", %esp\n";
	return prologue;
    }

    public String getEpilogue() {
	return "\tleave\n\tret\n\n";
    }

    public InstrList codegen (Stm stm) {
	return (new CodeGen(this)).codegen(stm);
    }

    public TempSet registers() {
	return regs;
    }
}
