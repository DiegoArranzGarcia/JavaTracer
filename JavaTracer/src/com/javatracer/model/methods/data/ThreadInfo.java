package com.javatracer.model.methods.data;

public class ThreadInfo {

	private String nameThread;
	
	public ThreadInfo(){
		this.nameThread = "main-thread";
	}

	public String getNameThread() {
		return nameThread;
	}

	public void setNameThread(String nameThread) {
		this.nameThread = nameThread;
	}

}
