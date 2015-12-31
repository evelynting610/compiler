package minijava.BackEnd.Machine;

import minijava.Temp.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An interface specifying more architecture-dependent operations required for code generation.
 */
public interface Machine extends minijava.Machine.Machine {

    /**
     * Constructs a frame for a method.
     * @param name the method's Label
     * @return the Frame
     */
    public abstract Frame makeFrame(Label name, String frameInfo);

    public abstract String makePrologue (String filename, 
				Map<Label,String> strings,
				Label mainMethod,
				Collection<Label> globalVars);
}
