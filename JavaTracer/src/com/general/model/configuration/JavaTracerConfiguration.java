package com.general.model.configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


public class JavaTracerConfiguration {
 
   
	private Properties properties;
	private final static String CONFIG_FILE_NAME = "tracer.properties";
	private final static String EXCLUDES = "excludes";
	private FileInputStream fileInput;
	private  FileWriter fileWriter;
 
    public JavaTracerConfiguration() {

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
                    properties.store(new FileOutputStream(CONFIG_FILE_NAME),"Config Tracer");
                    fileWriter.close();
                    fileInput.close();
            	
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    		
    	}
        
        
    }
 
    private HashMap<String, String> getConfig() {
    	HashMap<String, String> map= new HashMap<>();
    	map.put("excludes", "java.*,javax.*,sun.*,com.sun.*");
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