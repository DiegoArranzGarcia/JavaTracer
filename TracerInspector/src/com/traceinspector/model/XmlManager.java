package com.traceinspector.model;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.MethodEntryInfo;
import com.javatracer.model.data.MethodExitInfo;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.VariableInfo;
import com.thoughtworks.xstream.XStream;
import com.traceinspector.datamodel.MethodNode;

public class XmlManager {
    
   public static String TAG_TRACE = "trace";
   public static String TAG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
   public static String TAG_METHOD = "method-call";
   public static String TAG_CALLED_METHODS = "called-methods";
   public static String TAG_METHOD_ENTRY_EVENT = "method-entry-event";
   public static String TAG_METHOD_EXIT_EVENT = "method-exit-event";
   public static String TAG_VARIABLE = "variable";
   public static String TAG_OBJECT = "object-info";
   public static String TAG_ARRAY = "array";
   public static String TAG_NULL = "null";
	   
   private Document xmlDocument;
   private XStream xStream;
	   
   public XmlManager(String fileName) {
	   try {
		   xStream = new XStream();
			xStream.alias(TAG_METHOD_ENTRY_EVENT,MethodEntryInfo.class);
			xStream.alias(TAG_METHOD_EXIT_EVENT,MethodExitInfo.class);
			
			xStream.alias(TAG_VARIABLE,VariableInfo.class);
			xStream.alias(TAG_OBJECT, ObjectInfo.class);
			xStream.alias(TAG_ARRAY, ArrayInfo.class);
			xStream.alias(TAG_NULL,NullObject.class);
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
			String expression = "/trace/method-call";  
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpression = xPath.compile(expression);
			node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		} catch (Exception e){
			e.printStackTrace();
		}
		return node;
	}

	public MethodNode getMethodInfoFromNode(Node infoNode){
		MethodNode info = null;
		try{
			String name = getName(infoNode);
			long id = getIdFromNode(infoNode);
			info = new MethodNode(name,id);
			info.setVariables(loadArgumets(infoNode));
		} catch (Exception e){
			e.printStackTrace();
		}
		return info;
	}
	
	private long getIdFromNode(Node infoNode) throws Exception {
		String id = infoNode.getAttributes().getNamedItem("id").getNodeValue();
		return Long.parseLong(id);
	}

	private String getName(Node infoNode) throws Exception{
		String expression = "./info/name";  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    String methodName = (String) xPathExpression.evaluate(infoNode,XPathConstants.STRING);
	    return methodName;
	}

	private List<VariableInfo> loadArgumets(Node infoNode) throws XPathExpressionException{
		List<VariableInfo> arguments = new ArrayList<>();
		String expression = "./info/arguments/*";  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    NodeList argumentsXml = (NodeList) xPathExpression.evaluate(infoNode,XPathConstants.NODESET);
	    for (int i=0;i<argumentsXml.getLength();i++){
	    	arguments.add(getArgument(argumentsXml.item(i)));
	    }
		return arguments;
	}

	private VariableInfo getArgument(Node node) {
		VariableInfo info = (VariableInfo) xStream.fromXML(nodeToString(node));
		return info;
	}

	public NodeList getChildsOfNode(Node node) {
		NodeList childs = null;
		try{
			String expression = "./called-methods/method-call";  
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    XPathExpression xPathExpression = xPath.compile(expression);
		    childs = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		} catch (Exception e){
			e.printStackTrace();
		}
		return childs;
	}
	
	private String nodeToString(Node node) {

		StringWriter sw = new StringWriter();

		try {
			 Transformer t = TransformerFactory.newInstance().newTransformer();
			 t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			 t.setOutputProperty(OutputKeys.METHOD, "xml");
			 t.setOutputProperty(OutputKeys.INDENT,"no");
			 t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		String nodeString = sw.toString();
		nodeString = nodeString.replaceAll("\r\n\\s*", "");
		return nodeString;
	}

	public Node getNode(long id) {
		
		Node node = null;
		
		try{
			String expression = ".//method-call[@id=" + id + "]";  
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    XPathExpression xPathExpression = xPath.compile(expression);
		    node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
		    		    
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return node;
	}

}
