package com.profiler.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.profiler.model.data.ProfileData;
import com.sun.jdi.Method;
import com.sun.jdi.event.MethodEntryEvent;
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
		List<String> packageName = ClassUtils.getPackageList(fullClassName);
		String className = getClassName(fullClassName);
		String completeMethodName = method.toString();
		String methodName = completeMethodName.substring(fullClassName.length()+1,completeMethodName.length());
		
		profilerTree.add(packageName,className,methodName);
		
	}
	
	private String getClassName(String fullClassName) {
		String[] split = fullClassName.split("\\.");
		return split[split.length-1];
	}

	public void saveProfile(ProfilerTree data,File file) {
		try {
			XStream xStream = new XStream();
			FileWriter fileWriter = new FileWriter(file);
			xStream.toXML(data,fileWriter);
			fileWriter.close();
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

	private static List<String> splitByDot(String completeName) {
		List<String> namesList = new ArrayList<String>();
		int index = completeName.indexOf('(');
		String splitedString = completeName;
		int lastDot = 0;
		if (index != -1){
			lastDot = completeName.substring(0,index).lastIndexOf('.');
			splitedString = completeName.substring(0,lastDot);
		}
		
		String[] split = splitedString.split("\\.");
		for (int i = 0;i<split.length;i++)
			namesList.add(split[i]);
		
		if (index != -1){
			namesList.add(completeName.substring(lastDot+1));
		}
		
		return namesList;
	}
	
	public ProfileData getData(String completeName) {
		return profilerTree.getData(splitByDot(completeName));
	}
	
}
