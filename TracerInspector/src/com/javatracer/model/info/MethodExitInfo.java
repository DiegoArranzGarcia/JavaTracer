package com.javatracer.model.info;

import java.util.List;


public class MethodExitInfo implements InterfaceInfo{

	String methodName;
	String className;
	Object returnObject;
	List<VariableInfo> argumentsThis;
	List<VariableInfo> arguments;
	
	public MethodExitInfo(String methodName, String className,Object returnObject,List<VariableInfo> arguments, 
			List<VariableInfo> argumentsThis) {
		this.methodName = methodName;
		this.className = className;
		this.returnObject = returnObject;
		this.argumentsThis=argumentsThis;
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
