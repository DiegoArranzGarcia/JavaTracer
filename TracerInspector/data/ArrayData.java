package com.javatracer.model.variables.data;

import java.util.List;

public class ArrayData extends SimpleData{
	
	private String className;
	private long id;
	private int length;

	public ArrayData(String name, long id,List<Object> value,int lenght,String className) {
		super(name, value);
		this.length = lenght;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

}
