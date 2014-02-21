package com.general.model.configuration;


import java.io.*;
import java.util.HashMap;
import java.util.Properties;


public class TraceInspectorConfiguration {
 
    private final static String CONFIG_FILE_NAME = "inspector.properties";
    private final static String DEFAULT_NUM_LEVELS_DEPTH = "depth";
    private final static String DEFAULT_NUM_NODES = "nodes";
    private FileInputStream fileInput;
    private  FileWriter fileWriter;
    private Properties properties;
    
    public TraceInspectorConfiguration() {
    	/*
    	 * Create a file configuration
    	 */
    	this.properties = new Properties();
    	
    	File f = new File(CONFIG_FILE_NAME);
    	if (f.exists()){
    		try {
				this.properties.load(new FileInputStream(CONFIG_FILE_NAME));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else{	
		        try {
		        	
			        	/*
			        	 * The file is created on disk
			        	 */
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
			            properties.store(new FileOutputStream(CONFIG_FILE_NAME),"ConfigT Trace Inspector");
			            fileWriter.close();
		        	
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
    	}
    }

    private HashMap<String, String> getConfig() {
    	HashMap<String, String> map= new HashMap<>();
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