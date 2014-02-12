package com.javatracer.model.variables.data;

public class StringData extends Data{
	
	private String name;
	private String value;
	private long id;

	public StringData(String name, long id,String value) {
		this.name = name;
		this.id = id;
		this.value = value;
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
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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

	public void accept(InfoVisitor visitor) {
		visitor.visit(this);
	}

}
