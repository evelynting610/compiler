package minijava;

import java.io.*;

import minijava.ErrorHandler.*;
import minijava.Typechecker.*;
import minijava.Machine.*;

import minijava.lexer.*;
import minijava.parser.*;
import minijava.node.*;

public class Main5 {
    
    public static void main (String[] args) {

	ErrorHandler errorHandler = null;

	try{
	    int argIndex = 0;

	    checkForMoreArgs(argIndex, args);

	    Machine machine;

	    if (args[argIndex].equals("-target")) {
		argIndex++;
		checkForMoreArgs(argIndex, args);
		machine = getMachine(args[argIndex++]);
	    }
	    else
		machine = getMachine();

	    checkForMoreArgs(argIndex, args);
	    String filename = args[argIndex];
	    String fileBaseName = getBaseName(filename, ".java");
	    Reader in = new FileReader(filename);
	    errorHandler = new ErrorHandler(filename);

	    Lexer l = new Lexer(new PushbackReader(in, 1024));
	    Parser p = new Parser(l);
	    Start startNode = p.parse();

	    Typechecker typechecker 
		= new Typechecker(startNode, fileBaseName, machine);

	    typechecker.phase1();
	    typechecker.phase2();

	    String outfile = fileBaseName + ".icode1";
	    PrintWriter out = new PrintWriter(new FileWriter(outfile));

	    out.println ("icode1 " + fileBaseName + " " + machine.getName());
	    out.print   (typechecker.createICode());
	    out.close();

	}
	catch(LexerException e) {
	    System.err.println(e);
	    System.err.println(errorHandler.getLongMessage(e.getMessage()));
	    System.exit(1);
	}
	catch(ParserException e) {
	    System.err.println(e);
	    System.err.println(errorHandler.getLongMessage(e.getMessage()));
	    System.exit(1);
	}
	catch(TypecheckerException e) {
	    System.err.println(e);
	    System.err.println(errorHandler.getLongMessage(e.getMessage()));
	    System.exit(1);
	}
	catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public static String getBaseName (String filename, String requiredSuffix) {

	if (!filename.endsWith(requiredSuffix))
	    fatal ("Filename must have " + requiredSuffix + " suffix.");
	String f = filename.substring(0, filename.length()-requiredSuffix.length());
	int lastSlash = f.lastIndexOf('/');
	if (lastSlash >= 0)
	    f = f.substring(lastSlash+1);
	if (f.length() == 0)
	    fatal ("File name can't be just a suffix!");
	return f;
    }
				      
    public static void checkForMoreArgs(int count, String[] args) {
	if (count >= args.length) {
	    System.err.println ("Usage: java Main [-target arch] filename");
	    System.err.print ("Supported architectures are ");
	    for (Machine m : machines)
		System.err.print (m.getName() + " ");
	    System.err.println();
	    System.exit(1);
	}
    }

    public static void fatal (String s) {
	System.err.println ("Error: " + s);
	System.exit(1);
    }

    public static Machine darwin32 = new minijava.Arch.Darwin32.Darwin32();
    public static Machine darwin64 = new minijava.Arch.Darwin64.Darwin64();
    public static Machine linux32 = new minijava.Arch.Linux32.Linux32();
    public static Machine linux64 = new minijava.Arch.Linux64.Linux64();
    public static Machine simple = new minijava.Arch.Simple.Simple();
    public static Machine[] machines = {darwin32, darwin64, linux32, linux64, simple};

    public static Machine getMachine (String s) {
	for (Machine m : machines)
	    if (s.equalsIgnoreCase(m.getName()))
		return m;
	fatal ("Unknown target architecture " + s);
	return null;
    }

    public static Machine getMachine() {

	String ostype = System.getenv("OSTYPE");
	if (ostype == null) 
	    fatal ("There is no OSTYPE environment variable");

	String machtype = System.getenv("MACHTYPE");
	if (machtype == null) 
	    fatal ("There is no MACHTYPE environment variable");

	if (ostype.equals("linux")) {
	    if (machtype.equals("x86_64"))
		return linux64;
	    else if (machtype.equals("i386"))
		return linux32;
	}
	else if (ostype.equals("darwin")) {
	    if (machtype.equals("x86_64")) {
		return darwin64;
	    }
	    else if (machtype.equals("i386")) {
		return darwin32;
	    }
	}	    
	fatal ("Unknown target architecture " + ostype + " " + machtype);
	return null;
    }
}