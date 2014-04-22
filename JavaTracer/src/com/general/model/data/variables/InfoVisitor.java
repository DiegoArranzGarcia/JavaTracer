package com.general.model.data.variables;

public interface InfoVisitor {

	public void visit(ArrayData info);
	public void visit(StringData info);
	public void visit(NullData info);
	public void visit(ObjectData info);
	public void visit(SimpleData info);
	public void visit(IgnoredData ignoredClass);
	
}
