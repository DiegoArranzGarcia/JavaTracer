package com.javatracer.model.data;

import java.util.List;


public class MethodExitInfo {

	String methodName;
	String className;
	Object returnObject;
	VariableInfo objectThis;
	List<VariableInfo> arguments;
	
	public MethodExitInfo(String methodName, String className,Object returnObject,List<VariableInfo> arguments, 
			VariableInfo objectThis) {
		this.methodName = methodName;
		this.className = className;
		this.returnObject = returnObject;
		this.objectThis=objectThis;
		this.arguments = arguments;
	}

}
