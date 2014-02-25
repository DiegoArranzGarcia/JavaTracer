package com.javatracer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.general.model.FileUtilities;
import com.javatracer.model.Tracer;
import com.javatracer.model.finders.ClassFinder;
import com.javatracer.model.finders.JarFinder;
import com.javatracer.model.writers.TraceInspectorWriter;
import com.javatracer.view.JavaTracerView;
import com.profiler.controller.ProfilerController;

public class JavaTracerController {
	
	private JavaTracerView javaTracerView;
	private ProfilerController profilerController;
	
	private Tracer tracer;
	private TraceInspectorWriter traceInspectorWriter;
	
	private RunConfiguration lastConfig;
	
	private ClassFinder classFinder;
	private JarFinder jarFinder;
	
	public JavaTracerController(){
		 this.javaTracerView = new JavaTracerView(this);
		 this.classFinder = new ClassFinder();
		 this.jarFinder = new JarFinder();
		 this.tracer = new Tracer();
		 this.profilerController = new ProfilerController();
		 tracer.setController(this);
    }
	
	public void clickedOnTrace(){
				
		this.lastConfig = getAllConfig();		
		//boolean error = lastConfig.check();
		
		if (lastConfig.isProfiling_mode())
			tracer.profile(lastConfig,profilerController.getProfiler());
		else
			tracer.trace(lastConfig);				
	}
	
	
	private RunConfiguration getAllConfig() {
		 
		String mainClassPath = javaTracerView.getPath();
		
		boolean jar = checkIfJar();
		boolean profile_mode = javaTracerView.profileMode();
		String main;
		if (jar)
			main = javaTracerView.getPath();
		else
			main = javaTracerView.getMainClass();
		String classPath = "";
		if (!jar) 
			classPath = processPath(mainClassPath,main);
		String nameXml = getNameXml();
		String[] args = new String[0];
		String[] external_jars = new String[0];
		if (!jar) 
			external_jars = jarFinder.getJarDirectories(mainClassPath);
		 
		RunConfiguration config = new RunConfiguration(profile_mode,jar,main, classPath, nameXml, args, external_jars);
		
		return config;
		
	}

	private boolean checkIfJar() {
		String path = javaTracerView.getPath();
		File file = new File(path);
		return (file.isFile() && FileUtilities.getExtension(file).equals("jar"));
	}

	private String getNameXml() {
		String nameXml = javaTracerView.getNameXml();
		if (nameXml.equals(""))
			nameXml = "default";
		return nameXml;
	}

	
	private String processPath(String file, String name) {
		
		boolean equals=false;
		
		String path_file= giveMePathController(name);
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
			profilerController.showProfile();			
		} else {
			traceInspectorWriter = new TraceInspectorWriter(lastConfig.getNameXml());
			traceInspectorWriter.generateFinalTrace();
			javaTracerView.finishedTrace();
		}

	}
	
	public void selectedPath(String path){
		File file = new File(path);
		
		List<String> classes;
		if (FileUtilities.getExtension(file).equals("jar")){
			classes = new ArrayList<>();
			classes.add(file.getName());
		} else {
			classes = classFinder.getAllClasesFromFile(file);
			javaTracerView.enableMainClassCombo();
		}
	
		javaTracerView.loadClasses(classes);
	}
	
	
	public String giveMePathController(String className){
		return classFinder.giveMePath(className);
	}

}
