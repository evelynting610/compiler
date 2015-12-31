package minijava.BackEnd.Assem;

import minijava.Temp.*;

public class MOVEInstr extends Instr {
    private Temp dst;
    private Temp src;

    private TempList use, def;

    public MOVEInstr(String a, Temp d, Temp s) {
	assem=a; dst=d; src=s;
	use = new TempList(src);
	def = new TempList(dst);
    }
    public TempList use() {return use;}
    public TempList def() {return def;}
    public Targets jumps()     {return null;}
    
    public Temp getDst() {return dst;}
    public Temp getSrc() {return src;}

    public String format(TempMap m) {
	if (m.tempMap(src).equals(m.tempMap(dst))) return "";
	else return super.format(m);
    }
}
