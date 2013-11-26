package com.javatracer.model.data;

import java.util.List;

public class MethodInfo {
	
	String name;
	String calledFromClass;
	List<ArgumentInfo> arguments;
	List<Object> thisObject;
	List<String> changes;

	public MethodInfo(String name, String calledFromClass) {
		this.name = name;
		this.calledFromClass = calledFromClass;
	}
	
}
