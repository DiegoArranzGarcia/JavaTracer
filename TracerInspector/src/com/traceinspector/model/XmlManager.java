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

import com.javatracer.model.methods.data.MethodEntryInfo;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.variables.data.ArrayData;
import com.javatracer.model.variables.data.IgnoredData;
import com.javatracer.model.variables.data.NullData;
import com.javatracer.model.variables.data.ObjectData;
import com.javatracer.model.variables.data.SimpleData;
import com.javatracer.model.variables.data.StringData;
import com.thoughtworks.xstream.XStream;

public class XmlManager {
    
   public static String TAG_TRACE = "trace";
   public static String TAG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
   public static String TAG_METHOD = "method-call";
   public static String TAG_CALLED_METHODS = "called-methods";
   public static String TAG_METHOD_ENTRY_EVENT = "method-entry-event";
   public static String TAG_METHOD_EXIT_EVENT = "method-exit-event";
   public static String TAG_SIMPLE_DATA = "simple-variable";
   public static String TAG_OBJECT = "object-variable";
   public static String TAG_ARRAY = "array-variable";
   public static String TAG_STRING = "string-variable";
   public static String TAG_NULL = "null";
   public static String TAG_IGNORED = "ignored";
   public static String TAG_FIELDS = "fields";
   public static String TAG_CONTENT = "content";
   public static String TAG_THIS = "object-this";
	   
   private Document xmlDocument;
   private XStream xStream;
	   
   public XmlManager(String fileName) {
	   try {
		   xStream = new XStream();
		   
		   addAlias(); 
		
		   xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
	   } catch (Exception e){
		   e.printStackTrace();
	   }
   }

   private void addAlias() {
		xStream.alias(TAG_METHOD_ENTRY_EVENT,MethodEntryInfo.class);
		xStream.alias(TAG_METHOD_EXIT_EVENT,MethodExitInfo.class);
		xStream.aliasField(TAG_THIS, MethodEntryInfo.class,"objectThis");
		xStream.aliasField(TAG_THIS, MethodExitInfo.class,"objectThis");
		xStream.aliasField(TAG_CONTENT, ArrayData.class,"value");
		xStream.aliasField(TAG_FIELDS, ObjectData.class,"value");
		xStream.alias(TAG_SIMPLE_DATA, SimpleData.class);
		xStream.alias(TAG_OBJECT, ObjectData.class);
		xStream.alias(TAG_IGNORED, IgnoredData.class);
		xStream.alias(TAG_ARRAY, ArrayData.class);
		xStream.alias(TAG_STRING,StringData.class);
		xStream.alias(TAG_NULL,NullData.class);
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
	
	public long getIdFromNode(Node infoNode) throws Exception {
		String id = infoNode.getAttributes().getNamedItem("id").getNodeValue();
		return Long.parseLong(id);
	}

	public String getName(Node infoNode) throws Exception{
		String expression = "./info/name";  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    String methodName = (String) xPathExpression.evaluate(infoNode,XPathConstants.STRING);
	    return methodName;
	}

	public List<Object> loadArguments(Node infoNode) throws XPathExpressionException{
		List<Object> arguments = new ArrayList<>();
		String expression = "./info/arguments/*";  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    NodeList argumentsXml = (NodeList) xPathExpression.evaluate(infoNode,XPathConstants.NODESET);
	    for (int i=0;i<argumentsXml.getLength();i++){
	    	arguments.add(getArgument(argumentsXml.item(i)));
	    }
		return arguments;
	}

	private Object getArgument(Node node) {
		Object info = xStream.fromXML(nodeToString(node));
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
	
	public boolean haveChildrenOfNode(Node node) {
		boolean childs = false;
		try{
			String expression = "./called-methods/method-call";  
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    XPathExpression xPathExpression = xPath.compile(expression);
		    childs = (boolean) xPathExpression.evaluate(node,XPathConstants.BOOLEAN);
		} catch (Exception e){
			e.printStackTrace();
		}
		return childs;
	}

	public Object loadReturnValue(Node node) {
		return null;
	}

	public Object loadThisValue(Node node) {
		return null;
	}

}
