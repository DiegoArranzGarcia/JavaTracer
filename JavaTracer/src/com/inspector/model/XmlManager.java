package com.inspector.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.general.model.XStreamUtil;
import com.general.model.data.MethodInfo;
import com.general.model.data.ThreadInfo;
import com.general.model.variables.data.Data;
import com.inspector.treeinspector.data.Box;
import com.tracer.model.methods.data.MethodEntryInfo;
import com.tracer.model.methods.data.MethodExitInfo;

public class XmlManager extends XStreamUtil{
      
   private Document xmlDocument;
   
   public XmlManager(String fileName) {
	   
	   try {	
		   xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
	   } catch (Exception e){
		   e.printStackTrace();
	   }
   }
   
   public NodeList getChilds(Node node) {
	   return node.getChildNodes();
	}

	public Node getInfo(Node node) {
		return node.getFirstChild();
	}

	public Node getRootNode() {
		Node node = null;
		try {
			String expression = "/" + TAG_TRACE + "/" + TAG_THREAD;  
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpression = xPath.compile(expression);
			node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		} catch (Exception e){
			e.printStackTrace();
		}
		return node;
	}

	public String getName(Node infoNode) throws Exception{
		String expression = "./" + TAG_METHOD_INFO +"/" + TAG_METHOD_NAME;  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    String methodName = (String) xPathExpression.evaluate(infoNode,XPathConstants.STRING);
	    return methodName;
	}

	public List<Data> loadArguments(Node infoNode) throws XPathExpressionException{
		List<Data> arguments = new ArrayList<>();
		String expression = "./" + TAG_METHOD_INFO +"/" + TAG_ARGUMENTS + "/*";  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    NodeList argumentsXml = (NodeList) xPathExpression.evaluate(infoNode,XPathConstants.NODESET);
	    for (int i=0;i<argumentsXml.getLength();i++){
	    	arguments.add(getArgument(argumentsXml.item(i)));
	    }
		return arguments;
	}

	private Data getArgument(Node node) {
		Data info = (Data) xStream.fromXML(nodeToString(node));
		return info;
	}
	
	public Node getNode(long id) {
		
		Node node = null;
		
		try{
			String expression = ".//" + TAG_METHOD +"[@id=" + id + "]";  
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    XPathExpression xPathExpression = xPath.compile(expression);
		    node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		    		    
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return node;
	}

	public Data loadReturnValue(Node node) throws XPathExpressionException {
		
		Data return_data = null;
		
		String expression = "./" + TAG_METHOD_INFO +"/" + TAG_RETURN;  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    Node return_node = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
	    
	    if (return_node!=null)
	    	return_data = (Data) xStream.fromXML(nodeToString(return_node));
	    
	    return return_data;
	}

	public Data loadThisValue(Node node) throws XPathExpressionException {

		Data this_data = null;
		
		String expression = "./" + TAG_METHOD_INFO + " /" + TAG_THIS;  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    Node return_node = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
	    this_data = (Data) xStream.fromXML(nodeToString(return_node));
	    
	    return this_data;
		
	}

	public ThreadInfo getThreadName(Node infoNode) throws XPathExpressionException {
		String expression = "./" + TAG_THREAD_INFO   ;  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    Node node = (Node) xPathExpression.evaluate(infoNode,XPathConstants.NODE);
	    ThreadInfo thread = (ThreadInfo) xStream.fromXML(nodeToString(node)); 
	    return thread;
	}

	public int getNumChildrenOfNode(Box box) {
		int children = 0;
		try{
			String expression = "count(" + box.getPath() + "/" + TAG_CALLED_METHODS+"/*)";
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpression = xPath.compile(expression);
			children = ((Double) xPathExpression.evaluate(xmlDocument,XPathConstants.NUMBER)).intValue();
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return children;
	}

	public MethodInfo getBoxFromNode(String path) {
		MethodInfo info = null;
		try {
			String expression = path + "/" + TAG_METHOD_ENTRY_EVENT; 
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpression;
			xPathExpression = xPath.compile(expression);
			Node node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			
			MethodEntryInfo entry = (MethodEntryInfo) xStream.fromXML(nodeToString(node)); 
			
			expression = path + "/" + TAG_METHOD_EXIT_EVENT;  
		    xPathExpression = xPath.compile(expression);
		    node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		    
		    MethodExitInfo exit = null;
		    try {
		    	exit = (MethodExitInfo) xStream.fromXML(nodeToString(node));
		    } catch (Exception e){
		    	//There where no exit method
		    }
			
			info = new MethodInfo(entry,exit);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return info;
	}
	
	public long getIdFromNode(String path) {
		long nodeID = -1;
		try {
			String expression = path + "/@" + ATTR_ID ; 
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpression;
			xPathExpression = xPath.compile(expression);
			nodeID = ((Double) xPathExpression.evaluate(xmlDocument,XPathConstants.NUMBER)).intValue();
		} catch (Exception e){
			e.printStackTrace();
		}
		return nodeID;
	}

	public boolean hasChildrenNode(String path) {
		boolean exist = false;
		try {
			String expression = path + "/" + TAG_CALLED_METHODS + "/" + TAG_METHOD + "[1]";
			XPath xPath = XPathFactory.newInstance().newXPath();
			exist= (boolean) xPath.evaluate(expression,xmlDocument,XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return exist;
	}

	public String getPath(Box box, int i) {
		return box.getPath() + "/" + TAG_CALLED_METHODS+"/"+TAG_METHOD+"["+i+"]";
	}

	public boolean exist(String path){
		boolean exist = false;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			exist= (boolean) xPath.evaluate(path,xmlDocument,XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return exist;
	}

}
