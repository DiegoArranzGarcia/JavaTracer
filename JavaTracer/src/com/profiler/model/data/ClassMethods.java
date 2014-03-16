package com.profiler.model.data;

import java.util.ArrayList;
import java.util.List;

public class ClassMethods {

	private String className;
	private List<String> excludesMethods;
	
	public ClassMethods(String className) {
		this.className = className;
		this.excludesMethods = new ArrayList<String>();
	}
	
	public String getClassName(){
		return className;
	}
	
	public List<String>getExcludesMethods(){
		return excludesMethods;
	}

	public void addMethod(String methodName) {
		if (!excludesMethods.contains(methodName))
			excludesMethods.add(methodName);
	}

	public boolean isExcluded(String methodName) {
		return excludesMethods.contains(methodName);
	}

	public void removeMethod(String methodName) {
		excludesMethods.remove(methodName);
	}

	public boolean hasMethods() {
		return !excludesMethods.isEmpty();
	}

}
