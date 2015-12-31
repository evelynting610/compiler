package minijava.Frame;

import java.util.List;

import minijava.Temp.*;
import minijava.Tree.*;

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

    /** Returns a string (a single line containing the number of
     *  parameters and the size of the spill area) that will become
     *  part of the intermediate code that is emitted.
     *  @return a string
     */
    public String getFrameInfo();

    /** Returns the Temp named by a particular string.
     *  @param s a string
     *  @return a Temp
     */
    public Temp unMap(String s);
}
