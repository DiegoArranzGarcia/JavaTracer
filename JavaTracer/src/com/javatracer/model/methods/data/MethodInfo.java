package com.javatracer.model.methods.data;

import java.util.List;

import com.javatracer.model.variables.data.Data;

	
public class MethodInfo {
	String name;
	String calledFromClass;
	List<Data> arguments;
	Data return_data;
	Data this_data;

	public MethodInfo(String name, String calledFromClass, List<Data> arguments,Data this_data,Data return_data) {
		this.name = name;
		this.calledFromClass = calledFromClass;
		this.arguments = arguments;
		this.this_data = this_data;
		this.return_data = return_data;
	}
	
}
