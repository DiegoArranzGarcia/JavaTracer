package com.general.model.data.variables;

import java.util.ArrayList;
import java.util.List;


public class ObjectData extends Data{

	private String name;
	private List<Data> constantData;
	private List<Data> inheritData;
	private List<Data> fields;
	private long id;
	private String className;
	
	public ObjectData(String name,long id,List<Data> constantData,List<Data> inheritData,List<Data> fields,String className){
		this.name = name;
		this.constantData = constantData;
		this.inheritData = inheritData;
		this.fields = fields;
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
		List<Data> value = new ArrayList<Data>();
		if (constantData != null)
			value.addAll(constantData);
		if (inheritData != null)
			value.addAll(inheritData);
		if (fields != null)
			value.addAll(fields);
		return value;
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

	public List<Data> getConstantData() {
		return constantData;
	}

	public List<Data> getInheritData() {
		return inheritData;
	}

	public List<Data> getFields() {
		return fields;
	}

	public void setConstantData(List<Data> constantData) {
		this.constantData = constantData;
	}

	public void setInheritData(List<Data> inheritData) {
		this.inheritData = inheritData;
	}

	public void setFields(List<Data> fields) {
		this.fields = fields;
	}

	
}
