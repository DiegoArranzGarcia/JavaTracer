package com.javatracer.model.info;

import java.util.List;

public class MethodInfo {
	
	String name;
	String calledFromClass;
	List<VariableInfo> arguments;
	List<VariableInfo> thisObject;
	List<String> changes;

	public MethodInfo(String name, String calledFromClass, List<VariableInfo> arguments,List<VariableInfo> thisObject) {
		this.name = name;
		this.calledFromClass = calledFromClass;
		this.arguments = arguments;
		this.thisObject = thisObject;
	}
	
}
