package com.javatracer.model.methods.data;

import java.util.List;

import com.general.model.variables.data.Data;

	
public class MethodInfo {
	
	private String methodName;
	private String calledFromClass;
	private List<Data> arguments;
	private Data return_data;
	private Data this_data;

	public MethodInfo(String methodName, String calledFromClass, List<Data> arguments,Data this_data,Data return_data) {
		this.methodName = methodName;
		this.calledFromClass = calledFromClass;
		this.arguments = arguments;
		this.this_data = this_data;
		this.return_data = return_data;
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

	public List<Data> getArguments() {
		return arguments;
	}

	public Data getReturn_data() {
		return return_data;
	}

	public Data getThis_data() {
		return this_data;
	}

	public void setCalledFromClass(String calledFromClass) {
		this.calledFromClass = calledFromClass;
	}

	public void setArguments(List<Data> arguments) {
		this.arguments = arguments;
	}

	public void setReturn_data(Data return_data) {
		this.return_data = return_data;
	}

	public void setThis_data(Data this_data) {
		this.this_data = this_data;
	}
	
}
