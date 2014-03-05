package com.profiler.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.sun.jdi.Method;
import com.sun.jdi.event.MethodEntryEvent;
import com.thoughtworks.xstream.XStream;
import com.tracer.model.ClassUtils;

public class Profiler implements ProfilerModelInterface{
	
	private HashMap<String,MethodDataProfiler> registredMethods;
	private int totalTimeCalledMethods;
	
	private HashMap<String,Integer> registredClasses;
	
	public Profiler(){
		registredMethods = new HashMap<>();
		registredClasses = new HashMap<>();
		totalTimeCalledMethods = 0;
	}
	
	public void profileEvent(MethodEntryEvent event) {
		
		Method method = event.method();
		
		String className = ClassUtils.getClass(method.declaringType());
		String methodName = method.toString();
		
		registerMethod(className,methodName);
		registerClass(className);
		totalTimeCalledMethods++;
		
	}
	
	private void registerMethod(String className, String methodName) {
		
		String methodKey = methodName;
		MethodDataProfiler methodDataProfiler = registredMethods.get(methodKey);
		
		if (methodDataProfiler == null){
			
			MethodDataProfiler newMethodData = new MethodDataProfiler(className,methodName);
			registredMethods.put(methodKey, newMethodData);
			
		} else {
			
			methodDataProfiler.methodCalled();
			
		}
		
	}
	
	private void registerClass(String className) {

		Integer numCalls = registredClasses.get(className);
		if (numCalls == null){
			
			registredClasses.put(className,new Integer(1));

		} else {
			
			registredClasses.remove(className);
			int times = new Integer(numCalls.intValue()+1);
			registredClasses.put(className, times);
			
		}
		
	}
	
	public void saveProfile(ProfileData data,File file) {
		try {
			XStream xStream = new XStream();
			FileWriter fileWriter = new FileWriter(file);
			xStream.toXML(data,fileWriter);
		} catch (IOException exc) {
			
		}
	}

	public ProfileData openProfile(File file){
		XStream xStream = new XStream();
		return (ProfileData) xStream.fromXML(file);
	}


	//Getters and setters
	
	public int getTotalTimeCalledMethods() {
		return totalTimeCalledMethods;
	}

	public void setTotalTimeCalledMethods(int totalTimeCalledMethods) {
		this.totalTimeCalledMethods = totalTimeCalledMethods;
	}

	public HashMap<String, MethodDataProfiler> getRegistredMethods() {
		return registredMethods;
	}

	public HashMap<String, Integer> getRegistredClasses() {
		return registredClasses;
	}

	public void setRegistredMethods(
			HashMap<String, MethodDataProfiler> registredMethods) {
		this.registredMethods = registredMethods;
	}

	public void setRegistredClasses(HashMap<String, Integer> registredClasses) {
		this.registredClasses = registredClasses;
	}
	
}
