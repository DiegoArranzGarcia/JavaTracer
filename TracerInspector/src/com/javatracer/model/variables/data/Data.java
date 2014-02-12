package com.javatracer.model.variables.data;

public abstract class Data {
	
	public abstract Object getValue();
	public abstract String getName();
	public abstract void accept(InfoVisitor visitor);
	
	public void visit(InfoVisitor visitor){
		
		if (this instanceof ArrayData)
			visitor.visit((ArrayData)this);
		else if (this instanceof IgnoredData)
			visitor.visit((IgnoredData)this);
		else if (this instanceof NullData)
			visitor.visit((NullData)this);
		else if (this instanceof ObjectData)
			visitor.visit((ObjectData)this);
		else if (this instanceof SimpleData)
			visitor.visit((SimpleData)this);
		else if (this instanceof StringData)
			visitor.visit((StringData)this);
	}
	
}
