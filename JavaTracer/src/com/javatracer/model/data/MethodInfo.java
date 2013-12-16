package com.javatracer.model.data;

import java.util.List;

public class MethodInfo {
	
	String name;
	String calledFromClass;
	List<VariableInfo> arguments;
	VariableInfo thisObject;
	List<String> changes;

	public MethodInfo(String name, String calledFromClass, List<VariableInfo> arguments,VariableInfo thisObject) {
		this.name = name;
		this.calledFromClass = calledFromClass;
		this.arguments = arguments;
		this.thisObject = thisObject;
	}
	
}
