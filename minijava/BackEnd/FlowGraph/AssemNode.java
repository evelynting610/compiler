package minijava.BackEnd.FlowGraph;

import minijava.BackEnd.Graph.*;
import minijava.BackEnd.Assem.*;
import minijava.Temp.*;

public class AssemNode extends Node<AssemNode> {
    
    public Instr instr;
    
    AssemNode (Graph<AssemNode> g, Instr i) {
	super(g);
	instr = i;
    }
    
    TempList def() {
	return instr.def();
    }
    
    TempList use() {
	return instr.use();
    }
    
    boolean isMove() {
	return (instr instanceof MOVEInstr);
    }

    public String toString() {
	return instr.toString();
    }
}
