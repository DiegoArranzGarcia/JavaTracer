package com.javatracer.model.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

import com.javatracer.model.data.ArgumentInfo;
import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.MethodInfo;
import com.javatracer.model.data.ObjectInfo;
import com.thoughtworks.xstream.XStream;

public class TraceInspectorWriter {
	
	private static boolean DELETE_TMP_TRACE = false;
	private static String NAME_FILE = "trace.xml";
	
	private static String TAG_CHANGES = "changes";
	private static String TAG_METHOD_INFO = "info";
	
	private XStream xStream;
	private BufferedWriter bufferedWriter;
	private Document xmlDocument;
	private XPath xPath;
	
	public TraceInspectorWriter() {
		
		try {
			FileWriter fileWriter = new FileWriter(NAME_FILE);
			this.bufferedWriter = new BufferedWriter(fileWriter);
			
			this.xStream = new XStream();
			addAlias();
			
			xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XStreamWriter.FILE_NAME);
			xPath = XPathFactory.newInstance().newXPath();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void generateFinalTrace() {
		try{
			
			write(XStreamWriter.TAG_XML);
			write(startTag(XStreamWriter.TAG_TRACE));		
			
			Node root = getRootNode();
			generateXml(root);
			
			write(endTag(XStreamWriter.TAG_TRACE));
			
			bufferedWriter.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Node getRootNode() {
		Node node = null;
		
		try {
			
			String expression = "/" + XStreamWriter.TAG_TRACE + "/" + XStreamWriter.TAG_METHOD;  
			
			XPathExpression xPathExpression = xPath.compile(expression);
			node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return node;
	}
	
	private void generateXml(Node node) throws Exception{
		
		//Write <method-call>
		write(startTag(XStreamWriter.TAG_METHOD)); 
				
		String name = getNameMethod(node);
		String className = getCalledFromClass(node);
		getArguments(node);
		getThis(node);
		
		MethodInfo info = new MethodInfo(name,className);
		xStream.toXML(info,bufferedWriter);
		write("");
						
		//processChanges(getChanges(node));
		
		write(startTag(XStreamWriter.TAG_CALLED_METHODS));
		
		NodeList childs = getCalledMethods(node);
		
		for (int i=0;i<childs.getLength();i++){
			generateXml(childs.item(i));
		}
		
		write(endTag(XStreamWriter.TAG_CALLED_METHODS));
		
		write(endTag(XStreamWriter.TAG_METHOD));
		
	}

	private NodeList getCalledMethods(Node node) throws Exception {
		String expression = "./" + XStreamWriter.TAG_CALLED_METHODS + "/*";  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		NodeList calledMethods = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		return calledMethods;
	}

	private void processChanges(List<Object> changes) throws IOException {
		
		if (changes != null && changes.size()!=0){
			
			write(startTag(TAG_CHANGES));
			
			
			
			write(endTag(TAG_CHANGES));
			
		}
	}

	private List<Object> getChanges(Node node) {
		return null;
	}

	private void getThis(Node node) {
				
	}

	private void getArguments(Node node) {
				
	}

	private String getCalledFromClass(Node node) throws XPathExpressionException {
		
		String expression = "./" + XStreamWriter.TAG_METHOD_ENTRY_EVENT + "/calledFromClass";  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		String className = (String) xPathExpression.evaluate(node,XPathConstants.STRING);
		
		return className;
	}

	private String getNameMethod(Node node) throws Exception {
		
		String expression = "./" + XStreamWriter.TAG_METHOD_ENTRY_EVENT + "/methodName";  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		String name = (String) xPathExpression.evaluate(node,XPathConstants.STRING);
		
		return name;
	}

	private void addAlias() {
		xStream.alias("array",ArrayInfo.class);
		xStream.alias(TAG_METHOD_INFO, MethodInfo.class);
		xStream.alias("object",ObjectInfo.class);
		xStream.alias("argument",ArgumentInfo.class);
	}
	
	private void write(String string) throws IOException {
		bufferedWriter.write(string + "\n");
	}

	private String startTag(String tag){
		return "<" + tag + ">";
	}

	private String endTag(String tag) {
		return "</" + tag + ">";
	}

}
