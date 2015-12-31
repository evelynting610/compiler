package minijava.Typechecker;

import java.util.*;

import minijava.Temp.Label;
import minijava.Translate.ICode;
import minijava.Tree.Stm;
import minijava.Type.*;
import minijava.Machine.*;
import minijava.Frame.*;

public class Method {
	public String name;
	public List<Type> paramTypes;
	private List<String> paramNames;
	public List<Access> paramAccesses;
	private Type returnType;
	public Label label;
	public Label exitLabel;
	public Frame frame;
	public Var hidden;
	public Stm body;
	
	
	public Method (String name, List<Type> paramTypes){
		this.name = name;
		this.paramTypes = paramTypes;
	
	}
	
	public boolean equals(Method m){
		if (!this.name.equals(m.getName())){
			return false;
		}
		List<Type> oldParams = m.getParamTypes();
		if(oldParams.size()!=paramTypes.size()){
			return false;
		}
		int count =0;
		boolean paramsEqual=true;
		for(Type type : paramTypes){
			 if(!type.canAssignTo(oldParams.get(count))){
				 paramsEqual=false;}
			 count++;
		 }
		return paramsEqual;    	
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Type> getParamTypes() {
		return this.paramTypes;
	}
	
	public void addParamNames(List<String> names){
		this.paramNames=names;
	}
	
	public List<String> getParamNames(){
		return this.paramNames;
	}
	
	public void setReturnType(Type type){
		this.returnType = type;
	}
	
	public Type getReturnType(){
		return returnType;
	}
	
	public String toString(){
		StringBuffer key = new StringBuffer();
		key.append(this.name);
		for(Type t : paramTypes){
			key.append(" "+t.toString());
		}
		return key.toString();
	}
	
	public void setLabel(Label l){
		this.label=l;
	}

	public void makeFrame(Machine m){
		Frame f = m.makeFrame(label);
		frame = f;
		paramAccesses = frame.createParameterAccesses(paramTypes.size());
		
	}
	
	public void setExitLabel(Label e){
		exitLabel =e;
	}
	
	public void setHidden(){
		hidden = new Var("hidden",returnType);
		hidden.setAccess(this);
	}
	
	public void setBody(Stm b) {
		body =b;
	}
	
	public String createICode() {
		Stm b = frame.procEntryExit1(body);
		return "method " + label + " "
		+ b.idString() + " "
		+ frame.getFrameInfo() + "\n"
		+ new ICode(b, frame)
		+ "endMethod\n\n";
		}



	
	 

}

	
