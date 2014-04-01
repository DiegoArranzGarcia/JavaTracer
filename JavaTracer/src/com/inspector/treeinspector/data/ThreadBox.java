package com.inspector.treeinspector.data;

public class ThreadBox extends Box{
	
	String threadName;
	
	public ThreadBox(String path,long id, String threadName) {
		super(path,id,true);
		this.threadName = threadName;
	}

	public String getBoxText() {
		return "Thread: " + threadName;
	}
	
}
