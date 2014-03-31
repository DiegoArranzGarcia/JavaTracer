package com.tracer.controller;

import java.util.ArrayList;
import java.util.List;

import com.general.model.FileUtilities;

public class RunConfiguration {
	
	private boolean jar;
	private boolean profiling_mode;
	private String main;
	private String mainClassPath;
	private String nameXml;
	private String[] args;
	private String[] external_jars;
	
	public RunConfiguration(boolean profiling_mode,boolean jar,String main,String mainClassPath,String nameXml,String[] args,
			String[] external_jars){
		this.jar = jar;
		this.profiling_mode = profiling_mode;
		this.main = main;
		this.mainClassPath = mainClassPath;
		this.nameXml = nameXml;
		this.args = args;
		this.external_jars = external_jars;
	}
	
	public String[] getExternalJarPaths() {
		List<String> paths = new ArrayList<>();
		
		for (int i=0;i<external_jars.length;i++){
			String path = external_jars[i].substring(0,external_jars[i].lastIndexOf(FileUtilities.SEPARATOR));
			if (!paths.contains(path))
				paths.add(path);
		}
			
		return paths.toArray(new String[paths.size()]);
		
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
	
	public void setFrom_jar(boolean from_jar) {
		this.jar = from_jar;
	}
	public void setMain(String main) {
		this.main = main;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getNameXml() {
		return nameXml;
	}

	public void setNameXml(String nameXml) {
		this.nameXml = nameXml;
	}

	public String getClassPath() {
		return mainClassPath;
	}

	public void setMainClassPath(String mainClassPath) {
		this.mainClassPath = mainClassPath;
	}

	public boolean isProfiling_mode() {
		return profiling_mode;
	}

	public void setProfiling_mode(boolean profiling_mode) {
		this.profiling_mode = profiling_mode;
	}

	public String[] getExternalJars() {
		return external_jars;
	}




}
