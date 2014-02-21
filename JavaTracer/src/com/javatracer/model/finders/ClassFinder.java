package com.javatracer.model.finders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.Type;

public class ClassFinder extends Finder{
	
	public static String TEMP = "temp_";
	public static String FILES = "_files";

	private HashMap<String,String> pathsForFile;
	
	public List<String> getAllClasesFromFile(File file) {
		
		this.pathsForFile = new HashMap<String,String>();
		List<String> classes = null;
		
		try {
			
			if (getExtension(file).equals("jar")){
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

	private List<String> getClassFromJar(File file) {
		
		String temp_jar_directory = TEMP + getFileNameWithoutExtension(file.getName()) + FILES;
		extractJarOnDirectory(file,temp_jar_directory);
		List<String> classes = getAllClasesFromFile(new File(temp_jar_directory));
		
		return classes;
	}
	
	
	@SuppressWarnings("resource")
	private void extractJarOnDirectory(File file, String temp_jar_directory) {
		try {
			
			JarFile jarFile = new JarFile(file);
			
			File temp_directory = new File(temp_jar_directory);
			temp_directory.mkdir();
			
			Enumeration<JarEntry> enumeration = jarFile.entries();
			
			while (enumeration.hasMoreElements()){
				
				JarEntry entry = enumeration.nextElement();	
				
				String fileName = temp_jar_directory + java.io.File.separator + entry.getName(); 
				File temp_file = new File(fileName);
				
				if (fileName.endsWith("/")) {
					temp_file.mkdirs();
				} 
				
			}
			
			enumeration = jarFile.entries();
			
			while (enumeration.hasMoreElements()){
				
				JarEntry entry = enumeration.nextElement();	
				 
				String fileName = temp_jar_directory + java.io.File.separator + entry.getName(); 
				File temp_file = new File(fileName);
				
				if (!fileName.endsWith("/") && !fileName.contains("MANIFEST.MF")) {
					InputStream is = jarFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(temp_file);
	 
					// write contents of 'is' to 'fos'
					while (is.available() > 0) {
						fos.write(is.read());
					}
	 
					fos.close();
					is.close();
				}
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private List<String> getClassesFromFile(File file){
		
		List<String> classes = new ArrayList<String>();
	    
	    if (file.isDirectory()){
			
	    	List<String> classesFile;
			File[] folderFiles = file.listFiles();
			for (int i=0;i<folderFiles.length;i++){
			
				classesFile = getClassesFromFile(folderFiles[i]);
				classes.addAll(classesFile);

			}
										
		}
		
		else if (file.isFile()) {

			String extension = getExtension(file);
			
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
	
	public String giveMePath(String className){
		return pathsForFile.get(className);
		
	}
}
