package minijava.BackEnd.Assem;

import minijava.Temp.*;

public class LABELInstr extends Instr {
    public Label label;

    public LABELInstr(String a, Label l) {
	assem=a; label=l;
    }
    
    // It's OK for labels to share one empty list, because it's initially empty
    // and TempLists never increase in length.

    private static TempList emptyList = new TempList();

    public TempList use() { return emptyList; }
    public TempList def() { return emptyList; }
    public Targets jumps() { return null; }

    
}
