package com.general.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JarFinder {

	public String[] getJars(String path) {
		
		List<String> jarDirectories = new ArrayList<String>();
		
		try {	
			File file = new File(path);
			getJarsRec(file,jarDirectories);
						
		} catch (Exception e){
			e.printStackTrace();
		}
						
		return jarDirectories.toArray(new String[jarDirectories.size()]);
	}

	public void getJarsRec(File file,List<String> jars) {
			    
	    if (file.isDirectory()){
			
			File[] folderFiles = file.listFiles();
			for (int i=0;i<folderFiles.length;i++)
				getJarsRec(folderFiles[i],jars);
		
		}
		
		else if (file.isFile() && FileUtilities.getExtension(file).equals("jar")){
			if (!jars.contains(file.getAbsolutePath()))
				jars.add(file.getAbsolutePath());
		}
		
	}
	
}
