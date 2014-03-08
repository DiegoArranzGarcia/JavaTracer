/**
 * 
 */
package com.general.model.configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import com.general.model.XStreamUtil;
import com.thoughtworks.xstream.XStream;


public class JavaTracerConfigurationXml extends XStreamUtil{
	private final static String CONFIG_FILE_NAME = "java-tracer-properties";
	/*
	 * General
	 */
	private String FILE_EXT = ".xml";
	private XStream xStream;
	private Document xmlDocument;
	private XPath xPath;	
	private static JavaTracerConfigurationXml instance;
	private File fileXml;
	private FileWriter writer;
	/*
	 * Tracer
	 */
	private List<String>excludesList;
	/*
	 * Inspector
	 */
	private int numlevels;
	private int numNodes;
	
	public static JavaTracerConfigurationXml getInstance(){
		/*
		 * Instance class
		 */
		if (instance==null) 
			return instance = new JavaTracerConfigurationXml();
		else 
			return instance;
	}
	
	 private JavaTracerConfigurationXml() {
		/*
		 * Create default file xml
		 */
		try {
	        xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FILE_EXT);
	        xPath = XPathFactory.newInstance().newXPath();
        }
		catch (Exception e){
			e.printStackTrace();
		}
		
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
	

	public List<String> getExludes() throws Exception{
		String expression = "./" + TAG_EXCLUDES+"/*"; 
		XPathExpression xPathExpression = xPath.compile(expression);
		NodeList excludes = (NodeList) xPathExpression.evaluate(xmlDocument,XPathConstants.NODESET);
		for (int i=0;i<excludes.getLength();i++) {
			String nodeString = nodeToString(excludes.item(i));
			String exclude = (String) xStream.fromXML(nodeString);
			excludesList.add(exclude);
		};
		return excludesList;
	}
	
	public int getNumLevels() throws Exception {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_NUM_LEVELS;  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		Node node_levels = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		
		String nLevels =  (String) xPathExpression.evaluate(node_levels,XPathConstants.STRING);
		
		String s = nLevels.replaceAll("\n", "");
		numlevels = Integer.parseInt(s); 
		
		return numlevels;
	}
	
	public int getNumNodes() throws Exception {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_NUM_NODES;  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		Node node_nodes = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		
		String nNodes =  (String) xPathExpression.evaluate(node_nodes,XPathConstants.STRING);
		
		String s = nNodes.replaceAll("\n", "");
		numNodes = Integer.parseInt(s); 
		
		return numNodes;
	}
	
	
}
