package com.general.model.variables.data;

public class IgnoredData extends Data{

	private String name;
	private String className;
	private Object value;
	
	public IgnoredData(String className,String name){
		this.name = name;
		this.className = className;
		this.value = "ignored";
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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
