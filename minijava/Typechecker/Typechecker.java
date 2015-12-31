package minijava.Typechecker;

import minijava.node.*;
import minijava.Frame.Access;
import minijava.Machine.Machine;
import minijava.Temp.Label;
import minijava.Translate.Builtins;
import minijava.Translate.Ex;
import minijava.Translate.Expr;
import minijava.Tree.CONST;
import minijava.Tree.ESTM;
import minijava.Tree.Exp;
import minijava.Tree.MEM;
import minijava.Tree.NAME;
import minijava.Tree.SEQ;
import minijava.Tree.Stm;
import minijava.Tree.TEMP;
import minijava.Type.*;

import java.util.*;

public class Typechecker {
	Start root;
	String base;
	Machine machine;
	Builtins builtins;

	StringBuffer stringBuffer;
	HashMap < String, Type > typeMap;
	HashMap < String, Var > classVarMap;
	List < Method > methodList;
	HashMap < String, Method > methodMap;
	HashMap <String, Label> stringMap;
	boolean isClassVar;
	
	LocalSymbolTable localSymbolTable;
	Method currentMethod;
	

	public Typechecker(Start s, String b, Machine m) {
		root = s;
		base = b;
		machine = m;
		builtins = new Builtins(m);

		stringBuffer= new StringBuffer();
		typeMap = new HashMap < String, Type > ();
		typeMap.put("int", Type.intType);
		typeMap.put("String", Type.stringType);
		typeMap.put("void", Type.voidType);
		typeMap.put("boolean", Type.booleanType);

		classVarMap = new HashMap < String, Var > ();
		methodList = new LinkedList < Method > ();
		methodMap = new HashMap <String, Method> ();
		localSymbolTable = new LocalSymbolTable();
		stringMap = new HashMap<String, Label>();
		isClassVar=false;
		
	}

	public void phase1() {
		(new Phase1(this)).process(root);

		Set < String > keySet = classVarMap.keySet();
		for (String key: keySet) {
			System.out.println("classVarMap has "+classVarMap.get(key).type+" "+key);
		}
		System.out.println();
		System.out.println("All the Methods in the Program:");
		for (Method method: methodList) {
			System.out.println();
			System.out.println("method name is " + method.getName());
			List < Type > paramTypes = method.getParamTypes();
			System.out.print("list of parameter types:");

			if (paramTypes != null) {
				boolean first =true;
				for (Type type: paramTypes) {
					if (first){
						System.out.print(type);
						first=false;
					}else{
						System.out.print(", "+type);
					}
				}
			}
		}
		System.out.println();
	}

	public void createClassVar(String name, Type type, Token tok) {
		if (classVarMap.containsKey(name) || type == Type.voidType) {
			//System.out.println("classVarMap contains key or type is VOID");
			throw new TypecheckerException(tok, name);
		} else {
			Var v = new Var(name, type);
			classVarMap.put(name, v);
		}
	}

	public void createMethod(String name, Type returnType,
	List < Type > paramTypes, Token tok) {
		
		Method m = new Method(name,paramTypes);
		if(methodMap.containsKey(m.toString())){
			throw new TypecheckerException(tok, name);
		}
		m.setReturnType(returnType);
		methodMap.put(m.toString(), m);
		methodList.add(m);
		
		}

	

	public Type getType(TId idToken) {
		if (typeMap.containsKey(idToken.getText())) {
			return typeMap.get(idToken.getText());
		} else {
			//System.out.println("ERROR in getType, idToken is " + idToken);
			throw new TypecheckerException(idToken, idToken.getText());
		}
	}

	public Type makeArrayType(Type t, Token tok) {
		if ((t != Type.voidType && typeMap.containsValue(t)) || t instanceof ArrayType) {
			ArrayType array = new ArrayType(t);
			return array;
		} else {
			//System.out.println("MAKE ARRAY TYPE ERROR");
			throw new TypecheckerException(tok, tok.getText());
		}
	}
	public void phase2() {
		for(Method m : methodList){
			Label l = this.machine.makeLabel("m");
			m.setLabel(l);
		}
		(new Phase2(this)).process(root);
		LinkedList<ArrayList<String>> archivedSymbolTable = localSymbolTable.getArchivedTable();
		System.out.println();
		System.out.println("Here are the names of all the local vars put in the symbol table:");
		printContentsOf(archivedSymbolTable);
	}
	
	public void printContentsOf(
			LinkedList<ArrayList<String>> archivedSymbolTable) {
		int count =0;
		for (ArrayList<String> scope : archivedSymbolTable){
			count++;
			System.out.print("\tIn Scope "+count+": ");
			for(String s : scope){
				System.out.print(s+"  ");
			}
			System.out.println();
		}
		
	}

	public Var lookup(String s, Token t) {
		if(localSymbolTable.containsKey(s)){
			isClassVar=false;
			return localSymbolTable.lookup(s);
		}
		else if(classVarMap.containsKey(s)){
			isClassVar=true;
			return classVarMap.get(s);		
		}
		else{
			throw new TypecheckerException(t, "cannot find symbol");
		}
		
	}
	public boolean declareLocal(String text, Var v, Token t){
		
		if(!localSymbolTable.isEmpty() && !localSymbolTable.containsKey(text)){
			localSymbolTable.declareLocal(text, v);
			return true;
		}
		else{
			return false;
		}
		
	}

	public List<Method> getMethodList() {
		return methodList;
	}
	
	public void setCurrentMethodName (String s){
		stringBuffer = new StringBuffer();
		stringBuffer.append(s);
	}
	
	public void setParamTypes(String p){
		stringBuffer.append(" "+p);
	}
	
	public void setCurrentMethod() {
		Method m = methodMap.get(stringBuffer.toString());
		currentMethod=m;		
	}
	
	public List<Access> getParameterAccesses(){
		return currentMethod.paramAccesses;
	}
	
    public Stm noop() {
	return new ESTM(new CONST(0));
    }

    public Stm seq(Stm... list) {
	Stm result = null;
	for (Stm s : list)
	    result = (result == null) ? s : new SEQ(result, s);
	return result;
    }
	
	Method findMainMethod() {
		for (Method m : methodList) {
		if (m.name.equals("main") &&
		m.paramTypes.size() == 1 &&
		m.paramTypes.get(0).equals(new ArrayType(Type.stringType)))
		return m;
		}
		throw new RuntimeException ("no main method!");
		}
	
	public String createICode() {
		StringBuffer sb = new StringBuffer();
		for (String s: stringMap.keySet())
		sb.append ("string " + stringMap.get(s) + " " + s + "\n");
		for (Var v : classVarMap.values())
		sb.append ("globalVar " + v.label + "\n");
		sb.append ("mainMethod " + findMainMethod().label + "\n\n");
		for (Method m : methodList)
		sb.append (m.createICode() + "\n");
		return sb.toString();
		}
	
	public Exp varExp(Var v){
		return v.access.exp(new TEMP(currentMethod.frame.FP()));
		
	}
	
	
	public Expr varExpr(Var v){
		if(isClassVar){
			return new Ex(new MEM(new NAME(v.label)));
		}else{
			return new Ex(varExp(v));
		}
		
	}

}

