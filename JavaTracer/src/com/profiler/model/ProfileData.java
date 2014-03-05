package com.profiler.model;

import java.util.HashMap;

public class ProfileData {

	private HashMap<String,Integer> classesInfo;
	private HashMap<String,Boolean> checkedClasses;
	private int totalTimeCalledMethods;
	
	public ProfileData(){
		this.classesInfo = new HashMap<>();
		this.checkedClasses = new HashMap<>();
		this.totalTimeCalledMethods = 0;
	}

	public HashMap<String,Integer> getClassesInfo() {
		return classesInfo;
	}

	public void setClassesInfo(HashMap<String,Integer> classesInfo) {
		this.classesInfo = classesInfo;
	}

	public HashMap<String,Boolean> getCheckedClasses() {
		return checkedClasses;
	}

	public void setCheckedClasses(HashMap<String,Boolean> checkedClasses) {
		this.checkedClasses = checkedClasses;
	}

	public int getTotalTimeCalledMethods() {
		return totalTimeCalledMethods;
	}

	public void setTotalTimeCalledMethods(int totalTimeCalledMethods) {
		this.totalTimeCalledMethods = totalTimeCalledMethods;
	}
	
}
