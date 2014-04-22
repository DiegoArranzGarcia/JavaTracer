package com.general.model.data.variables;

public class SimpleData extends Data{
	
	private String name;
	private Object value;
	
	public SimpleData(String name,Object value){
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

	public void accept(InfoVisitor visitor) {	
		visitor.visit(this);
	}

}
