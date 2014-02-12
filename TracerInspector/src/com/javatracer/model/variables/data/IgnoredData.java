package com.javatracer.model.variables.data;

public class IgnoredData extends Data{

	private String name;
	private Object value;
	
	public IgnoredData(String name){
		this.name = name;
		this.value = "ignored";
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
