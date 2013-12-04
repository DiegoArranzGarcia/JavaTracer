package com.traceinspector.datamodel;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.info.VariableInfo;

public class MethodNode{
	
	String methodName;
	List<VariableInfo> variables;
	private long id;
	
	public MethodNode(){
		this.variables = new ArrayList<>();
	}
	
	public MethodNode(String methodName,long id) {
		this.methodName = methodName;
		this.id = id;
		this.variables = new ArrayList<>();
	}

	public String getMethodName(){
		return this.methodName;
	}
	
	public List<VariableInfo> getVariables() {
		return variables;
	}
	
	public void setVariables(List<VariableInfo> variables) {
		this.variables = variables;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
