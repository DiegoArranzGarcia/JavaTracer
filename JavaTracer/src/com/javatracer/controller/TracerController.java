package com.javatracer.controller;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import com.javatracer.model.Tracer;
import com.javatracer.model.finders.ClassFinder;
import com.javatracer.model.finders.JarFinder;
import com.javatracer.model.writers.TraceInspectorWriter;
import com.javatracer.view.Message;
import com.javatracer.view.WindowPath;

public class TracerController {
	
	private WindowPath view;
	
	private Tracer tracer;
	private TraceInspectorWriter traceInspectorWriter;
	private ClassFinder classFinder;
	private JarFinder jarFinder;
	
	public TracerController(){
		 this.view = new WindowPath(this);
		 this.classFinder = new ClassFinder();
		 this.jarFinder = new JarFinder();
    }
	
	public void startTrace(){
				
		String[] args = getArguments();
		String nameXml = getNameXml();
		
		boolean error = checkErrors(args,nameXml);
		
		if (!error){
			tracer = new Tracer(this);
			tracer.trace(args,nameXml);
		}
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
	
	private String[] getArguments() {
		String path = view.getPath();
		String mainClass = view.getMainClass();
		String jarDirectories = jarFinder.getJarDirectories(path);
		
		String[] args = new String[3];
		args[0] = processPath(path,mainClass);
		args[1] = mainClass;
		args[2] = jarDirectories;
		return args;
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
	
	public void loadClassFromPath(String path){
		List<String> classes = classFinder.getAllClasesFromPath(path);
		view.loadClasses(classes);
	}
	
	
	public String giveMePathController(String className){
		return classFinder.giveMePath(className);
	}

}
