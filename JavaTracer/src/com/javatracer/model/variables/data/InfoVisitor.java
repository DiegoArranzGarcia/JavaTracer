package com.javatracer.model.variables.data;

public interface InfoVisitor {

	public void visit(ArrayData info);
	public void visit(StringData info);
	public void visit(NullData info);
	public void visit(ObjectData info);
	public void visit(Object value);
	public void visit(IgnoredData ignoredClass);
	
}
