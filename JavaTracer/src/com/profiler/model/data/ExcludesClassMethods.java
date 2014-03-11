package com.profiler.model.data;

import java.util.ArrayList;
import java.util.List;

public class ExcludesClassMethods {
	
	private List<ClassMethods> methodList;
	
	/**
	 * Creates a class to excludes methods of the passed class.
	 */
	public ExcludesClassMethods(){
		this.methodList = new ArrayList<>();
	}
	
	
	/**
	 * Add the method name to the excludes methods of the class.
	 * @param className - The complete name of the class.
	 * @param methodName - The complete name of the method.
	 */

	public void addMethod(String className, String methodName){
		int index = findClass(className);
		
		if (index == -1){
			methodList.add(new ClassMethods(className));
			index = methodList.size()-1;
		}
		
		methodList.get(index).addMethod(methodName);
	}


	private int findClass(String className) {
		
		int i = 0;
		boolean found = false;
		
		while (!found && i<methodList.size()){
			found = methodList.get(i).getClassName().equals(className);
			if (!found)		
				i++;
		}
		
		
		if (found)
			return i;
		else
			return -1;
	}


	/**
	 * @return A list of classes which inside them, there are all
	 * the methods excludes for this class.
	 */
	public List<ClassMethods> getMethodList() {
		return methodList;
	}
	
}
