package minijava.Canon;

import minijava.Tree.*;
import minijava.Temp.*;

public class TraceSchedule {

    public StmList stms;

    BasicBlocks theBlocks;
    java.util.HashMap<Label,StmList> table = new java.util.HashMap<Label,StmList>();

    void trace (StmList l, StmList out) {
	
	while (true) {

	    LABEL lab = (LABEL)(l.getFirst());
	    table.remove(lab.label);

	    out.addAll(l);
	    Stm s = out.getLast();
	    
	    if (s instanceof JUMP) {
		JUMP j = (JUMP)s;

		StmList target = table.get(j.target);
		if (target != null) {
		    out.removeLast();
		    l = target;
		}
		else return;
	    }

	    else if (s instanceof CJUMP) {
		CJUMP j = (CJUMP)s;
		StmList t = table.get(j.iftrue);
		StmList f = table.get(j.iffalse);

		if (f!=null) {
		    l = f;
		}
		else if (t!=null) {
		    out.removeLast();

		    out.addLast(new CJUMP(notOp(j.relop),
					  j.left,j.right,
					  j.iffalse,j.iftrue));
		    l = t;
		}
		else {
		    Label ff = new Label();
		    out.removeLast();
		    out.addLast(new CJUMP(j.relop,j.left,j.right,
					       j.iftrue,ff));
		    out.addLast(new LABEL(ff));
		    out.addLast(new JUMP(j.iffalse));
		    return;
		}
	    }
	    else throw new Error("Bad basic block in TraceSchedule");
	}
    }

    private int notOp (int op) {
	switch (op) {
	case CJUMP.EQ: return CJUMP.NE;
	case CJUMP.NE: return CJUMP.EQ;
	case CJUMP.LT: return CJUMP.GE;
	case CJUMP.GT: return CJUMP.LE;
	case CJUMP.LE: return CJUMP.GT;
	case CJUMP.GE: return CJUMP.LT;
	default: throw new RuntimeException ("Unsupported relop");
	}
    }

    public TraceSchedule(BasicBlocks b) {
	theBlocks=b;
	
	for (StmList l : b.blocks) {
	    //System.out.println ("BEGIN BLOCK---");
	    //System.out.println (l);
	    //System.out.println ("END BLOCK---");

	    LABEL head = (LABEL)(l.getFirst());
	    table.put (head.label, l);
	}
	stms = new StmList();
	
	for (StmList l : b.blocks) {
	    LABEL head = (LABEL)(l.getFirst());

	    if (table.containsKey(head.label))
		trace(l, stms);
	}
	stms.add(new LABEL(theBlocks.done));
    }        
}


