package com.javatracer.model.data;

import java.util.List;


public class MethodExitInfo implements InterfaceInfo{

	String methodName;
	String className;
	Object returnObject;
	List<Object> ArgumentsThis;
	List<ArgumentInfo> arguments;
	
	public MethodExitInfo(String methodName, String className,Object returnObject,List<ArgumentInfo> arguments, 
			List<Object> Values_this) {
		this.methodName = methodName;
		this.className = className;
		this.returnObject = returnObject;
		this.ArgumentsThis=Values_this;
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
