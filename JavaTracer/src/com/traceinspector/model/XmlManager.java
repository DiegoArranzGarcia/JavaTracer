package com.traceinspector.model;

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

import com.general.model.variables.data.Data;
import com.javatracer.model.writers.XStreamUtil;
import com.thoughtworks.xstream.XStream;

public class XmlManager extends XStreamUtil{
      
   private Document xmlDocument;
   private XStream xStream;
	   
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
			String expression = "/" + TAG_TRACE + "/" + TAG_METHOD;  
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpression = xPath.compile(expression);
			node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		} catch (Exception e){
			e.printStackTrace();
		}
		return node;
	}
	
	public long getIdFromNode(Node infoNode) throws Exception {
		String id = infoNode.getAttributes().getNamedItem(ATTR_ID).getNodeValue();
		return Long.parseLong(id);
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

	public NodeList getChildsOfNode(Node node) {
		NodeList childs = null;
		try{
			String expression = "./" + TAG_CALLED_METHODS +"/" + TAG_METHOD;  
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    XPathExpression xPathExpression = xPath.compile(expression);
		    childs = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		} catch (Exception e){
			e.printStackTrace();
		}
		return childs;
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
	
	public boolean haveChildrenOfNode(Node node) {
		boolean childs = false;
		try{
			String expression = "./" + TAG_CALLED_METHODS +"/" + TAG_METHOD;  
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    XPathExpression xPathExpression = xPath.compile(expression);
		    childs = (boolean) xPathExpression.evaluate(node,XPathConstants.BOOLEAN);
		} catch (Exception e){
			e.printStackTrace();
		}
		return childs;
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

}
