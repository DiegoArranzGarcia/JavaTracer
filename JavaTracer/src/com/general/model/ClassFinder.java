package com.general.model;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.sun.org.apache.bcel.internal.classfile.*;
import com.sun.org.apache.bcel.internal.generic.Type;

public class ClassFinder {
	
	public static String TEMP = "temp_";
	public static String FILES = "_files";

	private HashMap<String,String> pathsForFile;
	
	public List<String> getAllClassesFromFile(File file) {
		
		this.pathsForFile = new HashMap<String,String>();
		List<String> classes = null;
		
		try {
			
			if (FileUtilities.getExtension(file).equals("jar")){
				classes = getClassFromJar(file);
			} else {
				classes = getClassesFromFile(file);
			}
						
		} catch (Exception e){
			e.printStackTrace();
			classes = new ArrayList<>();
		}
		
		return classes;
		
	}

	public List<String> getClassFromJar(File file) {
		
		List<String> classes = new ArrayList<>();
		try {
			@SuppressWarnings("resource")
            JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> enumeration = jarFile.entries();
			
			while (enumeration.hasMoreElements()){
				
				JarEntry entry = enumeration.nextElement();	
				
				if (FileUtilities.isExtension(entry.getName(),"class")){
					String className = entry.getName().replace("/",".");
					className = className.substring(0,className.indexOf(".class"));
					if (!classes.contains(className))
						classes.add(className);
				}
								
			}
			
		} catch (IOException e) {
			
		}
						
		return classes;
	}
	
	public List<String> getClassesFromFile(File file){
		
		List<String> classes = new ArrayList<String>();
	    
	    if (file.isDirectory()){
			
	    	List<String> classesFile;
			File[] folderFiles = file.listFiles();
			for (int i=0;i<folderFiles.length;i++){
			
				if(folderFiles[i].isFile() && FileUtilities.getExtension(folderFiles[i]).equals("jar") && ExecutableJar(folderFiles[i])){	
					classes.add(folderFiles[i].getName());
				    pathsForFile.put(folderFiles[i].getName(),folderFiles[i].toString());	
				}
				
				classesFile = getClassesFromFile(folderFiles[i]);
				classes.addAll(classesFile);

			}
										
		}
		
		else if (file.isFile()) {
			String extension = FileUtilities.getExtension(file);
			
			if (extension.equals("class")){
				
				ClassParser cp;
				
				try {
					
					cp = new ClassParser(file.getPath());
					JavaClass jc = cp.parse();
					String class_name = jc.getClassName();
					
					if (hasMain(jc)){
						classes.add(class_name);
						pathsForFile.put(class_name,file.getAbsolutePath());
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
	
	public String getPath(String className){
		return pathsForFile.get(className);
		
	}


	public boolean ExecutableJar(File file) {
		boolean isExecutable=false;
		try{
		@SuppressWarnings("resource")
        JarFile jar = new JarFile(file);
 
		// fist get all directories,
		// then make those directory on the destination Path
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();
			String fileName =entry.getName();
			if(fileName.contains("MANIFEST")){
				InputStream is = jar.getInputStream(entry);
				@SuppressWarnings("resource")
                String inputStreamString = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
				is.close();
					
				if(inputStreamString.contains("Main")||inputStreamString.contains("main"))	
					isExecutable=true;	
			}
		}
	}catch(Exception e){ 
		e.printStackTrace();
		}
		
		return isExecutable;
	}



}
