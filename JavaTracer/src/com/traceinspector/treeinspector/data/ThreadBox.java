package com.traceinspector.treeinspector.data;

public class ThreadBox extends Box{
	
	String threadName;
	
	public ThreadBox(long id, String threadName) {
		super(id,true);
	}

	public String getBoxText() {
		return "Thread: " + threadName;
	}
	
}
