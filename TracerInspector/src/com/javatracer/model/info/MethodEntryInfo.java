package com.javatracer.model.info;

import java.util.List;

public class MethodEntryInfo implements InterfaceInfo {

	String methodName;
	String calledFromClass;
	List<VariableInfo> arguments;
	List<VariableInfo> argumentsThis;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<VariableInfo> arguments, List<VariableInfo> argumentsThis){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = arguments;	
		this.argumentsThis= argumentsThis;
	}

	public String toString() {
		return "";
	}
	
}
