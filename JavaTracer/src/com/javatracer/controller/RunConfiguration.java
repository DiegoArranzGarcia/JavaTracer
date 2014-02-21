package com.javatracer.controller;

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
	
	public boolean check() {
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

}
