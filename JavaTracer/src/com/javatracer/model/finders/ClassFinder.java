package com.javatracer.model.finders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.Type;

public class ClassFinder {
	
	private HashMap<String,String> pathsForFile=new HashMap<String,String>();
	
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
			
			File[] folderFiles = file.listFiles();
			for (int i=0;i<folderFiles.length;i++){
			
				if (folderFiles[i].isDirectory()){
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
							
							if (hasMain(jc)){
								classes.add(class_name);
								pathsForFile.put(class_name, folderFiles[i].getAbsolutePath());
							}
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
					}
				}
				
			}
						
		}
		
		else {
			classes.add(file.getName());
			pathsForFile.put(file.getName(), file.getAbsolutePath());
		}
		
		return classes;
		
	}
	
	private boolean hasMain(JavaClass jc) {
		boolean hasMain = false;
		int i = 0;
		Method[] methods = jc.getMethods();
		
		while (!hasMain && i<methods.length){
			Method method = methods[i];
			hasMain = isMainMethod(method);
			i++;
		}

		return hasMain;
	}

	private boolean isMainMethod(Method method) {
		boolean isMain = false;
		Type[] argumentTypes = method.getArgumentTypes();
		
		isMain = (method.getName().equals("main") && argumentTypes.length == 1  && method.getReturnType().equals(Type.VOID) 
					&& method.isPublic() && method.isStatic() && argumentTypes[0].toString().equals("java.lang.String[]"));				
		return isMain;
	}

	private String getExtension(File file) {
		String extension = "";

		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getPath().substring(i+1);
		}
		
		return extension;
	}
	
	
	
	public String giveMePath(String className){
		return pathsForFile.get(className);
		
	}
}
