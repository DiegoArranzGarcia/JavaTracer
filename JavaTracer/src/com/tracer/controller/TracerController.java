package com.tracer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.general.model.ClassFinder;
import com.general.model.FileUtilities;
import com.general.model.JarFinder;
import com.general.presenter.JavaTracerPresenter;
import com.tracer.arguments.presenter.ArgumentsPresenter;
import com.tracer.model.Tracer;
import com.tracer.model.writers.InspectorWriter;
import com.tracer.view.TracerView;

public class TracerController {
	
	private TracerView tracerView;
	private JavaTracerPresenter presenter;
	private ArgumentsPresenter argumentsPresenter;
	
	private Tracer tracer;
	private RunConfiguration lastConfig;
	
	private ClassFinder classFinder;
	private JarFinder jarFinder;
	
	public TracerController(){
		tracer = new Tracer();
		jarFinder = new JarFinder();
		classFinder = new ClassFinder();
		argumentsPresenter = new ArgumentsPresenter();
		setControllers();
    }
	
	private void setControllers() {
		tracer.setController(this);
	}

	public void setPresenter(JavaTracerPresenter javaTracerPresenter) {
		this.presenter = javaTracerPresenter; 
	}
	
	public void open() {
		if (tracerView == null){
			tracerView = new TracerView();
			tracerView.setController(this);
		}
		tracerView.setVisible(true);
	}
	
	public void clickedOnTrace(){
				
		this.lastConfig = getAllConfig(false);		
		tracer.trace(lastConfig);				
	}
	
	public void clickedOnProfiling(){
		
		this.lastConfig = getAllConfig(true);		
		tracer.profile(lastConfig,presenter.getProfiler());
			
	}
	
	
	private RunConfiguration getAllConfig(boolean profileMode) {
		 
		String mainClassPath = tracerView.getPath();
		
		boolean jar = checkIfJar();
		boolean profile_mode = profileMode;
		String main;
		if (jar)
			main = tracerView.getPath();
		else
			main = tracerView.getMainClass();
		String classPath = "";
		if (!jar) 
			classPath = processPath(mainClassPath,main);
		String nameXml = getNameXml();
		String[] args = argumentsPresenter.getArguments();
		String[] external_jars = new String[0];
		if (!jar)
			external_jars = jarFinder.getJarDirectories(mainClassPath);
		RunConfiguration config = new RunConfiguration(profile_mode,jar,main, classPath, nameXml, args, external_jars);
		
		return config;
		
	}

	private boolean checkIfJar() {
		String path = tracerView.getPath();
		File file = new File(path);
		return (file.isFile() && FileUtilities.isExtension(file,"jar"));
	}

	private String getNameXml() {
		String nameXml = tracerView.getNameXml();
		
		return nameXml;
	}

	
	private String processPath(String file, String name) {
		
		boolean equals=false;
		String path_file= classFinder.getPath(name);
		file=path_file.substring(0, path_file.lastIndexOf("\\"));
		
		path_file=file.replaceAll("\\\\", ".");
			
		while(!equals){
		    	
			if(name.contains(path_file.substring(path_file.lastIndexOf(".")+1,path_file.length())))
				file=file.substring(0, file.lastIndexOf("\\"));
			else 
				equals=true;
	
			path_file=path_file.substring(0, path_file.lastIndexOf("."));
	
		}		
		
		return file;
	}
	
	
	public void finishedTrace() {
		
		if (lastConfig.isProfiling_mode()){
			presenter.showProfile();			
		} else {
			InspectorWriter traceInspectorWriter = new InspectorWriter(lastConfig.getNameXml());
			traceInspectorWriter.generateFinalTrace();
			tracerView.finishedTrace();
		}

	}
	
	public void selectedPath(String path){
		File file = new File(path);
		List<String> classes;
		if (FileUtilities.isExtension(file,"jar")){
			classes = new ArrayList<>();
			classes.add(file.getName());
		} else {
			classes = classFinder.getAllClasesFromFile(file);
			tracerView.enableMainClassCombo();
		}
	
		tracerView.loadClasses(classes);
	}

	public void starting() {
		
	}

	public void generatingTrace() {
				
	}

	public void clickedBack() {
		tracerView.setVisible(false);	
		presenter.back();		
	}

	public void clickedEditArguments() {
		argumentsPresenter.show();
	}
	
}
