package com.traceinspector.datamodel;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.info.ArgumentInfo;

public class MethodNode{
	
	String methodName;
	List<ArgumentInfo> variables;
	
	public MethodNode(){
		this.variables = new ArrayList<>();
	}
	
	public MethodNode(String methodName) {
		this.methodName = methodName;
		this.variables = new ArrayList<>();
	}

	public String getMethodName(){
		return this.methodName;
	}
	
	public List<ArgumentInfo> getVariables() {
		return variables;
	}
	
	public void setVariables(List<ArgumentInfo> variables) {
		this.variables = variables;
	}

}
