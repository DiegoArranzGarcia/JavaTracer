package com.tracer.model.methods.data;

import java.util.List;

import com.general.model.variables.data.Data;

public class MethodExitInfo {

	String methodName;
	String className;
	Data return_data;
	Data this_data;
	List<Data> arguments;
	
	public MethodExitInfo(String methodName, String className,Data return_data,List<Data> arguments,Data this_data) {
		this.methodName = methodName;
		this.className = className;
		this.return_data = return_data;
		this.this_data = this_data;
		this.arguments = arguments;
	}

}
