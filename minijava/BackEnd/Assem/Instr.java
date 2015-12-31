package minijava.BackEnd.Assem;

import minijava.Temp.TempList;
import minijava.Temp.TempMap;

public abstract class Instr {

    public String assem;

    public abstract TempList use();
    public abstract TempList def();
    public abstract Targets jumps();

    public String format(TempMap m) {
	TempList dst = def();
	TempList src = use();
	StringBuffer s = new StringBuffer();
	int len = assem.length();
	for(int i=0; i<len; i++)
	    if (assem.charAt(i)=='`')
		switch(assem.charAt(++i)) {
		case 's': {
		    int n = Character.digit(assem.charAt(++i),10);
		    s.append(m.tempMap(src.get(n)));
		}
		    break;
		case 'd': {
		    int n = Character.digit(assem.charAt(++i),10);
		    s.append(m.tempMap(dst.get(n)));
		}
		    break;
		case '`': 
		    s.append('`'); 
		    break;
		default: throw new Error("bad Assem format: " + assem);
		}
	    else s.append(assem.charAt(i));
	
	return s.toString();
    }
}
