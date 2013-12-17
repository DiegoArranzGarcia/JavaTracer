package com.javatracer.model.data;

import java.util.List;

public class ArrayInfo {
	
	private String className;
	private int length;
	private List<Object> values;
	
	public ArrayInfo(String className,int length,List<Object> values){
		this.className = className;
		this.length = length;
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	
}
