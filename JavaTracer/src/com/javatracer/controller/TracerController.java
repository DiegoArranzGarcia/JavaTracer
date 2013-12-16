package com.javatracer.controller;

import java.util.List;

import com.javatracer.model.Tracer;
import com.javatracer.model.classfinder.ClassFinder;
import com.javatracer.model.writers.TraceInspectorWriter;
import com.javatracer.view.WindowPath;

public class TracerController {
	
	Tracer tracer;
	TraceInspectorWriter traceInspectorWriter;
	WindowPath view;
	ClassFinder finder = new ClassFinder();
	
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
		
		if(!tracer.getWasError()){
			view.finishedTrace();
			this.traceInspectorWriter = new TraceInspectorWriter();
			traceInspectorWriter.generateFinalTrace();
		}
	}

	public void loadClassFromPath(String path) {
		List<String> classes = finder.getAllClasesFromPath(path);
		view.loadClasses(classes);
	}
	
	
	public String giveMePathController(String key){
		
		return finder.giveMePath(key);
		
	}

}
