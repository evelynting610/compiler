package minijava.Temp;

/** A Temp represents a register.
 */

public class Temp  {
   private static int count;
   private int num;

    /** 
     * Returns the name of the register.
     * @return the name
     */
   public String toString() {return "t" + num;}

    /** 
     * Class constructor
     */
   public Temp() { 
     num=count++;
   }
}
