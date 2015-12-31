package minijava.BackEnd.Machine;

import java.util.List;

import minijava.Temp.*;
import minijava.Tree.*;
import minijava.BackEnd.Assem.*;

/** An interface specifying architecture-specific methods needed to generate code for
    individual methods.
*/

public interface Frame extends minijava.Frame.Frame {

    public Temp unMap(String s);

    /** Adds an instruction to the end of the method to ensure that certain registers are live at the end.
     *  These registers are 1) the callee-save registers, 2) the zero
     *  register, 3) the return address register,  and 4) the stack
     *  pointer.
     *  @param body the body of the method
     *  @return the augmented body
     */
    public InstrList procEntryExit2 (InstrList body);

    /** Generates code for a given statement.
     *  @param stm the statement
     *  @return the list of instructions that is generated
     */
    public InstrList codegen (Stm stm);

    /** Returns the set of hardware registers.
     *  @return the set
     */
    public TempSet registers();

    /** Allocates space in the stack frame for a spilled variable, and
     *  returns the offset, from the frame pointer, of the new space.
     *  @return the offset
     */
    public int allocSpill();

    /** Returns an instruction that stores a given Temp at a given
     *	offset from the frame pointer.
     *  @param t the Temp
     *  @param offset the offset
     *  @return the instruction
     */
    public Instr storeSpill (Temp t, int offset);

    /** Returns an instruction that loads a given Temp from a given 
     *  offset from the frame pointer.
     *  @param t the Temp
     *  @param offset the offset
     *  @return the instruction
     */
    public Instr restoreSpill (Temp t, int offset);

    /** Returns a string that should appear as the initial part of the 
     *  assembly code for the method.
     *  @return the string
     */
    public String getPrologue();

    /** Returns a string that should appear as the final part of the
     *  assembly code for the method.
     *  @return the string
     */
    public String getEpilogue();
}
