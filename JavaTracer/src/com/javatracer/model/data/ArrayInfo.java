package com.javatracer.model.data;

import java.util.List;

public class ArrayInfo {
	
	String className;
	List<Object> values;
	
	public ArrayInfo(String className,List<Object> values){
		this.className = className;
		this.values = values;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	
}
