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
import com.profiler.model.data.ExcludesClassMethods;
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
	private ExcludesClassMethods excludesClassMethods,newExcludesMethods;
	private List<ExcludesClassMethods>listExcludesClassMethods;
	
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
		
		this. excludesList = new ArrayList<>();
		this.listExcludesClassMethods = new ArrayList<>();
		this.fileXml = new File(CONFIG_FILE_NAME + FILE_EXT);		
		
		this.xStream = new XStream();
		if (!fileXml.exists()) {
			/*
			 *If the file does not exist, create a new one with the default settings
			 */
			initExcludes();
			this.numlevels = 4;
			this.numNodes = 30;
			
			initExcludesMethod();
			
			generateFile(false);
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
				listExcludesClassMethods = getExcludesClassMethodFromFile();
            }
            catch (Exception ex) {
	            ex.printStackTrace();
            }
		}
	
		
	 }
	 

	private void initExcludes() {
    	excludesList.add("java.*");
    	excludesList.add("javax.*");
    	excludesList.add("sun.*");
    	excludesList.add("com.sun.*");    
    }
	
	/*
	 * Write in xml file the configuration
	 */
	private void generateFile(boolean newExcludesMethods) { 
    	
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
	    		  if (newExcludesMethods)
	    			  writeNewMethodsExcludes();
	    		  else writeMethodsExcludes();
	    		  write(endTag(TAG_METHODS_EXCLUDES));
		    		  
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

	private void writeNewMethodsExcludes() {
   	 try {
   		 for (int i =0;i<listExcludesClassMethods.size();i++){
   			 writeXStream(listExcludesClassMethods.get(i)); 
   		 }
  	        writeXStream(newExcludesMethods);
          }
          catch (Exception ex) {
  	        ex.printStackTrace();
          } 
	    
   }

	private void writeMethodsExcludes() {
   	 try {
   		 for (int i =0;i<listExcludesClassMethods.size();i++){
   			 writeXStream(listExcludesClassMethods.get(i)); 
   		 }
 	        
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
	
	   private List<ExcludesClassMethods> getExcludesClassMethodFromFile() {
		   String expression = "/" +TAG_CONFIGURATION +"/"+ TAG_METHODS_EXCLUDES + "/*"; 
		   List<ExcludesClassMethods> listExcludesClassMethods = new ArrayList<>();
		   
		   try {
	        	XPathExpression xPathExpression = xPath.compile(expression);
	        	NodeList  methodsExcludes  = (NodeList) xPathExpression.evaluate(xmlDocument,XPathConstants.NODESET);
	        	
	        	for (int i=0;i<methodsExcludes.getLength();i++) {
			 			String nodeString = nodeToString(methodsExcludes.item(i));
			 			excludesClassMethods = (ExcludesClassMethods) xStream.fromXML(nodeString); 		
			 			listExcludesClassMethods.add(excludesClassMethods);
		 		}
	        }
	        catch (Exception ex) {
	        	initExcludesMethod();
	        	return listExcludesClassMethods;
	        }	
			
			return listExcludesClassMethods;
	    }
	
	
    private void initExcludesMethod() {
	   excludesClassMethods = new ExcludesClassMethods();
	    
    }

	public void saveNewConfiguration(boolean newExcludesMethods) { 
		this.xStream = new XStream();
		 fileXml = new File(CONFIG_FILE_NAME + FILE_EXT);
		generateFile(newExcludesMethods);
		
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
	
	 public void addExcludes(List<String> newExcludes){
	    	
	    	List<String> currentExcludes = new ArrayList<>();
            try {
	            currentExcludes = getExludesFromFile();
            }
            catch (Exception ex) {
	            ex.printStackTrace();
            }
	    	
	    	for (int i=0;i<newExcludes.size();i++){
	    		if (!currentExcludes.contains(newExcludes.get(i))){
	    			currentExcludes.add(newExcludes.get(i)); 
	    		}
	    	}
	    	
	    	try {
				try {
			        xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FILE_EXT);
			        xPath = XPathFactory.newInstance().newXPath();
		        }
				catch (Exception e){
					e.printStackTrace();
				}
				excludesList = currentExcludes;
				numlevels =  getNumLevelsFromFile();
				numNodes = getNumNodesFromFile();
				listExcludesClassMethods = getExcludesClassMethodFromFile();
				saveNewConfiguration(false);
            }
            catch (Exception ex) {
	            ex.printStackTrace();
            }
	    	
	    		
	    }
	 
		public void addExcludesMethods(ExcludesClassMethods newExcludesMethods) {
			excludesList = getExludesFromFile();
			numlevels =  getNumLevelsFromFile();
			numNodes = getNumNodesFromFile();
			listExcludesClassMethods = getExcludesClassMethodFromFile();
			this.newExcludesMethods = newExcludesMethods;
			
			saveNewConfiguration(true);
			
		}

		public List<ExcludesClassMethods> getListExcludesClassMethods() {
	        return listExcludesClassMethods;
        }

		public void setListExcludesClassMethods(List<ExcludesClassMethods> listExcludesClassMethods) {
	        this.listExcludesClassMethods = listExcludesClassMethods;
        }
}
