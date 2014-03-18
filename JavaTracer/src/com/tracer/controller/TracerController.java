package com.tracer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.general.model.ClassFinder;
import com.general.model.FileUtilities;
import com.general.model.JarFinder;
import com.general.presenter.JavaTracerPresenter;
import com.tracer.arguments.presenter.ArgumentsPresenter;
import com.tracer.console.presenter.ConsolePresenter;
import com.tracer.model.Tracer;
import com.tracer.view.TracerView;

public class TracerController {
	
	private TracerView tracerView;
	private JavaTracerPresenter presenter;
	private ArgumentsPresenter argumentsPresenter;
	private ConsolePresenter consolePresenter;
	
	private Tracer tracer;
	private RunConfiguration lastConfig;
	
	private ClassFinder classFinder;
	private JarFinder jarFinder;
	
	public TracerController(){
		tracer = new Tracer();
		jarFinder = new JarFinder();
		classFinder = new ClassFinder();
		argumentsPresenter = new ArgumentsPresenter();
		consolePresenter = new ConsolePresenter();
		setControllers();
    }
	
	public void open() {
		if (tracerView == null){
			tracerView = new TracerView(consolePresenter.getConsole());
			tracerView.setController(this);
		}
		tracerView.setVisible(true);
	}
	
	public void clickedOnTrace(){
		this.lastConfig = getAllConfig(false);
		tracerView.showConsole();
		consolePresenter.resetConsole();
		tracer.trace(lastConfig);		
	}
	
	public void clickedOnProfile(){
		this.lastConfig = getAllConfig(true);		
		tracerView.showConsole();
		consolePresenter.resetConsole();
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

	public String getNameXml() {
		return tracerView.getNameXml();
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

	public void clickedExit() {
		System.exit(0);	
	}

	public void editArguments() {
		tracerView.setVisible(false); 
		presenter.clickedOnEditArguments();
	}

	public void redirectStreams(Process process){
		consolePresenter.redirectStreams(process);
	}

	public void closeStreams() {
		consolePresenter.closeStreams();
	}
	
	public void enableProfileAndTrace(){
		tracerView.setEnableProfileAndTracer(true);
	}
	
	public void disableProfileAndTracer(){
		tracerView.setEnableProfileAndTracer(false);
	}

	public void clickOnSettings() {
		tracerView.setVisible(false);
		presenter.clickedOnSettings();
	}
	
	
	private void setControllers() {
		tracer.setController(this);
	}

	public void setPresenter(JavaTracerPresenter javaTracerPresenter) {
		this.presenter = javaTracerPresenter; 
	}

	public void clickedOnLoadProfile() {
		presenter.clickedOnLoadProfile();
	}

	public void clickedOnLoadTrace() {
		presenter.clickedOnInspectTrace();
	}

	public void launching() {
		consolePresenter.launching();
	}

	public void started() {
		if (lastConfig.isProfiling_mode())
			consolePresenter.profiling();
		else
			consolePresenter.tracing();
	}
	public void finishedTrace() {
		
		consolePresenter.closeStreams();
		consolePresenter.finished();
		if (lastConfig.isProfiling_mode()){
			presenter.showProfile();	
		} else {
			tracerView.finishedTrace();
		}
		tracerView.setEnableProfileAndTracer(true);
	}

	public void open(String xmlPath) {
		presenter.openTrace(xmlPath);
	}

	public void setVisible(boolean b) {
		tracerView.setVisible(b);
	}
}
