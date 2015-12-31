package minijava.Temp;

/**
 * A Label represents an address in assembly language.
 */

public class Label  {
   private String name;
   private static int count;

  /**
   * a printable representation of the label, for use in assembly 
   * language output.
   */
   public String toString() {return name;}

  /**
   * Makes a new label that prints as "name".  Programmers
   * should avoid direct use of this constructor and should instead 
   * call makeLabel() in the Machine.Machine class to
   * create labels for methods or global variables.  
   * @param name the name
   */
   public Label(String name) {
       this.name = name;

       checkForL(name);
   }


    private void checkForL(String name) {
	try {
	    if (name.charAt(0) == 'L') {
		String s = name.substring(1,name.length());
		int val = Integer.parseInt(s);
		if (val >= count) count=val+1;
	    }
	}
	catch (Exception e) {}
    }
	
  /**
   * Makes a new label with an arbitrary name.  It is appropriate to
   * use this constructor to create a label for control flow within a
   * method or to create a label for a string.
   */
   public Label() {
	this("L" + count++);
   }
	
}
