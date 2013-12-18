package com.javatracer.model.configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


public class Configuration {
 
   
	private Properties properties;
	private final static String CONFIG_FILE_NAME = "configuration.properties";
	private final static String EXCLUDES = "excludes";
	private FileInputStream fileInput;
	private  FileWriter fileWriter;
 
    public Configuration() {

    	/*
    	 * Create a file configuration
    	 */

        this.properties = new Properties();
        try {
        	/*
        	 * The file is created on disk
        	 */
        	
        		System.out.println("FICHERO NO EXISTE ");
        		fileWriter = new FileWriter(CONFIG_FILE_NAME);
            	
            	/*
            	 * The file is uploaded to a File to use the Load method
            	 */
            	fileInput = new FileInputStream(CONFIG_FILE_NAME);
            	
            	/*
            	 * With the method load, we can load the object properties
            	 */
                properties.load(fileInput);
                /*
                 * Now we add information to the properties through a Hashmap
                 */
           
                	properties.putAll(getConfig());
                /*
                 * Save the data on the properties
                 */
                properties.store(new FileOutputStream("configuration.properties"),"Config");
                fileWriter.close();
                fileInput.close();
        	
        	System.out.println("FICHERO  EXISTE ");
        	
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
 
    private HashMap getConfig() {
    	HashMap map= new HashMap();
    	map.put("excludes", "java.*,javax.*,sun.*,com.sun.*,java.awt.*,java.swing*");
        return map;

	}

	public String getExcludesFromConfig(){
    	return this.properties.getProperty(EXCLUDES);
    }
    
    public String[] getExcludes(){
    	String excludes = getExcludesFromConfig();
    	String delims = ",";
    	String[] tokens = excludes.split(delims);
    	return tokens;
    }
}