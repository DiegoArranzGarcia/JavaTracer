package com.tracer.model.methods.data;

import java.util.List;

import com.general.model.variables.data.Data;

public class MethodEntryInfo {

	String methodName;
	String calledFromClass;
	List<Data> arguments;
	Data this_data;
	
	public MethodEntryInfo(String methodName, String calledFromClass, List<Data> arguments,Data this_data){
		this.methodName = methodName;
		this.calledFromClass = calledFromClass; 
		this.arguments = arguments;	
		this.this_data = this_data;
	}
	
}
