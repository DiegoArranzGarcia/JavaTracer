package com.javatracer.model.data;

import java.util.List;

public class ObjectInfo {

	private String className;
	private List<VariableInfo> fields;
	private long id;
	
	public ObjectInfo(String className,List<VariableInfo> fields,long id){
		this.className = className;
		this.fields = fields;
		this.id = id;
	}

	public ObjectInfo(String className, long id) {
		this.className = className;
		this.id = id;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the fields
	 */
	public List<VariableInfo> getFields() {
		return fields;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<VariableInfo>  fields) {
		this.fields = fields;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
}
