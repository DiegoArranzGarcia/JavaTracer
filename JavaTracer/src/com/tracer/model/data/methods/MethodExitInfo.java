package com.tracer.model.data.methods;

import java.util.List;

import com.general.model.data.variables.Data;

public class MethodExitInfo {

	private String methodName;
	private String className;
	private Data return_data;
	private Data this_data;
	private List<Data> arguments;
	
	public MethodExitInfo(String methodName, String className,Data return_data,List<Data> arguments,Data this_data) {
		this.methodName = methodName;
		this.className = className;
		this.return_data = return_data;
		this.this_data = this_data;
		this.arguments = arguments;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Data getReturnData() {
		return return_data;
	}

	public void setReturn_data(Data return_data) {
		this.return_data = return_data;
	}

	public Data getThis_data() {
		return this_data;
	}

	public void setThis_data(Data this_data) {
		this.this_data = this_data;
	}

	public List<Data> getArguments() {
		return arguments;
	}

	public void setArguments(List<Data> arguments) {
		this.arguments = arguments;
	}

}
