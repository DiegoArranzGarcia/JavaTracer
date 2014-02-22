package com.javatracer.controller;

import java.io.File;

import javax.swing.JOptionPane;

import com.javatracer.view.JavaTracerView;
import com.javatracer.view.Message;

public class RunConfiguration {
	
	private boolean jar;
	private String main;
	private String mainClassPath;
	private String nameXml;
	private String[] args;
	private String[] external_jars;
	
	public RunConfiguration(boolean jar,String main,String mainClassPath,String nameXml,String[] args,String[] external_jars){
		this.jar = jar;
		this.main = main;
		this.mainClassPath = mainClassPath;
		this.nameXml = nameXml;
		this.args = args;
		this.external_jars = external_jars;
	}
	
	public boolean check(JavaTracerView view) {
		
		if (checkNameXml(nameXml,view)) return true;
		
		
		
		return false;
	}
	
	//Getters and setters
	
	public boolean isJar() {
		return jar;
	}
	public String getMain() {
		return main;
	}
	public String[] getArgs() {
		return args;
	}
	public String[] getExternal_jars() {
		return external_jars;
	}
	public void setFrom_jar(boolean from_jar) {
		this.jar = from_jar;
	}
	public void setMain(String main) {
		this.main = main;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
	public void setExternal_jars(String[] external_jars) {
		this.external_jars = external_jars;
	}

	public String getNameXml() {
		return nameXml;
	}

	public void setNameXml(String nameXml) {
		this.nameXml = nameXml;
	}

	public String getMainClassPath() {
		return mainClassPath;
	}

	public void setMainClassPath(String mainClassPath) {
		this.mainClassPath = mainClassPath;
	}


private boolean checkNameXml(String nameXml,JavaTracerView view) { 
		
		boolean error = false;
		
		if (nameXml.contains(".xml") || nameXml.contains(".XML")){
			view.errorNameXml();
			return true;
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



}
