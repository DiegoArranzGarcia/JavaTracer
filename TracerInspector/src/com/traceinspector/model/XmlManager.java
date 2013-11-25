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

import com.javatracer.model.info.ArgumentInfo;
import com.javatracer.model.info.ArrayInfo;
import com.javatracer.model.info.MethodEntryInfo;
import com.javatracer.model.info.MethodExitInfo;
import com.javatracer.model.info.ObjectInfo;
import com.thoughtworks.xstream.XStream;
import com.traceinspector.datamodel.MethodNode;

public class XmlManager {
 
   public final String OUTPUT_NAME = "output.xml";
	   
   private Document xmlDocument;
   private XStream xStream;
	   
   public XmlManager(String fileName) {
	   try {
		   xStream = new XStream();
		   xStream.alias("array",ArrayInfo.class);
		   xStream.alias("methodEntryEvent",MethodEntryInfo.class);
		   xStream.alias("methodExitEvent",MethodExitInfo.class);
		   xStream.alias("object",ObjectInfo.class);
		   xStream.alias("argument",ArgumentInfo.class);
		   xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("C:\\Users\\Diego\\Documents\\GitHub\\JavaTracer\\JavaTracer\\" + fileName);
	   } catch (Exception e){
		   e.printStackTrace();
	   }
   }
	   
   public void read(){  
     try {
       String expression = "/trace/*[1]";  
       XPath xPath = XPathFactory.newInstance().newXPath();
       XPathExpression xPathExpression = xPath.compile(expression);
       Node node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
       do {
         node = node.getNextSibling();      
       } while (node != null);
   
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

	public MethodNode getMethodInfoFromNode(Node node) {
		MethodNode info = null;
		try{
			String expression = "./methodEntryEvent";  
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    XPathExpression xPathExpression = xPath.compile(expression);
		    Node infoNode = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
		    info= createMethodNode(infoNode);
		} catch (Exception e){
			e.printStackTrace();
		}
		return info;
	}

	private MethodNode createMethodNode(Node infoNode) throws XPathExpressionException {
		String expression = "./methodName";  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    String methodName = (String) xPathExpression.evaluate(infoNode,XPathConstants.STRING);
		MethodNode info = new MethodNode(methodName);
		info.setVariables(loadArgumets(infoNode));
		return info;
	}

	private List<ArgumentInfo> loadArgumets(Node infoNode) throws XPathExpressionException{
		List<ArgumentInfo> arguments = new ArrayList<>();
		String expression = "./arguments/*";  
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    XPathExpression xPathExpression = xPath.compile(expression);
	    NodeList argumentsXml = (NodeList) xPathExpression.evaluate(infoNode,XPathConstants.NODESET);
	    for (int i=0;i<argumentsXml.getLength();i++){
	    	arguments.add(getArgument(argumentsXml.item(i)));
	    }
		return arguments;
	}

	private ArgumentInfo getArgument(Node node) {
		ArgumentInfo info = (ArgumentInfo) xStream.fromXML(nodeToString(node));
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
}
