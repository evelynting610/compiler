package minijava.Interpreter;

import minijava.Machine.*;
import minijava.Temp.Label;
import minijava.Temp.Temp;
import minijava.Tree.*;
import minijava.Frame.Frame;

import java.util.*;

public class Simulator {

    protected HashMap<Label, String> stringMap;
    protected Label                  mainMethod;
    protected HashMap<Label, Value>  globalMap;
    public    HashMap<Label, Method> methodMap;
    public    HashMap<String, Label> labelMap;
    public    HashSet<String>        breakSet;

    public    Scanner                keyboard;

    public Simulator (HashMap<Label, String> s,
		      LinkedList<Label>      globals,
		      LinkedList<Method>     methods,
		      HashMap<String, Label> l,
		      Label                  mm) {
	stringMap = s;
	mainMethod = mm;
	labelMap = l;
	breakSet = new HashSet<String>();

	globalMap = new HashMap<Label, Value>();
	for (Label g : globals)
	    globalMap.put(g, new IntValue(0));

	methodMap = new HashMap<Label, Method>();
	for (Method m : methods)
	    methodMap.put(m.label, m);

	for (Method m : methods)
	    m.initializeSimulator(this);
    }

    public void simulate() {

	keyboard = new Scanner(System.in);

	getInitialCommands();

	/*	for (Label l : globalMap.keySet())
	    System.err.println ("Global " + l + " has value " + globalMap.get(l));

	for (Method m : methodMap.values())
	System.err.println ("Method " + m.label + " exists!");    */

	methodMap.get(mainMethod).simulate();
    }

    int debugLevel = 0;
    boolean stepping = false;

    public void getInitialCommands() {
	handleBreakPoint(null);
    }

    void handleBreakPoint(String breakPlace) {
	if (breakPlace != null)
	    System.err.println ("Reached a breakpoint at " + breakPlace + "\n");
	System.err.print ("interpreter> ");
	while (keyboard.hasNextLine()) {
	    String s = keyboard.nextLine();
	    Scanner sc = new Scanner(s);

	    if (!sc.hasNext()) {
		System.err.println ("Type ? for help.");
	    }
	    else {
		String command = sc.next();
		
		if (command.equals("run")) {
		    stepping = false;
		    return;
		}
		else if (command.equals("step")) {
		    stepping = true;
		    return;
		}
		else if (command.equals("?") || command.equals("help")) {
		    System.err.println ("Commands are:");
		    System.err.println ("\t? or help:     print this menu");
		    System.err.println ("\tmethods:       print a list of the methods");
		    System.err.println ("\tdebug 0:       debug level 0: no automatic display of progress through program");
		    System.err.println ("\tdebug 1:       debug level 1: show entry and exit from each method");
		    System.err.println ("\tdebug 2:       debug level 2: show each statement, but not SEQs:");
		    System.err.println ("\tdebug 3:       debug level 3: show each statement, including SEQs");
		    System.err.println ("\tdebug 4:       debug level 4: show each statement and expression");
		    System.err.println ("\tbreak label:   set a breakpoint at given label (method name, Stm, or Exp)");
		    System.err.println ("\tunbreak label: remove a breakpoint");
		    //		    System.err.println ("\twatch name:    break and print message if the given value (global, temp, or Exp) changes");
		    //		    System.err.println ("\tunwatch name:  remove a watch");
		    System.err.println ("\tprint stack:   print the stack of method calls");
		    //		    System.err.println ("\tprint name:    print a given value (global, temp, or Exp)");
		    System.err.println ("\tprint temps:   print all temps in current method activation");
		    System.err.println ("\tprint prevtemps:   print all temps in previous method activation on the stack");
		    System.err.println ("\tprint globals: print all globals");
		    System.err.println ("\trun:           begin execution");
		    System.err.println ("\tstep:          execute the current statement");
		    System.err.println  ("\tquit:          quit");
		    System.err.println ();
		    System.err.println ("All of these commands are available whenever a breakpoint is reached.");
		    System.err.println ();
		    System.err.println ("When stepping, type b or ? to go back to interp> command line.");
		    System.err.println ("Typing anything else will cause a step forward.");
		}
		else if (command.equals("debug")) {
		    if (!sc.hasNext()) System.err.println ("Current debug level is " + debugLevel);
		    else {
			String l = sc.next();
			if (l.equals("0")) debugLevel = 0;
			else if (l.equals("1")) debugLevel = 1;
			else if (l.equals("2")) debugLevel = 2;
			else if (l.equals("3")) debugLevel = 3;
			else if (l.equals("4")) debugLevel = 4;
			else System.err.println ("Unknown debug level... try again");
			System.err.println ("Debug level is " + debugLevel);
		    }
		}
		else if (command.equals("methods")) {
			System.err.println ("Methods are: ");
			for (Method m : methodMap.values()) {
			    System.err.println ("   " + m.label);
			}
		}
		else if (command.equals("break")) {
		    if (!sc.hasNext()) System.err.println ("break requires that you name a breakpoint");
		    else {
			String s2 = sc.next();
			if (s2.startsWith("Stm") || s2.startsWith("Exp") || labelMap.containsKey(s2)) {
			    breakSet.add(s2);
			    System.err.println ("Breakpoint set");
			}
			else
			    System.err.println ("I don't recognize that label.. be sure it matches a label in the icode");
		    }
		}
		else if (command.equals("print")) {	
		    if (!sc.hasNext()) System.err.println ("print requires that you choose something to print");
		    else {
			String s2 = sc.next();
			if (s2.equals("globals")) {
			    System.err.println ("Global variables:");
			    for (Label l : globalMap.keySet())
				System.err.printf ("%-12s   %s\n",l, globalMap.get(l));
			}
			else if (s2.equals("temps")) {
			    System.err.println ("Temps:");
			    Activation a = currActivation;
			    for (Temp temp: a.tm.keySet()) {
				String ts = a.method.frame.tempMap(temp);
				if (ts == null) ts = temp.toString();
				System.err.printf ("%-12s   %s\n", ts, a.tm.get(temp));
			    }
			}			    
			else if (s2.equals("prevtemps")) {
			    if (activations.size() <= 1)
				System.err.println ("There are no previous activations");
			    else {
				System.err.println ("Temps from previous stack frame:");
				Activation a = activations.getLast();
				for (Temp temp: a.tm.keySet()) {
				    String ts = a.method.frame.tempMap(temp);
				    if (ts == null) ts = temp.toString();
				    System.err.printf ("%-12s   %s\n", ts, a.tm.get(temp));
				}
			    }
			}			    
			else if (s2.equals("stack")) {
			    System.err.println ("Stack of method calls:");
			    for (Activation a : activations) 
				if (a != null) {
				    System.err.printf ("%-15sparams: ", a.method.label);
				    for (Value v : a.args)
					System.err.print ("  " + v);
				    System.err.println();
				}
			}
			else {
			    System.err.println ("Unrecognized symbol");
			}
		    }
		}
		
		else if (command.equals("unbreak")) {
		    if (!sc.hasNext()) System.err.println ("unbreak requires that you name a breakpoint");
		    else {
			String s2 = sc.next();
			if (breakSet.contains(s2)) {
			    breakSet.remove(s2);
			    System.err.println ("Breakpoint removed.");
			}
			else
			    System.err.println ("No such breakpoint");
		    }
		}
		
		else if (command.equals("quit")) {
		    System.exit(0);
		}
		else {
		    System.err.println ("Unknown interpreter command, try again.");
		}
	    }
	    System.err.print ("\ninterpreter> ");
	}
    }
	
