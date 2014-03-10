package com.profiler.model.data;

import java.util.ArrayList;

public class ProfileClass extends ProfileData{
	
	private String className;
	private String completeClassName;

	public ProfileClass(String className) {
		this.className = className;
		this.children = new ArrayList<ProfileData>();
		this.numCalls = 0;
	}

	public String getName() {
		return className;
	}
	
	public void add(ProfileData data){
		super.add(data);
		String prefix = "";
		prefix = completeClassName + ".";
		data.setCompleteName(prefix + data.getName());
	}
	
	public String getCompleteName(){
		return completeClassName;
	}
	
	public void setCompleteName(String completeName) {
		this.completeClassName = completeName;
	}


	public void accept(ProfileDataVisitor visitor){
		visitor.visit(this);
	}
}
