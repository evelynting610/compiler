package minijava.Tree;
import java.util.List;
import minijava.Temp.TempMap;

/**
   Stm is the abstract base class for tree nodes representing statements.
*/
abstract public class Stm {

    static int count = 0;
    public String idString;
    
    /**
     * Class constructor
     */
    public Stm() {
	idString = "Stm" + (++count);
    }
    
    /**
     * Returns a string describing this statement.
     *
     * @return the string
     */
    public String toString() {
	return idString + " : ";
    }

    /**
     * Returns the identifying string for this statement.
     *
     * @return the string
     */
    public String idString() {
	return idString;
    }

    public abstract String icode();
}

