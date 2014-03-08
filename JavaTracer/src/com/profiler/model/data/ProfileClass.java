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
		if (parent != null){
			if (parent.isRoot())
				completeClassName = className;
			else 
				completeClassName = ((ProfilePackage)parent).getCompletePackageName() + "." + className;
		} else {
			completeClassName = "";
		}
	}
	
	public String getCompleteClassName(){
		return completeClassName;
	}

	public void accept(ProfileDataVisitor visitor){
		visitor.visit(this);
	}

}
