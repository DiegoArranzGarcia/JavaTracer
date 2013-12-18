package com.javatracer.model.data;

import java.util.List;

public class MethodEntryInfo implements InterfaceInfo {

	String methodName;
	String calledFromClass;
	List<VariableInfo> arguments;
	VariableInfo objectThis;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<VariableInfo> arguments, VariableInfo objectThis){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = arguments;	
		this.objectThis= objectThis;
	}

	public String toString() {
		return "";
	}
	
}
