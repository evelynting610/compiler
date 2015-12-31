package minijava.Interpreter;

import minijava.Machine.*;
import minijava.Temp.Label;
import minijava.Temp.Temp;
import minijava.Tree.*;
import minijava.Frame.Frame;
import minijava.Interpreter.*;

import java.util.*;

public class ICode2 extends minijava.Interpreter.ICode {

    public ICode2(Machine m, Scanner sc, String b) {
	super (m, sc, b);
    }

    Method readMethod(Scanner scanner) {
	
	tempMap = new HashMap<String,Temp>();

	String methodName = scanner.next();
	newSymbol(methodName);
	
	Label methodLabel = getLabel(methodName);
	Frame frame = machine.makeFrame(methodLabel, scanner.nextLine());  
	StmList list = new StmList();

	while (true) {
	    String label = scanner.next();
	    if (label.equals("endMethod")) break;

	    
	    newSymbol(label);
	    
	    String line = scanner.nextLine();

	    Scanner ls = new Scanner(line);
	    ls.next();       // throw away the colon
	    
	    String kind = ls.next();
	    
	    if (kind.equals("BINOP")) {
		expMap.put(label, new BINOP(ls.next(),
					    findExp(ls.next()),
					    findExp(ls.next())));
	    }
	    
	    else if (kind.equals("CALL")) {
		Exp f = findExp(ls.next());
		
		ExpList elist = new ExpList();
		while (ls.hasNext())
		    elist.addLast(findExp(ls.next()));

		expMap.put(label, new CALL(f, elist));
	    }
	    
	    else if (kind.equals("CJUMP")) {
		list.addLast(new CJUMP(ls.next(), findExp(ls.next()),
				       findExp(ls.next()),
				       getLabel(ls.next()), getLabel(ls.next())));
	    }
	    else if (kind.equals("CONST")) {
		expMap.put(label, new CONST(Integer.parseInt(ls.next())));
	    }
	    else if (kind.equals("ESTM")) {
		list.addLast(new ESTM(findExp(ls.next())));
	    }
	    else if (kind.equals("JUMP")) {
		list.addLast(new JUMP(getLabel(ls.next())));
	    }
	    else if (kind.equals("LABEL")) {
		String l = ls.next();
		
		newSymbol(l);
		list.addLast(new LABEL(getLabel(l)));
	    }
	    else if (kind.equals("MEM")) {
		expMap.put(label, new MEM(findExp(ls.next())));
	    }
	    else if (kind.equals("MOVE")) {
		list.addLast(new MOVE(findExp(ls.next()), findExp(ls.next())));
	    }
	    else if (kind.equals("NAME")) {
		expMap.put(label, new NAME(getLabel(ls.next())));
	    }
	    else if (kind.equals("TEMP")) {
		expMap.put(label, new TEMP(findTemp(frame, ls.next())));
	    }
	    else
		throw new ICodeException("Unexpected kind of node (" + kind + ") in linearized intermediate code");
	}
	return new Method2(methodLabel, list, frame);
    }
}
