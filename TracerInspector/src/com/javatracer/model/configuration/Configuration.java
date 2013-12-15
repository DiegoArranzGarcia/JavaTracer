package com.javatracer.model.configuration;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configuration {
 
    Properties properties = null;
 
    /** Configuration file name */
    public final static String CONFIG_FILE_NAME = "configuration.properties";
    public final static String DEFAULT_NUM_LEVELS_DEPTH = "depth";
    public final static String DEFAULT_NUM_NODES = "nodes";
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

    public int getDefaultNumLevelsDepth(){
    	return Integer.parseInt(properties.getProperty(DEFAULT_NUM_LEVELS_DEPTH));
    }
    
    public int getDefaultNumNodes(){
    	return Integer.parseInt(properties.getProperty(DEFAULT_NUM_NODES));
    }
    
}