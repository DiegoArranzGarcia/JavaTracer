package com.profiler.model.data;

import java.util.ArrayList;

public class ProfilePackage extends ProfileData{

	private String packageName;
	private String completePackageName;
	
	public ProfilePackage(String packageName) {
		this.packageName = packageName;
		this.children = new ArrayList<ProfileData>();
		this.excluded = false;
		this.numCalls = 0;
	}

	public String getName() {
		return packageName;
	}
	
	public void add(ProfileData data){
		super.add(data);
		String prefix = "";
		if (!isRoot())
			prefix = completePackageName + ".";
		data.setCompleteName(prefix + data.getName());
	}

	public String getCompleteName() {
		return completePackageName;
	}

	public void accept(ProfileDataVisitor visitor) {
		visitor.visit(this);
	}

	public void setCompleteName(String completeName) {
		this.completePackageName = completeName;
	}

}
