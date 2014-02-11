package com.javatracer.model.variables.data;

public interface InfoElement {
	
	public void accept(InfoVisitor visitor);
}
