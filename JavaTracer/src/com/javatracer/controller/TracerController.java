package com.javatracer.controller;

import java.util.List;

import com.javatracer.model.ClassFinder;
import com.javatracer.model.Tracer;
import com.javatracer.model.writers.TraceInspectorWriter;
import com.javatracer.view.WindowPath;

public class TracerController {
	
	private WindowPath view;
	
	private Tracer tracer;
	private TraceInspectorWriter traceInspectorWriter;
	private ClassFinder finder = new ClassFinder();
	
	public TracerController(){
		 view = new WindowPath(this);
    }

	public void startTrace(String[] args, String nameXlm) {
		tracer = new Tracer(this);
		tracer.trace(args,nameXlm);
	}

	public void showErrorMain() {
		view.showErrorMain();		
	}

	public void showErrorLoadClass() {
		view.showErrorLoadClass();
	}

	public void finishedTrace(String nameXlm) {
		
		if(!tracer.getWasError()){
			view.finishedTrace();
			this.traceInspectorWriter = new TraceInspectorWriter(nameXlm);
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
