package minijava.Machine;

import minijava.Temp.*;
import minijava.Frame.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An interface specifying architecture-dependent operations required for code generation.
 */
public interface Machine {

    public String getName();

    /**
     * Returns the word size, in bytes.
     * @return the word size
     */
    public int wordSize();

    /**
     * Constructs a Label based on the given string.  Multiple calls
     * on the same string create labels with distinct suffices, so this method should
     * be called only once for each method or global variable.
     * @param s the string
     * @return the Label
     */
    public Label makeLabel (String s);

    /**
     * Constructs a frame for a method.
     * @param l the method's Label
     * @return the Frame
     */
    public Frame makeFrame(Label l);

    /**
     * Constructs a frame for a method.  This method is intended for use in the Interpreter
     * or the back end.
     * @param l the method's Label
     * @param s a string containing two ints, the number of parameters and the spill area, separated by a space
     * @return the Frame
     */
    public Frame makeFrame(Label l, String s);

}
