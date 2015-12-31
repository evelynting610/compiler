package minijava.ErrorHandler;
import minijava.lexer.*;

import java.io.StringReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PushbackReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PushbackReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import minijava.node.*;

public class ErrorHandler {

    String filename;

    public ErrorHandler (String filename) {
    	this.filename = filename;
    }

    public String getShortMessage(String s) {
        return s;
    }

    public String getLongMessage(String s) {
	//throw new UnsupportedOperationException();
	LinkedList<Integer> l = new LinkedList<Integer>();  
	Pattern p = Pattern.compile("\\d+");
	Matcher m = p.matcher(s); 
	for(int i=0; i<2; i++){	
		while (m.find()) {
			l.add(Integer.valueOf(m.group()));
		}
	}
	
		
	int row = 0;
	int col = 0;
	if(!l.isEmpty())
		row = l.remove();
	if(!l.isEmpty())
		col = l.remove();
	     
	String target="";
	try {

		String sCurrentLine;

		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		int rowcount = 0;
		while ((sCurrentLine = br.readLine()) != null && rowcount < row-1) {
				//System.out.println(sCurrentLine);
		        rowcount++;
			
	        }
        target = sCurrentLine;
		

	} catch (IOException e) {
		e.printStackTrace();
	}

	String spaces = "";
	for (int i = 0; i < col-1; i ++){
		if (target.charAt(i) == 9){
			spaces = spaces + "\t";
		}else{			
		spaces = spaces + " ";
	}
	}
	String detailedMessage = s+"\nThe error was detected at line "+row+", column "+col+"."+
		"\nHere is line "+row+".  The carat mark (^) indicates where the error was detected.\n"
		+target + "\n" + spaces + "^";	 
	return detailedMessage;
    }
}
