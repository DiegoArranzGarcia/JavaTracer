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

import com.general.model.variables.data.Data;
import com.javatracer.model.ChangeDetector;
import com.javatracer.model.methods.data.ChangeInfo;
import com.javatracer.model.methods.data.MethodInfo;

public class TraceInspectorWriter extends XStreamWriter{
	
	private String FILE_EXT = ".xml";
	
	private BufferedWriter bufferedWriter;
	private Document xmlDocument;
	private XPath xPath;
	private int idNode;
	
	public TraceInspectorWriter(String nameXlm) {
		
		try {
			FileWriter fileWriter = new FileWriter(nameXlm + FILE_EXT);
			this.bufferedWriter = new BufferedWriter(fileWriter);
			
			xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(nameXlm + JavaTraceWriter.FILE_EXT);
			xPath = XPathFactory.newInstance().newXPath();
			this.idNode = 1;
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void generateFinalTrace() {
		try{
			
			write(TAG_XML);
			write(startTag(TAG_TRACE));		
			
			Node root = getRootNode();
			generateXml(root);
			
			write(endTag(TAG_TRACE));
			
			bufferedWriter.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Node getRootNode() {
		Node node = null;
		
		try {
			
			String expression = "/" + TAG_TRACE + "/" + TAG_METHOD;  
			
			XPathExpression xPathExpression = xPath.compile(expression);
			node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return node;
	}
	
	private void generateXml(Node node) throws Exception{
				
		write(startTag(TAG_METHOD + ATTR_ID + idNode + DOUBLE_QUOTES)); 
		idNode++;
		
		writeNodeInfo(node);	
		writeChanges(node);
		writeCalledMethods(node);
		
		write(endTag(TAG_METHOD));
				
	}

	private void writeNodeInfo(Node node) throws Exception {
						
		String name = getNameMethod(node);
		String className = getCalledFromClass(node);
		List<Data> arguments = getEntryArguments(node);
		Data this_data = getEntryThis(node);
		Data return_data = getReturn(node);
				
		MethodInfo info = new MethodInfo(name,className,arguments,this_data,return_data);
		writeXStream(info);
		write("");
		
	}

	private void writeCalledMethods(Node node) throws Exception{
		
		write(startTag(TAG_CALLED_METHODS));	
		
		NodeList childs = getCalledMethods(node);
		
		for (int i=0;i<childs.getLength();i++){
			generateXml(childs.item(i));
		}
		
		write(endTag(TAG_CALLED_METHODS));
		
	}

	private void writeChanges(Node node) throws Exception{
		
		write(startTag(TAG_CHANGES));
		
		List<ChangeInfo> changes = getChangesArguments(node);
		changes.addAll(getChangesThis(node));
		
		for (int i=0;i<changes.size();i++){
			writeXStream(changes.get(i));
			write("");
		}
		
		write(endTag(TAG_CHANGES));
		
	}

	private List<ChangeInfo> getChangesThis(Node node) throws Exception{
		
		Data entryThis = getEntryThis(node);
		Data exitThis = getExitThis(node);
		ChangeDetector detector = new ChangeDetector();
		List<ChangeInfo> changes = detector.getChangesBetween(entryThis, exitThis);
		
		return changes;
		
	}

	private Data getExitThis(Node node) throws Exception {
		
		String expression = "./" + TAG_METHOD_EXIT_EVENT + "/" + "TAG_THIS" ;  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		Node nodeThis = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
		
		String nodeString = nodeToString(nodeThis);
		Data objectThis = (Data) xStream.fromXML(nodeString);
		
		return objectThis;	
	}
	
	private Data getEntryThis(Node node) throws Exception {
		String expression = "./" + TAG_METHOD_ENTRY_EVENT + "/" + TAG_THIS ;  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		Node nodeThis = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
		
		String nodeString = nodeToString(nodeThis);
		Data objectThis = (Data) xStream.fromXML(nodeString);

		return objectThis;		
	}
	
	private Data getReturn(Node node) throws Exception{
		
		Data return_data = null;
		
		String expression = "./" + TAG_METHOD_EXIT_EVENT + "/" + TAG_RETURN;  
		XPathExpression xPathExpression = xPath.compile(expression);
		Node node_return = (Node) xPathExpression.evaluate(node,XPathConstants.NODE);
		
		if (node_return!=null){
			String nodeString = nodeToString(node_return);
			return_data = (Data) xStream.fromXML(nodeString);
		}

		return return_data;	
	}

	private NodeList getCalledMethods(Node node) throws Exception {
		String expression = "./" + TAG_CALLED_METHODS + "/*";  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		NodeList calledMethods = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		
		return calledMethods;
	}

	private List<ChangeInfo> getChangesArguments(Node node) throws Exception{
		
		List<Data> entryArguments = getEntryArguments(node);
		List<Data> exitArguments = getExitArguments(node);
		ChangeDetector detector = new ChangeDetector();
		
		List<ChangeInfo> changes = new ArrayList<>();
		
		for (int i=0;i<exitArguments.size();i++){
			changes.addAll(detector.getChangesBetween(entryArguments.get(i), exitArguments.get(i)));
		}
		
		return changes;
	}
	
	private List<Data> getExitArguments(Node node) throws Exception {
		String expression = "./" + TAG_METHOD_EXIT_EVENT + "/" + TAG_ARGUMENTS + "/*";  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		NodeList arguments = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		
		List<Data> argumentList = new ArrayList<>();
		
		for (int i=0;i<arguments.getLength();i++){
			String nodeString = nodeToString(arguments.item(i));
			Data variable = (Data) xStream.fromXML(nodeString);
			argumentList.add(variable);
		}
		
		return argumentList;
	}

	private List<Data> getEntryArguments(Node node) throws Exception {
		String expression = "./" + TAG_METHOD_ENTRY_EVENT + "/" + TAG_ARGUMENTS + "/*";  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		NodeList arguments = (NodeList) xPathExpression.evaluate(node,XPathConstants.NODESET);
		
		List<Data> argumentList = new ArrayList<>();
		
		for (int i=0;i<arguments.getLength();i++){
			String nodeString = nodeToString(arguments.item(i));
			Data variable = (Data) xStream.fromXML(nodeString);
			argumentList.add(variable);
		}
		
		return argumentList;
	}

	private String getCalledFromClass(Node node) throws XPathExpressionException {
		
		String expression = "./" + TAG_METHOD_ENTRY_EVENT + "/" + TAG_CALLED_FROM_CLASS;  
			
		XPathExpression xPathExpression = xPath.compile(expression);
		String className = (String) xPathExpression.evaluate(node,XPathConstants.STRING);
		
		return className;
	}

	private String getNameMethod(Node node) throws Exception {
		
		String expression = "./" + TAG_METHOD_ENTRY_EVENT + "/" + TAG_METHOD_NAME;  
		
		XPathExpression xPathExpression = xPath.compile(expression);
		String name = (String) xPathExpression.evaluate(node,XPathConstants.STRING);
		
		return name;
	}
	
	private void write(String string) throws IOException {
		bufferedWriter.write(string + "\n");
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

	private void writeXStream(Object object){
		xStream.toXML(object, bufferedWriter);
	}
	
}
