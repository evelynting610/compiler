package minijava.Translate;

import minijava.Machine.Machine;
import minijava.Temp.*;

public class Builtins {

    public Builtins(Machine machine) {
	intToString       = machine.makeLabel("b_intToString");
	stringConcatenate = machine.makeLabel("b_stringConcatenate");
	stringLength      = machine.makeLabel("b_string_length");
	createArray       = machine.makeLabel("b_createArray");
	printString       = machine.makeLabel("b_printString");
    }

    /* static String intToString(int i): */
    public Label intToString;


    /* static String stringConcatenate(String a, String b) */
    public Label stringConcatenate;


    /* static int stringLength(String a)  */
    public Label stringLength;

    /* static ptr createArray (int length):

       Allocates a space for length+1 words, and returns a pointer to the second word.  The
       first word is an int holding "length"  */

    public Label createArray;

    /* static void print (String s) */

    public Label printString;

}
