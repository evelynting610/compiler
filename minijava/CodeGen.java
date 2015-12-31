package minijava;

import java.io.*;
import java.util.Scanner;

import minijava.BackEnd.ICode.*;
import minijava.BackEnd.Machine.Machine;

public class CodeGen {
    
    public static void main(String[] args){

	try{
	    if (args.length < 1) {
		System.err.println("Usage: java CodeGen filename");
		System.exit(1);
	    }

	    String filename = args[0];
	    String fileBaseName = Main5.getBaseName(filename, ".icode2");
	    Scanner scanner = new Scanner(new FileReader(filename));

	    if (!scanner.next().equals("icode2"))
		Main5.fatal ("Wrong filetype keyword given in input file");
	    if (!scanner.next().equals(fileBaseName))
		Main5.fatal ("Wrong filename given in input file");

	    String machineName = scanner.next();
	    Machine machine = getMachine(machineName);

	    ICode icode = new ICode(machine, scanner, fileBaseName);

	    String outfile = fileBaseName + ".s";
	    PrintWriter out = new PrintWriter(new FileWriter(outfile));
	    out.print (icode.createFinalCode());
	    out.close();
	}

	catch(Exception e){
	    throw new RuntimeException(e);
	}
    }

    //    public static Machine darwin32 = new minijava.BackEnd.Arch.Darwin32.Darwin32();
    //    public static Machine linux32 = new minijava.BackEnd.Arch.Linux32.Linux32();
    public static Machine linux64 = new minijava.BackEnd.Arch.Linux64.Linux64();
    public static Machine darwin64 = new minijava.BackEnd.Arch.Darwin64.Darwin64();

    public static Machine[] machines = {/*darwin32, linux32, */ linux64, darwin64};

    public static Machine getMachine (String s) {
	if (s.equalsIgnoreCase("simple"))
	    Main5.fatal("You can't generate machine code for the simple architecture.");
	for (Machine m : machines)
	    if (s.equalsIgnoreCase(m.getName()))
		return m;
	Main5.fatal ("Unknown target architecture " + s);
	return null;
    }
}