package minijava.Typechecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class LocalSymbolTable {
	private LinkedList<ArrayList<String>> stackOfLists = new LinkedList<ArrayList<String>>();
	private HashMap<String, Var> map = new HashMap<String, Var>();
	private LinkedList<ArrayList<String>> archivedTable = new LinkedList<ArrayList<String>>();
	
	public LinkedList<ArrayList<String>> getArchivedTable() {
		return archivedTable;
	}
	
	public Var lookup(String s){
		return map.get(s);
	}
	
	public boolean containsKey(String text){
		if(map.containsKey(text))
			return true;
		else{
			return false;
		}
	}
	
	public boolean isEmpty(){
		return stackOfLists.isEmpty();
	}
	
	public void declareLocal(String s, Var v){
		ArrayList<String> listOfLocalVars = stackOfLists.pop();
		listOfLocalVars.add(s);
		stackOfLists.push(listOfLocalVars);
		map.put(s, v);
		
	}
	public void increaseScope(){
		ArrayList<String> listOfLocalVars = new ArrayList<String>();
		stackOfLists.push(listOfLocalVars);
	}
	public void decreaseScope(){
		ArrayList<String> prevScope = stackOfLists.pop();
		archivedTable.push(prevScope);
		for (String s : prevScope){
			map.remove(s);
		}
	}
	
	
}
