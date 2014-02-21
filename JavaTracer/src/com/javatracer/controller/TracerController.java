package com.javatracer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.javatracer.model.Tracer;
import com.javatracer.model.finders.ClassFinder;
import com.javatracer.model.finders.JarFinder;
import com.javatracer.model.writers.TraceInspectorWriter;
import com.javatracer.view.Message;
import com.javatracer.view.JavaTracerView;

public class TracerController {
	
	private JavaTracerView view;
	
	private Tracer tracer;
	private TraceInspectorWriter traceInspectorWriter;
	private ClassFinder classFinder;
	private JarFinder jarFinder;
	
	public TracerController(){
		 this.view = new JavaTracerView(this);
		 this.classFinder = new ClassFinder();
		 this.jarFinder = new JarFinder();
    }
	
	public void clickedOnTrace(){
				
		RunConfiguration config = getAllConfig();		
		boolean error = config.check();
		
		if (!error){
			tracer = new Tracer(this);
			tracer.trace(config);
		}
		
	}
	
	private RunConfiguration getAllConfig() {
		 
		String mainClassPath = view.getPath();
		
		boolean jar = checkIfJar();
		String main;
		if (jar)
			main = view.getPath();
		else
			main = view.getMainClass();
		String classPath = "";
		if (!jar) 
			classPath = processPath(mainClassPath,main);
		String nameXml = getNameXml();
		String[] args = new String[0];
		String[] external_jars = new String[0];
		if (!jar) 
			external_jars = jarFinder.getJarDirectories(mainClassPath);
		 
		RunConfiguration config = new RunConfiguration(jar, main, classPath, nameXml, args, external_jars);
		
		return config;
		
	}

	private boolean checkIfJar() {
		String path = view.getPath();
		File file = new File(path);
		return (file.isFile() && classFinder.getExtension(file).equals("jar"));
	}

	private String getNameXml() {
		String nameXml = view.getNameXml();
		if (nameXml.equals(""))
			nameXml = "default";
		return nameXml;
	}

	private boolean checkErrors(String[] args, String nameXml) { 
		
		boolean error = false;
		
		if (nameXml.contains(".xml") || nameXml.contains(".XML")){
			view.errorNameXml();
		} else {
			if(existFileXml(nameXml+".xml")){
					
				int seleccion = JOptionPane.showOptionDialog(null,nameXml+new Message(7).getMessage(),new Message(8).getMessage(), 
						    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				
				error = (seleccion!=0);				    															   	
			}
		}
								
		return error;
	}

	private boolean existFileXml(String name){
		
		int i=0;
		boolean found=false;
		File files = new File("./");
		String[] classes=files.list();
		
		while(i<classes.length && !found){
			
			if(classes[i].equals(name)) found=true;
			i++;
			
		}
		
		return found;
		
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
	
	public void showErrorMain() {
		view.showErrorMain();		
	}

	public void showErrorLoadClass() {
		view.showErrorLoadClass();
	}

	public void finishedTrace(String nameXlm) {
		
		if(!tracer.getWasError()){
			this.traceInspectorWriter = new TraceInspectorWriter(nameXlm);
			traceInspectorWriter.generateFinalTrace();
			view.finishedTrace();
		}
		
	}
	
	public void selectedPath(String path){
		File file = new File(path);
		
		List<String> classes;
		if (classFinder.getExtension(file).equals("jar")){
			classes = new ArrayList<>();
			classes.add(file.getName());
		} else {
			classes = classFinder.getAllClasesFromFile(file);
			view.enableMainClassCombo();
		}
	
		view.loadClasses(classes);
	}
	
	
	public String giveMePathController(String className){
		return classFinder.giveMePath(className);
	}

}
