package com.profiler.model.data;

public interface ProfileDataVisitor {

	public void visit(ProfileClass data);
	public void visit(ProfilePackage data);
	public void visit(ProfileMethod data);
	
}
