package com.tracer.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.general.model.ClassFinder;
import com.general.model.FileUtilities;
import com.general.model.JarFinder;
import com.general.presenter.JavaTracerPresenter;
import com.tracer.arguments.presenter.ArgumentsPresenter;
import com.tracer.console.presenter.ConsolePresenter;
import com.tracer.model.RunConfiguration;
import com.tracer.model.Tracer;
import com.tracer.view.TracerView;
import com.tracer.view.TracerViewInterface;

public class TracerPresenter implements TracerPresenterInterface {
	
	private TracerViewInterface tracerView;
	private JavaTracerPresenter javaTracerPresenter;
	private ArgumentsPresenter argumentsPresenter;
	private ConsolePresenter consolePresenter;
	
	private Tracer tracer;
	private RunConfiguration lastConfig;
	
	private ClassFinder classFinder;
	private JarFinder jarFinder;
	
	public TracerPresenter(){
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
		consolePresenter.resetConsole();
		consolePresenter.play();
		tracer.trace(lastConfig);		
	}
	
	public void clickedOnProfile(){
		this.lastConfig = getAllConfig(true);		
		consolePresenter.resetConsole();
		consolePresenter.play();
		tracer.profile(lastConfig,javaTracerPresenter.getProfiler());	
	}
	
	private RunConfiguration getAllConfig(boolean profileMode) {
		 
		String mainClassPath = tracerView.getPath();
		
		boolean jar = checkIfJar();
		boolean profile_mode = profileMode;
		String main;
		if (jar){
			main = tracerView.getPath();
			File file= new File(main);
			if(!FileUtilities.getExtension(file).equals("jar"))
				main=classFinder.getPath(tracerView.getMainClass());
		
		}else
			main = tracerView.getMainClass();
		String classPath = "";
		if (!jar) 
			classPath = processPath(mainClassPath,main);
		String nameXml = tracerView.getNameXml(!profile_mode);
		String[] args = argumentsPresenter.getArguments();
		String[] external_jars = new String[0];
		if (!jar){
			external_jars = jarFinder.getJars(mainClassPath);
		}
		RunConfiguration config = new RunConfiguration(profile_mode,jar,main, classPath, nameXml, args, external_jars);
		
		return config;
		
	}
	
	private boolean checkIfJar() {
		String path = tracerView.getPath();
		String pathJar=classFinder.getPath(tracerView.getMainClass());
		File file = new File(path);
		File fileJar = new File(pathJar);
		
		return (file.isFile() && FileUtilities.isExtension(file,"jar"))||(fileJar.isFile() && FileUtilities.isExtension(fileJar,"jar"));
	}
	
	private String processPath(String file, String name) {
		String path_file = classFinder.getPath(name);
		file = path_file.substring(0, path_file.lastIndexOf(FileUtilities.SEPARATOR));	
		String[] split = name.split("\\.");
	
		for (int index = split.length-2;index >= 0;index--){
			file = file.substring(0,file.indexOf(FileUtilities.SEPARATOR+split[index]));
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
			classes = classFinder.getAllClassesFromFile(file);
			tracerView.enableMainClassCombo();
		}
	
		tracerView.loadClasses(classes);
	}

	public void clickedExit() {
		System.exit(0);	
	}

	public void editArguments() {
		//tracerView.setVisible(false); 
		argumentsPresenter.show();
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
		//tracerView.setVisible(false);
		javaTracerPresenter.clickedOnSettings();
	}
	
	
	private void setControllers() {
		tracer.setPresenter(this);
		argumentsPresenter.setController(this);
		consolePresenter.setController(this);
	}

	public void setPresenter(JavaTracerPresenter javaTracerPresenter) {
		this.javaTracerPresenter = javaTracerPresenter; 
	}

	public void clickedOnLoadProfile() {
		javaTracerPresenter.clickedOnLoadProfile(tracerView.getView());
	}

	public void clickedOnLoadTrace() {
		javaTracerPresenter.clickedOnLoadTrace(tracerView.getView());
	}

	public void launching() {
		consolePresenter.setLastFileName(lastConfig.getNameXml());
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
			javaTracerPresenter.showProfile();	
		} else {
			tracerView.finishedTrace(lastConfig.getNameXml());
		}
		tracerView.setEnableProfileAndTracer(true);
	}

	public void open(String xmlPath) {
		javaTracerPresenter.openTrace(xmlPath);
	}

	public void setVisible(boolean b) {
		tracerView.setVisible(b);
	}

	public void clickedOnAbout() {
		javaTracerPresenter.clickedOnAbout();
	}

	public String getXmlName() {
		return lastConfig.getNameXml();
	}

	public JFrame getView() {
		return tracerView.getView();
	}

	public void minimize() {
		tracerView.consoleMinimize();
	}

	public void maximize() {
		tracerView.consoleMaximize();
	}

	public boolean isExecutableJar(File file){
		return classFinder.ExecutableJar(file);
	}

	public void clickedOnStop() {
		tracer.stopTrace();
	}
}
