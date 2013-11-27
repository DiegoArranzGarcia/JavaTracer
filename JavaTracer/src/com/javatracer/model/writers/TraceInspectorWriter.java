package com.javatracer.model.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
import com.javatracer.model.data.MethodInfo;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.VariableInfo;
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
		List<VariableInfo> arguments = getArguments(node);
		List<VariableInfo> thisObject = getThis(node);
		
		MethodInfo info = new MethodInfo(name,className,arguments,thisObject);
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

	private List<VariableInfo> getThis(Node node) throws Exception {
		String expression = "./" + XStreamWriter.TAG_METHOD_ENTRY_EVENT + "/argumentsThis/variable" ;  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		NodeList thisArguments = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		
		List<VariableInfo> thisArgumentList = new ArrayList<>();
		
		for (int i=0;i<thisArguments.getLength();i++){
			String nodeString = nodeToString(thisArguments.item(i));
			VariableInfo variable = (VariableInfo) xStream.fromXML(nodeString);
			thisArgumentList.add(variable);
		}
		
		return thisArgumentList;		
	}

	@SuppressWarnings("unchecked")
	private List<VariableInfo> getArguments(Node node) throws Exception {
		String expression = "./" + XStreamWriter.TAG_METHOD_ENTRY_EVENT + "/arguments/*";  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		NodeList arguments = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		
		List<VariableInfo> argumentList = new ArrayList<>();
		
		for (int i=0;i<arguments.getLength();i++){
			String nodeString = nodeToString(arguments.item(i));
			VariableInfo variable = (VariableInfo) xStream.fromXML(nodeString);
			argumentList.add(variable);
		}
		
		return argumentList;
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
		xStream.alias(TAG_METHOD_INFO, MethodInfo.class);
		
		xStream.alias(XStreamWriter.TAG_ARRAY,ArrayInfo.class);
		xStream.alias(XStreamWriter.TAG_OBJECT,ObjectInfo.class);
		xStream.alias(XStreamWriter.TAG_VARIABLE,VariableInfo.class);
		xStream.alias(XStreamWriter.TAG_NULL,NullObject.class);
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
	
	private String nodeToString(Node node) {

		StringWriter sw = new StringWriter();
		String nodeString;
		try {
			 Transformer t = TransformerFactory.newInstance().newTransformer();
			 t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			 t.transform(new DOMSource(node), new StreamResult(sw));
			 nodeString = sw.toString();
			 nodeString = nodeString.replaceAll("\r\n\\s*", "");
		} catch (TransformerException e) {
			nodeString = "error";
		}

		return nodeString;
	}

}
