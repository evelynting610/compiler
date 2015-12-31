package minijava.BackEnd.Assem;

import minijava.Temp.*;

public class OPERInstr extends Instr {
    private TempList dst;   // dst and src are non-null
    private TempList src;
    private Targets jumps;       // jumps is null if there are no jumps
    
    public OPERInstr(String a, TempList d, TempList s, Label... targets) {
	assem=a; 
	dst = (d == null) ? new TempList() : d;
	src = (s == null) ? new TempList() : s;
	if (targets.length > 0)
	    jumps = new Targets(targets);
    }

   public TempList use() {return src;}
   public TempList def() {return dst;}
   public Targets jumps() {return jumps;}

}
