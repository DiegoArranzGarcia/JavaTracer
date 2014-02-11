package com.javatracer.model.variables.data;

public class StringData extends SimpleData{
	
	private long id;

	public StringData(String name, long id,String value) {
		super(name, value);
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
