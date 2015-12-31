package minijava.BackEnd.Arch.Linux32;

import minijava.BackEnd.Assem.*;

import minijava.Temp.Temp;
import minijava.Temp.TempList;
import minijava.Temp.Label;

import minijava.Tree.Stm;
import minijava.Tree.ESTM;
import minijava.Tree.MOVE;
import minijava.Tree.JUMP;
import minijava.Tree.CJUMP;
import minijava.Tree.LABEL;
import minijava.Tree.ExpList;
import minijava.Tree.Exp;
import minijava.Tree.TEMP;
import minijava.Tree.MEM;
import minijava.Tree.NAME;
import minijava.Tree.BINOP;
import minijava.Tree.CONST;
import minijava.Tree.CALL;

import java.util.List;
import java.util.ListIterator;

/** An instruction selector for the Pentium architecture.
 */
class CodeGen {

    private PFrame frame;

    private InstrList ilist;

    private void emit (Instr inst) {
	if (ilist == null) 
	    ilist = new InstrList();
	ilist.add(inst);
    }

    /**
     * Class constructor
     * @param f the PFrame for the method for which code will be generated.
     */
    CodeGen (PFrame f) {frame = f;}
    
    private static String tab = "\t";
    

    /**
     * Returns an instruction list for a given intermediate tree.
     * @param s an Stm
     * @return an instruction list
     */

    InstrList codegen (Stm s) {
	
	munchStm (s);
	return ilist;
    }

    private void munchStm (Stm s) {
	if (s == null) return;
	else if (s instanceof ESTM) munchStm ((ESTM) s);
	else if (s instanceof JUMP) munchStm ((JUMP) s);
	else if (s instanceof CJUMP) munchStm ((CJUMP) s);
	else if (s instanceof LABEL) munchStm ((LABEL) s);
	else if (s instanceof MOVE) munchStm ((MOVE) s);
	else throw new RuntimeException ("unknown Stm");
    }
    
    private void munchStm (ESTM s) {
	throw new UnsupportedOperationException();
    }

    private void munchStm (MOVE s) {
	throw new UnsupportedOperationException();
    }

    private void munchStm (LABEL s) {
	throw new UnsupportedOperationException();
    }
      
    private void munchStm (JUMP s) {
	throw new UnsupportedOperationException();
    }

    private void munchStm (CJUMP s) {
	throw new UnsupportedOperationException();
    }

}
