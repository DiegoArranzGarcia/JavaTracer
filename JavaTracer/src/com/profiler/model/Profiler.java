package com.profiler.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.sun.jdi.Method;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.source.tree.Tree;
import com.thoughtworks.xstream.XStream;
import com.tracer.model.ClassUtils;

public class Profiler implements ProfilerModelInterface{
	
	private ProfilerTree profilerTree;
	
	public Profiler(){
		this.profilerTree = new ProfilerTree();
	}
	
	public void profileEvent(MethodEntryEvent event) {
		
		Method method = event.method();
		
		String fullClassName = ClassUtils.getClass(method.declaringType());
		List<String> packageName = getPackages(fullClassName);
		String className = getClassName(fullClassName);
		String methodName = method.toString();
		
		profilerTree.add(packageName,className,methodName);
		
	}
	
	private String getClassName(String fullClassName) {
		String[] split = fullClassName.split("\\.");
		return split[split.length-1];
	}

	private List<String> getPackages(String fullClassName) {
		List<String> packages = new ArrayList<String>();
		String[] split = fullClassName.split("\\.");
		String completePackageName = "";
        for (int i=0;i<split.length-1;i++){
        	
        	if (i>0)
        		completePackageName+= ".";
        	
        	completePackageName += split[i];
        	packages.add(completePackageName);
        }
		return packages;
	}

	public void saveProfile(ProfilerTree data,File file) {
		try {
			XStream xStream = new XStream();
			FileWriter fileWriter = new FileWriter(file);
			xStream.toXML(data,fileWriter);
		} catch (IOException exc) {
			
		}
	}

	public ProfilerTree openProfile(File file){
		XStream xStream = new XStream();
		profilerTree = (ProfilerTree) xStream.fromXML(file);
		return profilerTree;
	}

	//Getters and setters

	public void clean() {
		profilerTree.removeAll();
	}

	public int getNumMethodCalls() {
		return profilerTree.getNumCalls();
	}

	public ProfilerTree getProfileTree() {
		return profilerTree;
	}

	public Iterator<Entry<String, Integer>> getClassesInfo() {
		return profilerTree.getClasses().entrySet().iterator();
	}
	
}
