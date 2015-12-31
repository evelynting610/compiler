package minijava;

import java.io.*;
import java.util.Scanner;

import minijava.Interpreter.*;
import minijava.Machine.Machine;

public class Interp {
    
    public static void main(String[] args){

	try{
	    if (args.length != 1) {
		System.err.println("Usage: java Interp filename");
		System.exit(1);
	    }

	    String filename = args[0];
	    String fileBaseName = Main4.getBaseName(filename, ".icode1");
	    Scanner scanner = new Scanner(new FileReader(filename));

	    if (!scanner.next().equals("icode1"))
		Main4.fatal ("Wrong filetype keyword given in input file");
	    if (!scanner.next().equals(fileBaseName))
		Main4.fatal ("Wrong filename given in input file");
	    String machineName = scanner.next();
	    if (!machineName.equalsIgnoreCase("simple"))
		Main4.fatal ("Interpreter requires the simple architecture, not " + machineName);

	    Machine machine = new minijava.Arch.Simple.Simple();

	    ICode icode = new ICode(machine, scanner, fileBaseName);
	    icode.simulate();
	}
	catch(ICodeException e) {
	    System.out.println(e);
	    System.exit(1);
	}
	catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }
}