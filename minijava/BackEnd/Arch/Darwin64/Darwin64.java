package minijava.BackEnd.Arch.Darwin64;

import java.util.*;

import minijava.Frame.*;
import minijava.Temp.*;
import minijava.BackEnd.Arch.Linux64.*;

/**
 * An implementation of minijava.Machine.Machine.
 */

public class Darwin64 extends Linux64 {

    /**
     * Class constructor
     */
    public Darwin64() {
    }
    
    public String getName() {
	return "Darwin64";
    }

    public Label makeLabel (String s) {
	Integer count = labels.get(s);
	if (count == null) {
	    labels.put (s, 1);
	    return new Label("_" + s + "_0");
	}
	else {
	    labels.put(s, count+1);
	    return new Label("_" + s + "_" + count);
	}
    }

    public String makePrologue(String filename, 
			       Map<Label,String> stringMap,
			       Label mainMethodLabel,
			       Collection<Label> globalsVars) {

	StringBuffer sb = new StringBuffer();
	
	if (stringMap.size() > 0) {
	    sb.append ("\t.cstring\n");
	    for (Map.Entry<Label,String> e : stringMap.entrySet()) {
		sb.append (e.getKey() + ":\t.ascii\t" + e.getValue() + "\n");
		sb.append ("\t.ascii\t " + "\"\\0\"\n");
	    }
	}
	sb.append("\n");
	sb.append ("\t.text\n");
	for (Label label : globalsVars)
	    sb.append (".lcomm\t" + label + ",4,2\n");
	sb.append("\n");

	sb.append ("\t.text\n");
	sb.append ("\t.globl _main\n");
	sb.append ("_main:\n");
	sb.append ("\tjmp\t" + mainMethodLabel + "\n");

	return sb.toString();
    } 
}