package com.javatracer.model.data;

import java.util.List;

public class MethodEntryInfo implements InterfaceInfo {

	String methodName;
	String calledFromClass;
	List<ArgumentInfo> arguments;
	List<Object> argumentsThis;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<ArgumentInfo> arguments, List<Object> values_this){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = arguments;	
		this.argumentsThis= values_this;
	}

	public String toString() {
		return "";
	}
	
}
