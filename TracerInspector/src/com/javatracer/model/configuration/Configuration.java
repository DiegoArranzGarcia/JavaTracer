package com.javatracer.model.configuration;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;


public class Configuration {
 
    private final static String CONFIG_FILE_NAME = "configuration.properties";
    private final static String DEFAULT_NUM_LEVELS_DEPTH = "depth";
    private final static String DEFAULT_NUM_NODES = "nodes";
    private FileInputStream fileInput;
    private  FileWriter fileWriter;
    private Properties properties;
    
    public Configuration() {
    	fileInput= null;
    	fileWriter =null;
    	/**
    	 * Create a file configuration
    	 */
        this.properties = new Properties();
        try {
        	/**
        	 * The file is created on disk
        	 */
        	fileWriter = new FileWriter(CONFIG_FILE_NAME);
        	/**
        	 * The file is uploaded to a File to use the Load method
        	 */
        	fileInput = new FileInputStream(CONFIG_FILE_NAME);
        	/**
        	 * With the method load, we can load the object properties
        	 */
            properties.load(fileInput);
            /**
             * Now we add information to the properties through a Hashmap
             */
            properties.putAll(getConfig());
            /**
             * Save the data on the properties
             */
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