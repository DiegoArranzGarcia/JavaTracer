package com.javatracer.model.data;

public class StringInfo {

	private long id;
	private String value;
	
	public StringInfo(long id, String value) {
		this.id = id;
		this.value = value;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
