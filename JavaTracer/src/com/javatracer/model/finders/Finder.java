package com.javatracer.model.finders;

import java.io.File;

public abstract class Finder {
	
	public String getExtension(File file) {
		String extension = "";

		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getPath().substring(i+1);
		}
		
		return extension;
	}
	
	public String getFileNameWithoutExtension(String filename){
		return filename.replaceFirst("[.][^.]+$", "");
	}
}
