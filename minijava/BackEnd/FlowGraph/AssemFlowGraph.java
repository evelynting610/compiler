package minijava.BackEnd.FlowGraph;

import minijava.BackEnd.Assem.*;
import minijava.BackEnd.Graph.Graph;
import minijava.BackEnd.Graph.Node;
import minijava.Temp.*;

public class AssemFlowGraph extends FlowGraph<AssemNode> {

    public AssemFlowGraph (InstrList list) {
	
	java.util.HashMap<Label,AssemNode> labelDict 
	    = new java.util.HashMap<Label,AssemNode>();
	
	for (Instr i : list) {
	    if (i instanceof LABELInstr) {
		LABELInstr label = (LABELInstr)i;
		AssemNode node = new AssemNode (this,label);
		labelDict.put (label.label, node);
	    }
	}
	
	AssemNode prev = null;
	
	for (Instr i : list) {
	    
	    AssemNode node;
	    
	    if (i instanceof LABELInstr) {
		LABELInstr label = (LABELInstr) i;
		node = labelDict.get(label.label);
		if (prev != null) addEdge (prev, node);
		prev = node;
	    }
	    else {
		node = new AssemNode (this,i);
		if (prev != null) addEdge (prev, node);
		
		Targets t = i.jumps();
		if (t == null)
		    prev = node;
		else {         // it's a jump
		    for (Label l : t.labels) {
			AssemNode n = labelDict.get(l);
			addEdge (node, n);
		    }
		    prev = null;
		}
	    }
	}
    }

    public TempList def (AssemNode node) {
	return node.def();
    }    
    
    public TempList use (AssemNode node) {
	return node.use();
    }
    
    public boolean isMove (AssemNode node) {
	return node.isMove();
    }  
}
