package minijava.Canon;

import minijava.Machine.*;
import minijava.Temp.Label;
import minijava.Temp.Temp;
import minijava.Tree.*;
import minijava.Frame.Frame;

import java.util.*;

public class ICode {

    Machine machine;
    Scanner scanner;
    String baseName;

    HashMap<Label, String> stringMap = new HashMap<Label, String>();

    HashMap<String, Temp>  tempMap;
    HashMap<String, Exp>   expMap    = new HashMap<String, Exp>();
    HashMap<String, Stm>   stmMap    = new HashMap<String, Stm>();
    HashMap<String, Label> labelMap  = new HashMap<String, Label>();

    LinkedList<String> declaredSymbols = new LinkedList<String>();
    LinkedList<Label> globalVars    = new LinkedList<Label>();
    Label mainMethod;

    LinkedList<Method> methods = new LinkedList<Method>();

    public ICode(Machine m, Scanner sc, String b) {
	machine = m;
	scanner = sc;
	baseName = b;

	while (scanner.hasNext()) {
	    
	    String s = scanner.next();
	    
	    if (s.equals("string")) {
		String labelText = scanner.next();
		newSymbol(labelText);
		s = scanner.nextLine();
		s = s.substring(s.indexOf("\""), s.lastIndexOf("\"")+1);
		stringMap.put(getLabel(labelText),s);
	    }

	    else if (s.equals("globalVar")) {
		String globalText = scanner.next();
		newSymbol(globalText);
		globalVars.add(getLabel(globalText));
	    }

	    else if (s.equals("mainMethod")) {
		if (mainMethod != null)
		    throw new RuntimeException("Multiple main methods specified");
		mainMethod = getLabel(scanner.next());
	    }

	    else if (s.equals("method")) {
		methods.add(readMethod(scanner));
	    }
	}
    }

    void newSymbol (String s) {
	if (declaredSymbols.contains(s))
	    throw new RuntimeException ("Multiple declarations of " + s);
	declaredSymbols.add(s);
    }

    Label getLabel(String s) {
	Label l = labelMap.get(s);
	if (l == null) {
	    l = new Label(s);
	    labelMap.put(s,l);
	}
	return l;
    }

    Exp findExp(String s) {
	Exp e = expMap.get(s);
	if (e == null)
	    throw new RuntimeException ("Using undefined symbol " + e);
	return e;
    }

    Stm findStm(String s) {
	Stm e = stmMap.get(s);
	if (e == null)
	    throw new RuntimeException ("Using undefined symbol " + e);
	return e;
    }

    Temp findTemp(Frame frame, String s) {
	Temp t = frame.unMap(s);
	if (t == null) {
	    t = tempMap.get(s);
	    if (t == null) {
		t = new Temp();
		tempMap.put(s, t);
	    }
	}
	return t;
    }
	    
    Method readMethod(Scanner scanner) {
	
	tempMap = new HashMap<String,Temp>();

	String methodName = scanner.next();
	newSymbol(methodName);
	
	Label methodLabel = getLabel(methodName);
	String rootName = scanner.next();
	Frame frame = machine.makeFrame(methodLabel, scanner.nextLine());  

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
		
		ExpList list = new ExpList();
		while (ls.hasNext())
		    list.addLast(findExp(ls.next()));

		expMap.put(label, new CALL(f, list));
	    }
	    
	    else if (kind.equals("CJUMP")) {
		stmMap.put(label, new CJUMP(ls.next(), findExp(ls.next()),
					    findExp(ls.next()),
					    getLabel(ls.next()), getLabel(ls.next())));
	    }
	    else if (kind.equals("CONST")) {
		expMap.put(label, new CONST(Integer.parseInt(ls.next())));
	    }
	    else if (kind.equals("ESEQ")) {
		expMap.put(label, new ESEQ(findStm(ls.next()), findExp(ls.next())));
	    }
	    else if (kind.equals("ESTM")) {
		stmMap.put(label, new ESTM(findExp(ls.next())));
	    }
	    else if (kind.equals("JUMP")) {
		stmMap.put(label, new JUMP(getLabel(ls.next())));
	    }
	    else if (kind.equals("LABEL")) {
		String l = ls.next();
		newSymbol(l);
		stmMap.put(label, new LABEL(getLabel(l)));
	    }
	    else if (kind.equals("MEM")) {
		expMap.put(label, new MEM(findExp(ls.next())));
	    }
	    else if (kind.equals("MOVE")) {
		stmMap.put(label, new MOVE(findExp(ls.next()), findExp(ls.next())));
	    }
	    else if (kind.equals("NAME")) {
		expMap.put(label, new NAME(getLabel(ls.next())));
	    }
	    else if (kind.equals("SEQ")) {
		stmMap.put(label, new SEQ(findStm(ls.next()), findStm(ls.next())));
	    }
	    else if (kind.equals("TEMP")) {
		expMap.put(label, new TEMP(findTemp(frame, ls.next())));
	    }
	    else
		throw new RuntimeException("Unexpected kind of node in intermediate tree");
	}
	return new Method(methodLabel, findStm(rootName), frame);
    }

    public String createCanonicalCode() {
	
	StringBuffer sb = new StringBuffer();

	for (Label l: stringMap.keySet())
	    sb.append ("string " + l + " " + stringMap.get(l) + "\n");

	for (Label l: globalVars)
	    sb.append ("globalVar " + l + "\n");

	sb.append ("mainMethod " + mainMethod + "\n\n");

	for (Method m : methods)
	    sb.append (m.createCanonicalCode() + "\n");

	return sb.toString();
    }
}
