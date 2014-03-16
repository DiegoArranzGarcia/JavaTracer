/**
 * 
 */
package com.general.model.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.general.model.XStreamUtil;
import com.profiler.model.data.ExcludedClassesMethods;
import com.thoughtworks.xstream.XStream;


public class JavaTracerConfiguration extends XStreamUtil{
	private final static String CONFIG_FILE_NAME = "java-tracer-properties";
	/*
	 * General
	 */
	private String FILE_EXT = ".xml";
	private Document xmlDocument;
	private XPath xPath;	
	private static JavaTracerConfiguration instance;
	private File fileXml;
	private FileWriter writer;
	/*
	 * Tracer
	 */
	private List<String>excludesList;
	private ExcludedClassesMethods excludedClassesMethods;
	
	/*
	 * Inspector
	 */
	private int numlevels;
	private int numNodes;
	
	public static JavaTracerConfiguration getInstance(){
		/*
		 * Instance class
		 */
		if (instance==null) 
			return instance = new JavaTracerConfiguration();
		else 
			return instance;
	}
	
	 private JavaTracerConfiguration() {
		/*
		 * Create default file xml
		 */
		this.fileXml = new File(CONFIG_FILE_NAME + FILE_EXT);		
		
		if (!fileXml.exists()) {
			/*
			 *If the file does not exist, create a new one with the default settings
			 */
			initExcludes();
			this.numlevels = 4;
			this.numNodes = 30;
			this.excludedClassesMethods = new ExcludedClassesMethods();
			
			generateFile();
			try {
		        xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FILE_EXT);
		        xPath = XPathFactory.newInstance().newXPath();
	        }
			catch (Exception e){
				e.printStackTrace();
			}
		}else {
			try {
				try {
			        xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FILE_EXT);
			        xPath = XPathFactory.newInstance().newXPath();
		        }
				catch (Exception e){
					e.printStackTrace();
				}
				excludesList = getExludesFromFile();
				numlevels =  getNumLevelsFromFile();
				numNodes = getNumNodesFromFile();
				excludedClassesMethods = getExcludesClassesMethodFromFile();
            }
            catch (Exception ex) {
	            ex.printStackTrace();
            }
		}
	
		
	 }
	 

	private void initExcludes() {
		excludesList = new ArrayList<>();
    	excludesList.add("java.*");
    	excludesList.add("javax.*");
    	excludesList.add("sun.*");
    	excludesList.add("com.sun.*");    
    }
	
	/**
	 * Write in xml file the configuration
	 */
	private void generateFile() { 
    	
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
	    		  
	    		  write(startTag(TAG_METHODS_EXCLUDES));
	    		  writeExcludesClassesMethods();
	    		  write(endTag(TAG_METHODS_EXCLUDES));
		    		  
	    		  write(endTag(TAG_CONFIGURATION)); 
	    		  writer.close();
	    		  
	        }
	        catch (IOException ex) {
		        ex.printStackTrace();
	        } 
		 
   }

	private void writeExcludesClassesMethods() {
		 try {
	 	        writeXStream(numNodes);
	         }
	         catch (Exception ex) {
	 	        ex.printStackTrace();
	         } 
	}

	private void writeNumNodes() {
    	 try {
 	        writeXStream(excludedClassesMethods);
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
	
	private void writeXStream(Object object) throws Exception{
		String string = xStream.toXML(object);
		write(string);
	}
	
	/*
	 * Get Information from file xml
	 */
    @SuppressWarnings("unchecked")
    public List<String> getExludesFromFile() {
		String expression = "/" +TAG_CONFIGURATION +"/"+ TAG_EXCLUDES + "/*";  
		
        try {
        	XPathExpression xPathExpression = xPath.compile(expression);
	        NodeList excludes  = (NodeList) xPathExpression.evaluate(xmlDocument,XPathConstants.NODESET);
	         for (int i=0;i<excludes.getLength();i++) {
	 			String nodeString = nodeToString(excludes.item(i));
	 			excludesList = (List<String>) xStream.fromXML(nodeString);
	 		}
        }
        catch (Exception ex) {
        	initExcludes();
        	return excludesList;
        }	
		
		return excludesList;
	}
	
	public int getNumLevelsFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_NUM_LEVELS;  
		
		String nLevels ="4";
		int numLevels = 4;
        try {
	        	XPathExpression xPathExpression = xPath.compile(expression);
		        Node node_levels = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		        nLevels =  (String) xPathExpression.evaluate(node_levels,XPathConstants.STRING);
		        String s = nLevels.replaceAll("\n", "");
				numlevels = Integer.parseInt(s); 
        }
        catch (Exception ex) {
	        	return numLevels;
        }	
		
		return numlevels;
	}
	
	public int getNumNodesFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_NUM_NODES;  
		int numNodes = 30;
	
		Node node_nodes;
        try {
        	XPathExpression xPathExpression = xPath.compile(expression);
	        node_nodes = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
	    	String nNodes =  (String) xPathExpression.evaluate(node_nodes,XPathConstants.STRING);
			
			String s = nNodes.replaceAll("\n", "");
			numNodes = Integer.parseInt(s); 
        }
        catch (Exception ex) {
        	return numNodes;
        }		
	
		return numNodes;
	}
	
   private ExcludedClassesMethods getExcludesClassesMethodFromFile() {
	   
	   ExcludedClassesMethods excludedClassMethods = null;
	   try { 
		   String expression = "/" +TAG_CONFIGURATION +"/"+ TAG_METHODS_EXCLUDES; 
		   XPathExpression xPathExpression = xPath.compile(expression);
	   	   Node methodsExcludes  = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
	   	   excludedClassMethods = (ExcludedClassesMethods) xStream.fromXML(nodeToString(methodsExcludes));
	   } catch (Exception e){
			excludedClassMethods = new ExcludedClassesMethods();
	   }
	   
   	   return excludedClassMethods;
    }
	
	public void saveConfiguration() { 
		this.xStream = new XStream();
		fileXml = new File(CONFIG_FILE_NAME + FILE_EXT);
		generateFile();
		
		try {
	        xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FILE_EXT);
	        xPath = XPathFactory.newInstance().newXPath();
        }catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public int getNumlevels() {
		return numlevels;
	}

	public void setNumlevels(int numlevels) {
		this.numlevels = numlevels;
	}

	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}
	
	public void setExcludes(List<String>newEcludes) {
		this.excludesList = newEcludes;
		
	}
	
	public ExcludedClassesMethods getExcludeClassMethods() {
		return this.excludedClassesMethods;
	}
}
