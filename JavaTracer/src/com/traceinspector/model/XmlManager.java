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

import com.general.model.variables.data.ArrayData;
import com.general.model.variables.data.Data;
import com.general.model.variables.data.IgnoredData;
import com.general.model.variables.data.NullData;
import com.general.model.variables.data.ObjectData;
import com.general.model.variables.data.SimpleData;
import com.general.model.variables.data.StringData;
import com.javatracer.model.methods.data.MethodEntryInfo;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.writers.JavaTraceWriter;
import com.thoughtworks.xstream.XStream;

public class XmlManager {
      
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
		xStream.alias(JavaTraceWriter.TAG_METHOD_ENTRY_EVENT,MethodEntryInfo.class);
		xStream.alias(JavaTraceWriter.TAG_METHOD_EXIT_EVENT,MethodExitInfo.class);
		xStream.aliasField(JavaTraceWriter.TAG_THIS, MethodEntryInfo.class,"this_data");
		xStream.aliasField(JavaTraceWriter.TAG_THIS, MethodExitInfo.class,"this_data");
		xStream.aliasField(JavaTraceWriter.TAG_RETURN, MethodExitInfo.class,"return_data");
		xStream.aliasField(JavaTraceWriter.TAG_CONTENT, ArrayData.class,"value");
		xStream.aliasField(JavaTraceWriter.TAG_FIELDS, ObjectData.class,"value");
		xStream.alias(JavaTraceWriter.TAG_SIMPLE_DATA, SimpleData.class);
		xStream.alias(JavaTraceWriter.TAG_OBJECT, ObjectData.class);
		xStream.alias(JavaTraceWriter.TAG_IGNORED, IgnoredData.class);
		xStream.alias(JavaTraceWriter.TAG_ARRAY, ArrayData.class);
		xStream.alias(JavaTraceWriter.TAG_STRING,StringData.class);
		xStream.alias(JavaTraceWriter.TAG_NULL,NullData.class);
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

	public List<Data> loadArguments(Node infoNode) throws XPathExpressionException{
		List<Data> arguments = new ArrayList<>();
		String expression = "./info/arguments/*";  
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

	public Data loadReturnValue(Node node) throws XPathExpressionException {
		
		Data return_data = null;
		
		String expression = "./info/" + JavaTraceWriter.TAG_RETURN;  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    Node return_node = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
	    
	    if (return_node!=null)
	    	return_data = (Data) xStream.fromXML(nodeToString(return_node));
	    
	    return return_data;
	}

	public Data loadThisValue(Node node) throws XPathExpressionException {

		Data this_data = null;
		
		String expression = "./info/" + JavaTraceWriter.TAG_THIS;  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    Node return_node = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
	    this_data = (Data) xStream.fromXML(nodeToString(return_node));
	    
	    return this_data;
		
	}

}
