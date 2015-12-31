package minijava.Typechecker;

import minijava.Temp.Label;
import minijava.Type.Type;
import minijava.Frame.Access;

public class Var {
	
	String name;
	Type type;
	Label label;
	Access access;

	
	public Var (String n, Type t){
		name = n;
		type=t;
	}
	
	public void setLabel (Label l){
		label = l;
	}
	
	public void setAccess (Method m){
		if(m==null)
			System.out.println("in Var, method is "+m);
		access = m.frame.allocLocal();	
	}
	
	public void setAccess(Access a){
		access =a;
	}

}
