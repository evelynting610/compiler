package minijava;

import java.io.*;
import java.util.Scanner;

import minijava.Canon.*;
import minijava.Machine.Machine;

public class Canonical {
    
    public static void main(String[] args){

	try{
	    if (args.length != 1) {
		System.err.println("Usage: java Canonical filename");
		System.exit(1);
	    }

	    String filename = args[0];
	    String fileBaseName = Main5.getBaseName(filename, ".icode1");
	    Scanner scanner = new Scanner(new FileReader(filename));

	    if (!scanner.next().equals("icode1"))
		Main5.fatal ("Wrong filetype keyword given in input file");
	    if (!scanner.next().equals(fileBaseName))
		Main5.fatal ("Wrong filename given in input file");

	    String machineName = scanner.next();
	    Machine machine = Main5.getMachine(machineName);
	    
	    ICode icode = new ICode(machine, scanner, fileBaseName);

	    String outfile = fileBaseName + ".icode2";
	    PrintWriter out = new PrintWriter(new FileWriter(outfile));

	    out.println ("icode2 " + fileBaseName + " " + machine.getName());
	    out.print   (icode.createCanonicalCode());
	    out.close();
	    System.exit(0);
	}
	catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }
}