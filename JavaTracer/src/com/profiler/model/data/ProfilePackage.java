package com.profiler.model.data;

import java.util.ArrayList;

public class ProfilePackage extends ProfileData{

	private String packageName;
	private String completePackageName;
	
	public ProfilePackage(String packageName) {
		this.packageName = packageName;
		this.children = new ArrayList<ProfileData>();
		this.numCalls = 0;
	}

	public String getName() {
		return packageName;
	}
	
	public void add(ProfileData data){
		super.add(data);
		if (parent != null){
			if (parent.isRoot())
				completePackageName = packageName;
			else 
				completePackageName = ((ProfilePackage)parent).getCompletePackageName() + "." + packageName;
		} else {
			completePackageName = "";
		}
		
	}

	public String getCompletePackageName() {
		return completePackageName;
	}

	public void setCompletePackageName(String completePackageName){
		this.completePackageName = completePackageName;
	}

	public void accept(ProfileDataVisitor visitor) {
		visitor.visit(this);
	}
}
