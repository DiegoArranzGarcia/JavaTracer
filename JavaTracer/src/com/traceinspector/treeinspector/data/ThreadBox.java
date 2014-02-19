package com.traceinspector.treeinspector.data;

public class ThreadBox extends Box{
	
	public ThreadBox(long id) {
		super(id,true);
	}

	public String getBoxText() {
		return "Thread";
	}
	
}
