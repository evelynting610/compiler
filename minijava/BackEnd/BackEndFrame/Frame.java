package minijava.BackEnd.BackEndFrame;

import java.util.List;
import minijava.Frame.*;

import minijava.Temp.*;
import minijava.Tree.*;
import minijava.BackEnd.Assem.*;

/** An interface specifying architecture-specific methods needed to generate code for
    individual methods.
*/

public interface Frame extends TempMap {
    /** Returns the Label for the method. 
     *  @return the Label
     */
    public Label getLabel();

    /** Creates Access objects for the formal parameters.
     *  @param count the number of parameters.  All are assumed to be non-escaping.
     *  @return a list of Access objects for the parameters
     */
    public List<Access> createParameterAccesses(int count);

    /** Creates an Access object for a local variable.  The variable is assumed to be non-escaping.
     *  @return an Access
     */
    public Access allocLocal ();
    
    /** Returns the Temp that represents the frame pointer.
     *  @return a Temp
     */
    public Temp FP();

    /** Returns the Temp in which the return value should be stored when the method returns.
     *  @return a Temp
     */
    public Temp RV();

    /** Augment the intermediate code for a method to include the "view shift".  This includes
     *  ensuring that incoming parameters are stored in registers, saving callee-save registers into
     *  Temps at the beginning, and restoring callee-save registers at the end.
     *  @param body intermediate code
     *  @return augmented intermediate code
     */
    public Stm procEntryExit1(Stm body);

    /** Returns a string (a single line) that will become part of the intermediate code that is emitted.
     *  @return a string
     */
    public String getFrameInfo();

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
