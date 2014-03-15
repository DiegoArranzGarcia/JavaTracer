
package com.general.model;

import java.io.File;


public class FileUtilities {
	
	private static String EXTENSIONS ="[.][^.]+$";
	public static String SEPARATOR = java.io.File.separator ;
	public static String EXTENSION_XML = ".xml";
	
	
	public static String getExtension(File file) {
		String extension = "";

		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getPath().substring(i+1);
		}
		
		return extension;
	}
	
	public static String getOnlyName(String nameWithExtension) {
		String onlyName = nameWithExtension;
		int i = onlyName.lastIndexOf('.');
		onlyName = onlyName.substring(0, i);
		return onlyName;
	}
	
	public static String getFileNameWithoutExtension(String filename){
		return filename.replaceFirst(EXTENSIONS, "");
	}

	public static boolean isExtension(File file,String extension) {
		return getExtension(file).equals(extension);
	}
	
	public static boolean isExtension(String nameXml,String extension) {
		File file = new File(nameXml);
		return getExtension(file).equals(extension);
	}
	
}
