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

import com.general.model.FileUtilities;
import com.general.model.XStreamUtil;
import com.profiler.model.data.ExcludedClassesMethods;
import com.thoughtworks.xstream.XStream;


public class JavaTracerConfiguration extends XStreamUtil{

	public final static String CONFIG_FILE_NAME = "java-tracer-properties";
	/*
	 * Defaults Tracer
	 */
	public final static String JAVA = "java.*";
	public final static String JAVAX = "javax.*";
	public final static String SUN ="sun.*";
	public final static String COM_SUN = "com.sun.*";
	public final static String JARS = "org.eclipse.jdt.internal.jarinjarloader.*";
	/*
	 * Defaults Display tree
	 */
	public final static int DEFAULT_NUM_LEVELS = 4;
	public final static int DEFAULT_NUM_NODES  = 30;


	/*
	 * General
	 */

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
	private boolean excludedThis;
	private boolean excludedDataStructure;
	

	/*
	 * Inspector
	 */
	private int numlevels;
	private int numNodes;
	private boolean unlimitedLevels;
	private boolean unlimitedNodes;

	public List<String> getExcludesList() {
		return excludesList;
	}

	public void setExcludesList(List<String> excludesList) {
		this.excludesList = excludesList;
	}

	public ExcludedClassesMethods getExcludedClassesMethods() {
		return excludedClassesMethods;
	}

	public void setExcludedClassesMethods(
			ExcludedClassesMethods excludedClassesMethods) {
		this.excludedClassesMethods = excludedClassesMethods;
	}

	public int getNumNodes() {
		return numNodes;
	}

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
		this.fileXml = new File(CONFIG_FILE_NAME + FileUtilities.EXTENSION_XML);		

