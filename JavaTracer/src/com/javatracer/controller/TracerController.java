package com.javatracer.controller;

import com.javatracer.model.Tracer;
import com.javatracer.model.writers.TraceInspectorWriter;
import com.javatracer.view.WindowPath;

public class TracerController {
	
	Tracer tracer;
	TraceInspectorWriter traceInspectorWriter;
	WindowPath view;
	
	public TracerController(){
		 view = new WindowPath(this);
    }

	public void startTrace(String[] args) {
		tracer = new Tracer(this);
		tracer.trace(args);
	}

	public void showErrorMain() {
		view.showErrorMain();		
	}

	public void showErrorLoadClass() {
		view.showErrorLoadClass();
	}

	public void finishedTrace() {
		view.finishedTrace();
		this.traceInspectorWriter = new TraceInspectorWriter();
		traceInspectorWriter.generateFinalTrace();
	}
	
	

}
