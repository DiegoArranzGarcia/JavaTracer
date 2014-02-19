package com.traceinspector.treeinspector.data;

public class ExceptionBox extends Box{

	public ExceptionBox(long id) {
		super(id,false);
	}
	
	public String getBoxText() {
		return "Exception";
	}
	
}
