package com.javatracer.model.configuration;


import java.io.IOException;
import java.util.Properties;


public class Configuration {
 
    Properties properties = null;
 
    /** Configuration file name */
    public final static String CONFIG_FILE_NAME = "Configuration.properties";
    public final static String DEFAULT_NUM_LEVELS_DEPTH = "depth";
    public final static String DEFAULT_NUM_NODES = "nodes";
 
    public Configuration() {
        this.properties = new Properties();
        try {
            properties.load(Configuration.class.getResourceAsStream(CONFIG_FILE_NAME));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 
    public String aux(){
    	return properties.getProperty(DEFAULT_NUM_LEVELS_DEPTH);
    }
    public int getDefaultNumLevelsDepth(){
    	return Integer.parseInt(properties.getProperty(DEFAULT_NUM_LEVELS_DEPTH));
    }
    
    public int getDefaultNumNodes(){
    	return Integer.parseInt(properties.getProperty(DEFAULT_NUM_NODES));
    }
    
}