package minijava.Canon;

import minijava.Tree.*;
import minijava.Temp.*;

import java.util.*;

public class BasicBlocks {
    public List<StmList> blocks;
    public Label done;

    // Each basic block begins with a LABEL and ends with a JUMP or CJUMP.
    // Instance variable done contains the Label for the final JUMP

    public BasicBlocks(StmList list) {
	done = new Label();
	blocks = new LinkedList<StmList>();
	list.add(new JUMP(done));

	StmList currBlock = null;

	for (Stm s : list) {

	    if (currBlock == null) {
		currBlock = new StmList();
		blocks.add(currBlock);

		if (!(s instanceof LABEL))
		    currBlock.add(new LABEL(new Label()));
	    }
	    else {
		if (s instanceof LABEL) {
		    currBlock.add(new JUMP(((LABEL)s).label));
		    currBlock = new StmList();
		    blocks.add(currBlock);
		}
	    }
	    currBlock.add(s);
	    if ((s instanceof JUMP) || (s instanceof CJUMP))
		currBlock = null;
	}
    }
}
