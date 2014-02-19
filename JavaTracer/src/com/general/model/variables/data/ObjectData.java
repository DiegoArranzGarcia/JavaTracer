package com.general.model.variables.data;

import java.util.List;


public class ObjectData extends Data{

	private String name;
	private List<Data> value;
	private long id;
	private String className;
	
	public ObjectData(String name,long id,List<Data> value,String className){
		this.name = name;
		this.value = value;
		this.id = id;
		this.className = className;
	}
	
	public void accept(InfoVisitor visitor) {
		visitor.visit(this);		
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
