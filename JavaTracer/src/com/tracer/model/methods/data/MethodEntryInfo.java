package com.tracer.model.methods.data;

import java.util.List;

import com.general.model.variables.data.Data;

public class MethodEntryInfo {

	private String methodName;
	private String calledFromClass;
	private List<Data> arguments;
	private Data this_data;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<Data> arguments,Data this_data){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = arguments;	
		this.this_data = this_data;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getCalledFromClass() {
		return calledFromClass;
	}

	public void setCalledFromClass(String calledFromClass) {
		this.calledFromClass = calledFromClass;
	}

	public List<Data> getArguments() {
		return arguments;
	}

	public void setArguments(List<Data> arguments) {
		this.arguments = arguments;
	}

	public Data getThis_data() {
		return this_data;
	}

	public void setThis_data(Data this_data) {
		this.this_data = this_data;
	}
	
}
