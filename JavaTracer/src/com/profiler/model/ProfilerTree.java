package com.profiler.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.profiler.model.data.ProfileClass;
import com.profiler.model.data.ProfileData;
import com.profiler.model.data.ProfileMethod;
import com.profiler.model.data.ProfilePackage;

public class ProfilerTree {

	ProfileData rootNode;
	
	public ProfilerTree(){
		rootNode = new ProfilePackage("All packages");
		((ProfilePackage)rootNode).setCompleteName("");
	}
	
	public void add(List<String> packageName, String className,String methodName){
		addRec(rootNode,packageName,className,methodName);
	}
	
	private void addRec(ProfileData node,List<String> packageName, String className,String methodName){
		
		if (!packageName.isEmpty()){
			
			String lookedPackage = packageName.get(0);
			int packageIndex = foundPackage(node,lookedPackage);
			
			if (packageIndex == -1){
				ProfileData data = new ProfilePackage(lookedPackage);
				node.add(data);
				packageIndex = node.getNumChildren()-1;
			}
			
			ProfilePackage profilePackage = (ProfilePackage) node.getChild(packageIndex);
			packageName.remove(0);
			addRec(profilePackage,packageName,className,methodName);
			
		} else {
			
			int classIndex = foundClass(node,className);
			
			if (classIndex == -1){
				ProfileClass data = new ProfileClass(className);
				node.add(data);
				classIndex= node.getNumChildren()-1;
			}
			
			ProfileClass profileClass = (ProfileClass) node.getChild(classIndex);
			
			int methodIndex = foundMethod(profileClass,methodName);
			
			if (methodIndex == -1){
				ProfileMethod data = new ProfileMethod(methodName);
				profileClass.add(data);
				methodIndex = profileClass.getNumChildren()-1;
			}
			
			ProfileMethod profileMethod = (ProfileMethod) profileClass.getChild(methodIndex);
			profileMethod.increment();
			
		}
		
	}

	private int foundMethod(ProfileData node,String methodName) {
		int index = -1;
		boolean found = false;
		int i = 0;
		
		while (i<node.getNumChildren() && !found){
			
			ProfileData data = node.getChild(i);
			found = (data instanceof ProfileMethod && data.getName().equals(methodName));
			
			if (!found)
				i++;
		}
		
		if (found)
			index = i;
		
		return index;
	}

	private int foundClass(ProfileData node, String className) {
		int index = -1;
		boolean found = false;
		int i = 0;
		
		while (i<node.getNumChildren() && !found){
			
			ProfileData data = node.getChild(i);
			found = (data instanceof ProfileClass && data.getName().equals(className));
			
			if (!found)
				i++;
		}
		
		if (found)
			index = i;
		
		return index;
	}

	private int foundPackage(ProfileData node, String packageName) {
		int index = -1;
		boolean found = false;
		int i = 0;
		
		while (i<node.getNumChildren() && !found){
			
			ProfileData data = node.getChild(i);
			found = (data instanceof ProfilePackage && data.getName().equals(packageName));
			
			if (!found)
				i++;
		}
		
		if (found)
			index = i;
		
		return index;
	}

	public void removeAll() {
		rootNode.removeAllChildren();
	}

	public int getNumCalls() {
		return rootNode.getNumCalls();
	}

	public HashMap<String, Integer> getClasses() {
		HashMap<String,Integer> classes = new HashMap<String, Integer>();
		getallClassesRec(rootNode,classes);
		return classes;
	}

	private void getallClassesRec(ProfileData node, HashMap<String, Integer> classes) {
		
		if (node instanceof ProfilePackage){
			
			for (int i=0;i<node.getNumChildren();i++)
				getallClassesRec(node.getChild(i), classes);
			
		} else if (node instanceof ProfileClass){
			
			classes.put(((ProfileClass) node).getCompleteName(), node.getNumCalls());
						
		}
		
		
	}

	public ProfileData getRoot() {
		return rootNode;
	}

	public ProfileData getData(List<String> list) {
		return getDataRec(rootNode,list);
	}
	
	private ProfileData getDataRec(ProfileData node,List<String> keys){
		ProfileData data = null;
		
		if (keys.isEmpty())
			data = node;
		else {
			List<ProfileData> children = node.getChildren();
			boolean found = false;
			String key = keys.get(0);
			int i = 0;
			while (!found && i<children.size()){
				found = children.get(i).getName().equals(key);
				if (!found)
					i++;
			}
			
			if (found){
				keys.remove(0);
				data = getDataRec(children.get(i),keys);
			}
				
		} 
			
		return data;
	}

	public void setCheckedClasses(HashMap<List<String>, Boolean> dataState) {
		
	}

	
	public void changeExcludeNodes(HashMap<List<String>, Boolean> states) {
		
		Set<List<String>> keys = states.keySet();
		Iterator<List<String>> it = keys.iterator();
		
		while(it.hasNext()){
			
			List<String> node = it.next();
			if(!states.get(node)){
				
				ProfileData trueNode = getData(node);
				if(trueNode!=null){
					
					int classes=foundClass(trueNode.getParent(), trueNode.getName());
					int packagee=foundPackage(trueNode.getParent(), trueNode.getName());
					int method=foundMethod(trueNode.getParent(), trueNode.getName());
					
					if(classes!=-1||method!=-1)
						trueNode.remove();
					else
						if(packagee!=-1){
							
							ProfileData parent=trueNode.getParent(); 
							ProfileData son=trueNode; 
							while(!parent.getName().equals("All packages")){
							      parent.removeAllChildren();
							      son=parent;
							      parent=parent.getParent();
							     }
							son.remove();
							}
				
					}
				
			}
			
			
		}
		
		
	}
	



	
}
