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
	
	public boolean equals(Object obj) {
		
		if (obj instanceof ClassMethods)
			return className.equals(((ClassMethods)obj).getClassName());
		else 
			return super.equals(obj); 
	}
	
	public String getClassName(){
		return className;
	}
	
	public List<String>getExcludesMethods(){
		return excludesMethods;
	}

	public void addMethod(String methodName) {
		excludesMethods.add(methodName);
	}

}