    Activation currActivation;
    LinkedList<Activation> activations = new LinkedList<Activation>();

    void newActivation (Activation a, Value... args) {
	activations.addLast(currActivation);
	currActivation = a;

	if (debugLevel > 0) {
	    System.err.print (">>> beginning method " + a.method.label + " with these parameters:");
	    for (Value v : args)
		System.err.print ("  " + v);
	    System.err.println();
	}
	if (breakSet.contains(a.method.label.toString()))
	    handleBreakPoint(a.method.label.toString());
    }

    void newActivation (Activation a, Iterable<Value> args) {
	activations.addLast(currActivation);
	currActivation = a;

	if (stepping || debugLevel > 0) {
	    System.err.print (">>> beginning method " + a.method.label + " with these parameters:");
	    for (Value v : args)
		System.err.print ("  " + v);
	    System.err.println();
	}
	if (breakSet.contains(a.method.label.toString()))
	    handleBreakPoint(a.method.label.toString());
    }

    void endActivation (Value v) {
	if (stepping || debugLevel > 0)
	    System.err.println (">>> finished method " + currActivation.method.label + " with return value " + v);
	currActivation = activations.removeLast();
    }

    void checkIn (Stm s) {
	if (stepping) {
	    System.err.println ("About to start " + s.icode());
	    if (s instanceof MOVE)
		System.err.println ("   Destination is " + ((MOVE)s).dst.icode(currActivation.method.frame));
	    String b = keyboard.nextLine();
	    if (b.equals("b") || b.equals("?"))
		handleBreakPoint(null);
	}
	if (breakSet.contains(s.idString)) 
	    handleBreakPoint(s.idString);

	if (debugLevel > 2 || (debugLevel == 2 && !(s instanceof SEQ)))
	    System.err.println (s.icode());
    }
    
    void checkIn (Exp e) {
	if (stepping) {
	    System.err.println ("About to start " + e.icode(currActivation.method.frame));
	    String b = keyboard.nextLine();
	    if (b.equals("b") || b.equals("?"))
		handleBreakPoint(null);
	}
	if (breakSet.contains(e.idString)) 
	    handleBreakPoint(e.idString);

	if (debugLevel > 3) 
	    System.err.println (e.icode(currActivation.method.frame));
    }

    void checkOut (Stm s) {
	if (stepping) {
	    System.err.println ("Done with " + s.icode());
	    if (s instanceof MOVE)
		System.err.println ("   Destination was " + ((MOVE)s).dst.icode(currActivation.method.frame));
	    String b = keyboard.nextLine();
	    if (b.equals("b") || b.equals("?"))
		handleBreakPoint(null);
	}
	if (debugLevel > 2 || (debugLevel == 2 && !(s instanceof SEQ)))
	    System.err.println ("---ended " + s.icode());
    }
    
    void checkOut (Exp e) {
	if (stepping) {
	    System.err.println ("Done with " + e.icode(currActivation.method.frame) + ".  Value is " + currActivation.peekExpValue(e));
	    String b = keyboard.nextLine();
	    if (b.equals("b") || b.equals("?"))
		handleBreakPoint(null);
	}
	if (debugLevel > 3) {
	    System.err.println ("---ended " + e.icode(currActivation.method.frame) + ".  Value is " + currActivation.peekExpValue(e));
	}
    }
}