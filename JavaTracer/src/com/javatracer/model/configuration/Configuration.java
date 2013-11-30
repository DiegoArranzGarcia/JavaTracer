package com.javatracer.model.configuration;
import java.io.IOException;
import java.util.Properties;


public class Configuration {
 
    Properties properties = null;
 
    /** Configuration file name */
    public final static String CONFIG_FILE_NAME = "Configuration.properties";
    public final static String EXCLUDES = "excludes";
 
    public Configuration() {
        this.properties = new Properties();
        try {
            properties.load(Configuration.class.getResourceAsStream(CONFIG_FILE_NAME));
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