		if (!fileXml.exists()) {
			/*
			 *If the file does not exist, create a new one with the default settings
			 */
			initExcludes();
			this.excludedThis = false;
			this.excludedDataStructure = false;
			this.excludedClassesMethods = new ExcludedClassesMethods();

			this.unlimitedLevels = false;
			this.unlimitedNodes = false;
			this.numlevels = DEFAULT_NUM_LEVELS;;
			this.numNodes = DEFAULT_NUM_NODES;

			generateFile();
			
			try {
				xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FileUtilities.EXTENSION_XML);
				xPath = XPathFactory.newInstance().newXPath();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}else {
			try {
				try {
					xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FileUtilities.EXTENSION_XML);
					xPath = XPathFactory.newInstance().newXPath();
				}
				catch (Exception e){
					e.printStackTrace();
				}
				
				excludesList = getExludesFromFile();
				excludedThis = getExcludedThisFromFile();
				excludedDataStructure = getExcludedDataStructureFromFile();
				excludedClassesMethods = getExcludesClassesMethodFromFile();

				unlimitedLevels = getUnlimitedLevelsFromFile();
				unlimitedNodes = getUnlimitedNodesFromFile();
				numlevels =  getNumLevelsFromFile();
				numNodes = getNumNodesFromFile();


			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}


	}

	private void initExcludes() {
		excludesList = new ArrayList<>();
		excludesList.add(JAVA);
		excludesList.add( JAVAX);
		excludesList.add(SUN);
		excludesList.add(COM_SUN);    
		excludesList.add(JARS);
	}


	/**
	 * Write in xml file the configuration
	 */
	private void generateFile() { 

		try {
			writer = new FileWriter(fileXml);
			write(startTag(TAG_XML));
			write(startTag(TAG_CONFIGURATION));	

			write(startTag(TAG_EXCLUDES));	
			writeExcluded();
			write(endTag(TAG_EXCLUDES));

			write(startTag(TAG_EXCLUDED_THIS));
			writeExcludedThis();
			write(endTag(TAG_EXCLUDED_THIS));

			write(startTag(TAG_METHODS_EXCLUDES));
			writeExcludedClassesMethods();
			write(endTag(TAG_METHODS_EXCLUDES));

			write(startTag(TAG_EXCLUDED_DATA_STRUCTURE));
			writeExcludedDataStructures();
			write(endTag(TAG_EXCLUDED_DATA_STRUCTURE));			

			write(startTag(TAG_UNLIMITED_LEVELS));
			writeUnlimitedLevels();
			write(endTag(TAG_UNLIMITED_LEVELS));

			write(startTag(TAG_UNLIMITED_NODES));
			writeUnlimitedNodes();
			write(endTag(TAG_UNLIMITED_NODES));	

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

	/*
	 * Write in the file the configuration : Tracer
	 */
	private void writeExcluded() {
		try {
			writeXStream(excludesList);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void writeExcludedClassesMethods() {
		try {
			writeXStream(excludedClassesMethods);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	private void writeExcludedThis() {
		try {
			writeXStream(excludedThis);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void writeExcludedDataStructures() {
		try {
			writeXStream(excludedDataStructure);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Write in file the configuration: Display tree
	 */

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

	private void writeUnlimitedLevels() {
		try {
			writeXStream(unlimitedLevels);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void writeUnlimitedNodes() {
		try {
			writeXStream(unlimitedNodes);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}	


	/*
	 * Get Information from file xml : Tracer
	 */
	@SuppressWarnings("unchecked")
	private List<String> getExludesFromFile() {
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
	
	private boolean getExcludedThisFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_EXCLUDED_THIS;  

		boolean  excluded_this =false;
		String excluded_this_query ="";
		try {
			XPathExpression xPathExpression = xPath.compile(expression);
			Node node_excluded_this = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			excluded_this_query =  (String) xPathExpression.evaluate(node_excluded_this,XPathConstants.STRING);
			String result_query = excluded_this_query.replaceAll("\n", ""); 
			excludedThis = Boolean.parseBoolean(result_query);	       
		}
		catch (Exception ex) {
			return excluded_this;
		}	

		return excludedThis;
	}

	private boolean getExcludedDataStructureFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_EXCLUDED_DATA_STRUCTURE;  

		boolean  excluded_data_structure =false;
		String unlimited_query ="";
		try {
			XPathExpression xPathExpression = xPath.compile(expression);
			Node node_excluded_this = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			unlimited_query =  (String) xPathExpression.evaluate(node_excluded_this,XPathConstants.STRING);
			String result_query = unlimited_query.replaceAll("\n", ""); 
			excludedDataStructure = Boolean.parseBoolean(result_query);	       
		}
		catch (Exception ex) {
			return excluded_data_structure;
		}	

		return excludedDataStructure;
	}
	
	/*
	 * Get Information from file xml : Display-tree
	 */

	private int getNumLevelsFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_NUM_LEVELS;  

		String nLevels ="4";
		int numLevels = DEFAULT_NUM_LEVELS;
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


	private boolean getUnlimitedLevelsFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_UNLIMITED_LEVELS;  

		boolean  unlimited_levels =false;
		String unlimited_query ="";
		try {
			XPathExpression xPathExpression = xPath.compile(expression);
			Node node_excluded_this = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			unlimited_query =  (String) xPathExpression.evaluate(node_excluded_this,XPathConstants.STRING);
			String result_query = unlimited_query.replaceAll("\n", ""); 
			unlimitedLevels = Boolean.parseBoolean(result_query);	       
		}
		catch (Exception ex) {
			return unlimited_levels;
		}	

		return unlimitedLevels;
	}

	private boolean getUnlimitedNodesFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_UNLIMITED_NODES;  

		boolean  unlimited_nodes =false;
		String unlimited_query ="";
		try {
			XPathExpression xPathExpression = xPath.compile(expression);
			Node node_excluded_this = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			unlimited_query =  (String) xPathExpression.evaluate(node_excluded_this,XPathConstants.STRING);
			String result_query = unlimited_query.replaceAll("\n", ""); 
			unlimitedNodes = Boolean.parseBoolean(result_query);	       
		}
		catch (Exception ex) {
			return unlimited_nodes;
		}	

		return unlimitedNodes;
	}

	private int getNumNodesFromFile() {
		String expression = "/" +TAG_CONFIGURATION+"/" + TAG_NUM_NODES;  
		int numNodes = DEFAULT_NUM_NODES;

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

	/*
	 * General
	 */
	private void write(String string) throws IOException {
		writer.write(string + "\n");
	}

	private void writeXStream(Object object) throws Exception{
		String string = xStream.toXML(object);
		write(string);
	}

	public void saveConfiguration() { 
		this.xStream = new XStream();
		fileXml = new File(CONFIG_FILE_NAME + FileUtilities.EXTENSION_XML);
		generateFile();

		try {
			xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(CONFIG_FILE_NAME + FileUtilities.EXTENSION_XML);
			xPath = XPathFactory.newInstance().newXPath();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * Getters and Setters
	 */
	public int getNumLevels() {
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

	public boolean isExcludedThis() {
		return excludedThis;
	}

	public void setExcludedThis(boolean excludedThis) {
		this.excludedThis = excludedThis;
	}

	public boolean isUnlimitedLevels() {
		return unlimitedLevels;
	}

	public void setUnlimitedLevels(boolean unlimitedLevels) {
		this.unlimitedLevels = unlimitedLevels;
	}

	public boolean isUnlimitedNodes() {
		return unlimitedNodes;
	}

	public void setUnlimitedNodes(boolean unlimitedNodes) {
		this.unlimitedNodes = unlimitedNodes;
	}

	public boolean isExcludedDataStructure() {
		return excludedDataStructure;
	}

	public void setExcludedDataStructure(boolean excludedDataStructure) {
		this.excludedDataStructure = excludedDataStructure;
	}

	public List<String> getDefaultExcluded(){
		List<String>excludesList = new ArrayList<>();
		excludesList.add(JAVA);
		excludesList.add( JAVAX);
		excludesList.add(SUN);
		excludesList.add(COM_SUN);    
		excludesList.add(JARS);
		return excludesList;
	}

}
