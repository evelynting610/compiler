package minijava;

import java.io.*;
import java.util.Scanner;

import minijava.Interpreter.*;
import minijava.Machine.Machine;

public class Interp2 {
    
    public static void main(String[] args){

	try{
	    if (args.length != 1) {
		System.err.println("Usage: java Interp filename");
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

	    if (!machineName.equalsIgnoreCase("simple"))
		Main5.fatal ("Interpreter requires the simple architecture, not " + machineName);

	    Machine machine = new minijava.Arch.Simple.Simple();
	    
	    ICode2 icode = new ICode2(machine, scanner, fileBaseName);
	    icode.simulate();
	}
	catch(Exception e){
	    throw new RuntimeException(e);
	}
    }
}