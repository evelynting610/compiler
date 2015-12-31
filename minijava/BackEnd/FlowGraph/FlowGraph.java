package minijava.BackEnd.FlowGraph;

import minijava.Temp.TempList;
import minijava.Temp.Temp;
import minijava.BackEnd.Graph.Graph;
import minijava.BackEnd.Graph.NodeList;
import minijava.BackEnd.Graph.Node;

/**
 * A control flow graph is a directed graph in which each edge
 * indicates a possible flow of control.  Also, each node in
 * the graph defines a set of temporaries; each node uses a set of
 * temporaries; and each node is, or is not, a <strong>move</strong>
 * instruction.
 *
 * @see AssemFlowGraph
 */

public abstract class FlowGraph<E extends Node<E>> extends Graph<E> {
 /**
  * The set of temporaries defined by this instruction or block 
  */
    public abstract TempList def(E node);

 /**
  * The set of temporaries used by this instruction or block 
  */
    public abstract TempList use(E node);
    
 /**
  * True if this node represents a <strong>move</strong> instruction,
  * i.e. one that can be deleted if def=use. 
  */
    public abstract boolean isMove(E node);
    
 /**
  * Print a human-readable dump for debugging.
  */
    public void show(java.io.PrintStream out) {
	 // XXX: make sure that TempList and NodeList are printable
	
	for (E n : nodes()) {
	    out.print(n + ": " + def(n) + " ");
	    out.print(isMove(n) ? "<= " : "<- ");
	    out.print(use(n) + " ");
	    out.println ("; goto " + nodeListToString(n.succ()));
	}
    }
}
    
    