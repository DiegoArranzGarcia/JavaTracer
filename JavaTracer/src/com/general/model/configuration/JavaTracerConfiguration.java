package com.general.model.configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class JavaTracerConfiguration {
 	
	private final static String CONFIG_FILE_NAME = "tracer.properties";
	private final static String EXCLUDES = "excludes";
	private final static String FILE_TITLE = "Config Tracer";
	
	private static JavaTracerConfiguration instance;
	
	private Properties properties;
	private FileInputStream fileInput;
	private FileOutputStream fileWriter;
 
	public static JavaTracerConfiguration getInstance(){
		if (instance==null) 
			return instance = new JavaTracerConfiguration();
		else 
			return instance;
	}
	
    private JavaTracerConfiguration() {

    	/*
    	 * Create a file configuration
    	 */

    	this.properties = new Properties();
    	
    	File f = new File(CONFIG_FILE_NAME);
    	
    	if (f.exists()){
    		
    		try {
				this.properties.load(new FileInputStream(CONFIG_FILE_NAME));
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    	} else {	
    		try {
                	
                /*
                 * The file is uploaded to a File to use the Load method
                 */
    			
    			
    			fileWriter = new FileOutputStream(CONFIG_FILE_NAME);
                fileInput = new FileInputStream(CONFIG_FILE_NAME);
                
                /*
                 * With the method load, we can load the object properties
                 */
                
                properties.load(fileInput);
                
                /*
                * Now we add information to the properties through a Hashmap
                */
               
                properties.putAll(defaultConfig());
                
                properties.store(fileWriter,FILE_TITLE);
    			fileWriter.close();
    			fileInput.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    		
    	}
        
    }
 
    private HashMap<String, String> defaultConfig() {
    	HashMap<String, String> map= new HashMap<>();
    	map.put(EXCLUDES, "java.*,javax.*,sun.*,com.sun.*");
        return map;
	}
    
    public void addExcludes(String[] newExcludes){
    	
    	List<String> currentExcludes = new ArrayList<String>(Arrays.asList(getExcludes()));
    	
    	for (int i=0;i<newExcludes.length;i++){
    		if (!currentExcludes.contains(newExcludes[i])){
    			currentExcludes.add(newExcludes[i]);
    		}
    	}
    	
    	String value = "";
    	for (int i=0;i<currentExcludes.size();i++){
    		value += currentExcludes.get(i);
    		if (i<(currentExcludes.size()-1))
    			value += ",";    		
    	}
    	
    	System.out.println(value);
    	properties.setProperty(EXCLUDES,value);
    	storePropierties();
    		
    }

	private void storePropierties() {
		
     	try {
			fileWriter = new FileOutputStream(CONFIG_FILE_NAME);
			properties.store(fileWriter,FILE_TITLE);
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
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