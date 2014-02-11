package com.javatracer.model.methods.data;

import java.util.List;

public class MethodEntryInfo {

	String methodName;
	String calledFromClass;
	List<Object> arguments;
	Object objectThis;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<Object> arguments, Object argument_this){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = arguments;	
		this.objectThis= argument_this;
	}
	
}
