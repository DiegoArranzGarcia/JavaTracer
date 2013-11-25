package com.javatracer.model.info;

public class ArgumentInfo {

	String variable;
	Object value;
	
	public ArgumentInfo(String nameVar, Object value) {
		this.variable = nameVar;
		this.value = value;
	}
	
}
