package com.profiler.model.data;

import java.util.ArrayList;
import java.util.List;

public class ExcludedClassesMethods {
	
	private List<ClassMethods> classList;
	
	/**
	 * Creates a class to excludes methods of the passed class.
	 */
	public ExcludedClassesMethods(){
		this.classList = new ArrayList<>();
	}
	
	
	/**
	 * Add the method name to the excludes methods of the class.
	 * @param className - The complete name of the class.
	 * @param methodName - The complete name of the method.
	 */

	public void addMethod(String className, String methodName){
		
		int index = findClass(className);
		
		if (index == -1)
			index = addClass(new ClassMethods(className));
		
		classList.get(index).addMethod(methodName);
	}
	
	public void removeMethod(String className, String methodName){
		int index = findClass(className);
		
		if (index != -1){
			ClassMethods classMethods = classList.get(index);
			classMethods.removeMethod(methodName);
			if (!classMethods.hasMethods())
				classList.remove(classMethods);
		}

	}


	private int findClass(String className) {
		
		int index = 0;
		boolean found = false;
		
		while (!found && index<classList.size()){
			found = classList.get(index).getClassName().equals(className);
			if (!found)		
				index++;
		}
		
		
		if (!found)
			index = -1;
		
		return index;
	}


	/**
	 * @return A list of classes which inside them, there are all
	 * the methods excludes for this class.
	 */
	public List<ClassMethods> getClassList() {
		return classList;
	}


	public boolean isExcluded(String className, String methodName) {
		
		boolean excluded = false;

		int index = findClass(className);
		
		if (index != -1){
			excluded = classList.get(index).isExcluded(methodName);
		}
		
		return excluded;
	}

	private int addClass(ClassMethods classMethods) {
		
		int index = findClass(classMethods.getClassName());
		
		if (index == -1){
			classList.add(classMethods);
			index = classList.size()-1;
		} else {
		
			ClassMethods current = classList.get(index);
			List<String> excludesMethods = classMethods.getExcludesMethods();
			for (int i =0;i<excludesMethods.size();i++){
				current.addMethod(excludesMethods.get(i));
			}
		}
		
		return index;
		
	}

	public void addExcludedClassesMethods(ExcludedClassesMethods newExcludesMethods) {
		
		List<ClassMethods> classes = newExcludesMethods.getClassList();
		
		for (int i=0;i<classes.size();i++){
			addClass(classes.get(i));						
		}
		
	}
	
}
