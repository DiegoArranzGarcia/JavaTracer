/**
 * 
 */
package com.general.model.configuration;

import java.io.*;
import java.util.*;

import com.general.model.XStreamUtil;
import com.thoughtworks.xstream.XStream;


public class JavaTracerConfigurationXml extends XStreamUtil{
	private final static String CONFIG_FILE_NAME = "java-tracer-properties";
	private final static String EXCLUDES = "excludes";
	private final static String FILE_TITLE = "Config JavaTracer";
	
	private String FILE_EXT = ".xml";
	private XStream xStream;
	
	private static JavaTracerConfigurationXml instance;
	
	private Properties properties;
	private FileInputStream fileInput;
	private FileOutputStream fileWriter;
	private File fileXml;
	private FileWriter writer;
	private List<String>excludesList;
	private int numlevels;
	private int numNodes;
	
	public static JavaTracerConfigurationXml getInstance(){
		if (instance==null) 
			return instance = new JavaTracerConfigurationXml();
		else 
			return instance;
	}
	
	 private JavaTracerConfigurationXml() {
		
		        //xStream.toXML(TAG_INIT_CONFIGURATION,writer);
		this. excludesList = new ArrayList<>();
		initExcludes();
		
		this.numlevels = 4;
		this.numNodes = 30;
		
		 generateFile();
		 
	 }
	 
    private void initExcludes() {
    	excludesList.add("java.*");
    	excludesList.add("javax.*");
    	excludesList.add("sun.*");
    	excludesList.add("com.sun.*");    
    }

	private void generateFile() {
    	 this.xStream = new XStream();
		 fileXml = new File(CONFIG_FILE_NAME + FILE_EXT);
    	try {
    		  writer = new FileWriter(fileXml);
    		  write(startTag(TAG_CONFIGURATION));	
    		  
	    		  write(startTag(TAG_EXCLUDES));	
	    		  writeExcludes();
	    		  write(endTag(TAG_EXCLUDES));
	    		  
	    		  write(startTag(TAG_NUM_LEVELS));	
	    		  writeNumLevels();
	    		  write(endTag(TAG_NUM_LEVELS));
	    		  
	    		  write(startTag(TAG_NUM_NODES));	
	    		  writeNumNodes();
	    		  write(endTag(TAG_NUM_NODES));
	    		  
    		  write(endTag(TAG_CONFIGURATION)); 
    		  writer.close();
        }
        catch (IOException ex) {
	        ex.printStackTrace();
        } 
	    
    }

    private void writeNumNodes() {
    	 try {
 	        writeXStream(numNodes);
         }
         catch (Exception ex) {
 	        ex.printStackTrace();
         } 
    }

	private void writeNumLevels() {
    	 try {
	        writeXStream(numlevels);
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        } 
	    
    }

	private void writeExcludes() {
		try {
	        writeXStream(excludesList);
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        }
	    
    }

	private void write(String string) throws IOException {
			writer.write(string + "\n");
		}
	
	protected String startTag(String tag){
		return "<" + tag + ">";
	}

	protected String endTag(String tag) {
		return "</" + tag + ">";
	}
	
	private void writeXStream(Object object) throws Exception{
		String string = xStream.toXML(object);
		write(string);
	}
}
