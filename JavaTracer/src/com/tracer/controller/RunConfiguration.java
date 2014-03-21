package com.tracer.controller;

public class RunConfiguration {
	
	private boolean jar;
	private boolean profiling_mode;
	private String main;
	private String mainClassPath;
	private String nameXml;
	private String[] args;
	private String[] external_jars;
	
	public RunConfiguration(boolean profiling_mode,boolean jar,String main,String mainClassPath,String nameXml,String[] args,String[] external_jars){
		this.jar = jar;
		this.profiling_mode = profiling_mode;
		this.main = main;
		this.mainClassPath = mainClassPath;
		this.nameXml = nameXml;
		this.args = args;
		this.external_jars = external_jars;
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

	public boolean isProfiling_mode() {
		return profiling_mode;
	}

	public void setProfiling_mode(boolean profiling_mode) {
		this.profiling_mode = profiling_mode;
	}




}
