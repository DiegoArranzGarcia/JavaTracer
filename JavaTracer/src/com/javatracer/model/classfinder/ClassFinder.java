package com.javatracer.model.classfinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;

public class ClassFinder {
	
	
	private HashMap<String,String> PathsForFile=new HashMap<String,String>(); 
	private String path="";
	
	public List<String> getAllClasesFromPath(String path) {
		
		List<String> classes = null;
		
		try {
			
			File file = new File(path);
			classes = getClassesFromFile(file);
						
		} catch (Exception e){
			e.printStackTrace();
			classes = new ArrayList<>();
		}
		
		return classes;
		
	}

	private List<String> getClassesFromFile(File file){
		
		List<String> classes = new ArrayList<String>();
	    
	    if (file.isDirectory()){
			path=path + "\\" + file.getName();
			
			File[] folderFiles = file.listFiles();
			for (int i=0;i<folderFiles.length;i++){
			
				if (folderFiles[i].isDirectory()){
					//path=path + "\\" + folderFiles[i].getName();
					List<String> classesFile = getClassesFromFile(folderFiles[i]);
					classes.addAll(classesFile);
					
				} else if (folderFiles[i].isFile()){
					
					String extension = getExtension(folderFiles[i]);
					
					if (extension.equals("class")){
						
						ClassParser cp;
						
						try {
							
							cp = new ClassParser(folderFiles[i].getPath());
							JavaClass jc = cp.parse();
							String class_name = jc.getClassName();
							classes.add(class_name);
							String PathParse= path.substring(path.indexOf("\\", 1)+1, path.length());
							PathsForFile.put(class_name, PathParse);
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
					}
				}
				
			}
						
		}
		
		else {classes.add(file.getName());
			String PathParser= path.substring(path.indexOf("\\", 1)+1, path.length());
			PathsForFile.put(file.getName(), PathParser);
		      }
		
		return classes;
		
	}
	
	private String getExtension(File file) {
		String extension = "";

		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getPath().substring(i+1);
		}
		
		return extension;
	}
	
	
	
	public String giveMePath(String key){
		return PathsForFile.get(key);
		
	}
}
