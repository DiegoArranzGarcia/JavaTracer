package com.profiler.model.data;

import java.util.List;


public abstract class ProfileData {

	protected List<ProfileData> children;
	protected ProfileData parent;
	protected int numCalls;
	protected boolean checked;
	
	public abstract String getName();
	public abstract String getCompleteName();
	public abstract void setCompleteName(String completeName);
	public abstract void accept(ProfileDataVisitor visitor);
	
	public List<ProfileData> getChildren(){
		return children;
	}
	
	public boolean isRoot(){
		return (parent == null);
	}

	public void add(ProfileData data) {
		children.add(data);
		data.setParent(this);
	}

	private void setParent(ProfileData profileData) {
		this.parent = profileData;
	}

	
	public void remove(){
		
		List<ProfileData> children=parent.getChildren();
		if(children.remove(this))
			parent.setChildren(children);
		}

	public ProfileData getChild(int packageIndex) {
		return children.get(packageIndex);
	}

	public int getNumChildren() {
		return children.size();
	}
	
	public void removeAllChildren(){
		children.clear();
		numCalls = 0;
	}
	
	public void increment(){
		numCalls++;
		if (parent != null)
			parent.increment();
	}

	public int getNumCalls() {
		return numCalls;
	}

	
	public ProfileData getParent(){
		return parent;
		
		
	}
	
	private void setChildren(List<ProfileData> children){
		this.children=children;
		
	}

	
	
}



