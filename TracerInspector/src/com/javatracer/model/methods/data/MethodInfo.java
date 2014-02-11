package com.javatracer.model.methods.data;

import java.util.List;

public class MethodInfo {
	
	String name;
	String calledFromClass;
	List<Object> arguments;
	Object thisObject;
	List<String> changes;

	public MethodInfo(String name, String calledFromClass, List<Object> arguments,Object thisObject) {
		this.name = name;
		this.calledFromClass = calledFromClass;
		this.arguments = arguments;
		this.thisObject = thisObject;
	}
	
}
