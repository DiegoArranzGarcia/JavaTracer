package com.javatracer.model.methods.data;

import java.util.ArrayList;
import java.util.List;


public class MethodExitInfo {

	String methodName;
	String className;
	Object returnObject;
	Object objectThis;
	List<Object> arguments;
	
	public MethodExitInfo(String methodName, String className,Object returnObject,List<Object> arguments, Object objectThis) {
		this.methodName = methodName;
		this.className = className;
		this.returnObject = returnObject;
		this.objectThis=objectThis;
		this.arguments = new ArrayList<>();
	}

}
