package com.javatracer.model.configuration;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Configuration {
 
   
	public Properties properties;
    /** Configuration file name */
    public final static String CONFIG_FILE_NAME = "configuration.properties";
    public final static String EXCLUDES = "excludes";
    public FileInputStream fileInput;
    public  FileWriter fileWriter;
 
    public Configuration() {
    	fileInput= null;
    	fileWriter =null;

        this.properties = new Properties();
        try {
        	fileWriter = new FileWriter("configuration.properties");
        	fileInput = new FileInputStream(CONFIG_FILE_NAME);
            properties.load(fileInput);
            properties.putAll(getConfig());
            properties.store(new FileOutputStream("configuration.properties"),"Config");
            fileWriter.close();
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