package com.javatracer.model.variables.data;

import java.util.List;


public class ObjectData extends SimpleData{

	private long id;
	private String className;
	
	public ObjectData(String name,long id,List<Object> value,String className){
		super(name,value);
		this.setId(id);
		this.className = className;
	}
	
	public void accept(InfoVisitor visitor) {
		visitor.visit(this);
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

	
}
