package com.javatracer.controller;

import com.javatracer.model.Tracer;
import com.javatracer.view.WindowPath;

public class TracerController {
	
	Tracer tracer;
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
	}
	
	

}
