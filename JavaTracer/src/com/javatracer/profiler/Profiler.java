package com.javatracer.profiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.javatracer.model.TracerUtilities;
import com.sun.jdi.Method;
import com.sun.jdi.event.MethodEntryEvent;

public class Profiler {

	HashMap<String,MethodDataProfiler> registredMethods;
	
	public Profiler(){
		registredMethods = new HashMap<>();
	}
	
	public void profileEvent(MethodEntryEvent event) {
		
		Method method = event.method();
		
		String className = TracerUtilities.getClass(method.declaringType());
		String methodName = method.toString();
		
		registerMethod(className,methodName);
		
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

	public void showProfile() {
		
		Set<Entry<String, MethodDataProfiler>> methods = registredMethods.entrySet();
		Iterator<Entry<String, MethodDataProfiler>> iterator = methods.iterator();
		
		while (iterator.hasNext()){
			MethodDataProfiler method = iterator.next().getValue();
			printMethod(method);
		}
		 
	}

	private void printMethod(MethodDataProfiler method) {
		System.out.println("The method : " + method.getMethod() + " has been called " + method.getNumCalls() + " times");		
	}

}
