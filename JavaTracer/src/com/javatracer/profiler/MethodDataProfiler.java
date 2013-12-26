package com.javatracer.profiler;

public class MethodDataProfiler {
	
	private String className;
	private String method;
	private int numCalls;
	
	public MethodDataProfiler(String className,String method){
		this.className = className;
		this.method = method;
		this.numCalls = 1;
	}
	
	public void methodCalled(){
		this.numCalls++;
	}

	public String getNameClass() {
		return className;
	}

	public void setNameClass(String nameClass) {
		this.className = nameClass;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getNumCalls() {
		return numCalls;
	}

	public void setNumCalls(int numCalls) {
		this.numCalls = numCalls;
	}

}
