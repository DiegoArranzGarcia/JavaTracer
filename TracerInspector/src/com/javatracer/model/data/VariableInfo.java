package com.javatracer.model.data;

public class VariableInfo {

	String name;
	Object value;
	
	public VariableInfo(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public String CompleteArgumentString() {
				
				String name="";
				if (value instanceof ArrayInfo)
					name=((ArrayInfo)value).className +"[ ]"+" "+this.name + ", " ;
				else
					if (value instanceof ObjectInfo)
							name=((ObjectInfo)value).getClassName() +" "+this.name + ", " ;
						 else 
							 if (value instanceof NullObject)
									name= "null"+" "+this.name + ", " ;
							 else
								 if (value instanceof String)
									       name= value.toString() +" "+this.name + ", " ;
								 else
									 name= value.toString() +" "+this.name + ", " ;
					
				
				
				return name;
			}

	
}
