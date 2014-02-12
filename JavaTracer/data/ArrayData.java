package com.javatracer.model.variables.data;

import java.util.List;

public class ArrayData extends Data{
	
	private String name;
	private String className;
	private long id;
	private int length;
	private List<Data> value;

	public ArrayData(String name, long id,List<Data> value,int lenght,String className) {
		this.name = name;
		this.value = value;
		this.length = lenght;
		this.className = className;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
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

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the value
	 */
	public List<Data> getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(List<Data> value) {
		this.value = value;
	}

	public void accept(InfoVisitor visitor) {
		
		visitor.visit(this);
		
	}

}
