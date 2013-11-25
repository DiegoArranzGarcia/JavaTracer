package com.javatracer.model.info;

import java.util.List;

public class MethodEntryInfo implements InterfaceInfo {

	String methodName;
	String calledFromClass;
	List<Object> arguments;
	List<Object> argumentsThis;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<Object> value, List<Object> values_this){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = value;	
		this.argumentsThis=values_this;
	}

	public String toString() {
		return "";
	}
	
}
