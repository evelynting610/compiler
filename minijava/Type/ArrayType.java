package minijava.Type;

public class ArrayType extends Type {
	
	Type baseType;
	
	public ArrayType(Type base){
		this.baseType=base;
	}
	
	public boolean equals(Object array) {
		
		if(this.toString().equals(array.toString())){return true;}
		else{
		
			return false;}
	}
	
	public Type getBaseType(){
		return this.baseType;
	}
	
	public String toString() {	
		return ""+baseType+"[]";
	}
	public boolean primitive(){
		return false;
	}

	
}
