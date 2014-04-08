package com.profiler.model.data;

import java.util.ArrayList;

public class ProfileMethod extends ProfileData{
	
	private String methodName;
	private String completeMethodName;

	public ProfileMethod(String methodName){
		this.methodName = methodName;
		this.children = new ArrayList<ProfileData>();
		this.excluded = false;
		this.numCalls = 0;
	}
	
	public String getName() {
		return methodName;
	}

	public void accept(ProfileDataVisitor visitor) {
		visitor.visit(this);
	}

	public void setCompleteMethodName(String completeMethodName) {
		this.completeMethodName = completeMethodName;
	}

	public String getCompleteName() {
		return completeMethodName;
	}

	public void setCompleteName(String completeName) {
		completeMethodName = completeName;
	}

	public String getParentCompleteName() {
		if (parent == null)
			return "";
		else 
			return parent.getCompleteName();
	}
	
	

}
