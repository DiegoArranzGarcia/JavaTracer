package com.javatracer.model.finders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JarFinder {

	public String getJarDirectories(String path) {
		
		List<String> jarDirectories = new ArrayList<String>();
		
		try {	
			File file = new File(path);
			getJarDirectoriesRec(file,jarDirectories);
						
		} catch (Exception e){
			e.printStackTrace();
		}
		
		String directories = "";
		for (int i=0;i<jarDirectories.size();i++)
			directories += jarDirectories.get(i) + "\\*;"; 
				
		return directories;
	}

	public void getJarDirectoriesRec(File file,List<String> jars) {
			    
	    if (file.isDirectory()){
			
			File[] folderFiles = file.listFiles();
			for (int i=0;i<folderFiles.length;i++)
				getJarDirectoriesRec(folderFiles[i],jars);
			
		}
		
		else if (file.isFile() && getExtension(file).equals("jar")){
			if (!jars.contains(file.getParent()))
				jars.add(file.getParent());
		}
		
	}
	
	private String getExtension(File file) {
		String extension = "";

		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getPath().substring(i+1);
		}
		
		return extension;
	}
	
}
