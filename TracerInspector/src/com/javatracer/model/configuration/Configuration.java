package com.javatracer.model.configuration;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;


public class Configuration {
 
    Properties properties = null;
 
    /** Configuration file name */
    public final static String CONFIG_FILE_NAME = "configuration.properties";
    public final static String DEFAULT_NUM_LEVELS_DEPTH = "depth";
    public final static String DEFAULT_NUM_NODES = "nodes";
    public FileInputStream fileInput;
    public  FileWriter fileWriter;
    
    public Configuration() {
        this.properties = new Properties();
        try {
        	fileWriter = new FileWriter(CONFIG_FILE_NAME);
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
    	map.put("depth", "4");
    	map.put("nodes", "30");
        return map;

	}
    
    public int getDefaultNumLevelsDepth(){
    	return Integer.parseInt(properties.getProperty(DEFAULT_NUM_LEVELS_DEPTH));
    }
    
    public int getDefaultNumNodes(){
    	return Integer.parseInt(properties.getProperty(DEFAULT_NUM_NODES));
    }
    
}