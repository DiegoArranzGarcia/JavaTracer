package com.general.model.data.variables;

public class NullData extends Data {

	private String name;
	
	public NullData(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return "null";
	}

	public void accept(InfoVisitor visitor) {
		visitor.visit(this);
	}
	
}
