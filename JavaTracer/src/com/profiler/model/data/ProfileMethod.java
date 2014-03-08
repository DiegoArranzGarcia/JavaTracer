package com.profiler.model.data;

import java.util.ArrayList;

public class ProfileMethod extends ProfileData{
	
	private String methodName;

	public ProfileMethod(String methodName){
		this.methodName = methodName;
		this.children = new ArrayList<ProfileData>();
		this.numCalls = 0;
	}
	
	public String getName() {
		return methodName;
	}

	public void accept(ProfileDataVisitor visitor) {
		visitor.visit(this);
	}
	
	

}
