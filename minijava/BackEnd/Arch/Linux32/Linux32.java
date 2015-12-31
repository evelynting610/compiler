package minijava.BackEnd.Arch.Linux32;

import java.util.*;
import minijava.BackEnd.Machine.*;
import minijava.Temp.*;

/**
 * An implementation of Machine.Machine for the Linux32 architecture.
 */

public class Linux32 extends minijava.Arch.Linux32.Linux32 implements Machine {

    public Frame makeFrame(Label name, String frameInfo) {
	return new PFrame(name, frameInfo);
    }

    public String makePrologue(String filename, 
			      Map<Label,String> stringMap,
			      Label mainMethodLabel,
			      Collection<Label> globalsVars) {

	StringBuffer sb = new StringBuffer();
	
	sb.append ("\t.file\t\"" + filename + "\"\n");
	sb.append ("\t.section\t.rodata\n");
	
	for (Map.Entry<Label,String> e : stringMap.entrySet())
	    sb.append (e.getKey() + ":\t.string\t" + e.getValue() + "\n");
	    
	sb.append("\n");
	sb.append ("\t.align 4\n\n");
	sb.append ("\t.section\t.data\n");
	for (Label label : globalsVars)
	    sb.append (label + ":\t.long\t0\n");
	sb.append("\n");

	sb.append ("\t.section\t.text\n");
	sb.append ("\t.align 4\n");
	sb.append ("\t.globl main\n");
	sb.append ("\t.type\tmain,@function\n");
	sb.append ("main:\n");
	sb.append ("\tjmp\t" + mainMethodLabel + "\n");

	return sb.toString();
    }
}