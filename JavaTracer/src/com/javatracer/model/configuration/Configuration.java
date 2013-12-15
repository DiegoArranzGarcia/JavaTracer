package com.javatracer.model.configuration;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configuration {
 
   
	public Properties properties;
    /** Configuration file name */
    public final static String CONFIG_FILE_NAME = "configuration.properties";
    public final static String EXCLUDES = "excludes";
    public InputStream is;
 
    public Configuration() {
        this.properties = new Properties();
        try {
        	
        	is = new FileInputStream(CONFIG_FILE_NAME);
            properties.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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