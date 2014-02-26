package com.tracer.model.writers;

import java.io.StringWriter;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.general.model.data.*;
import com.general.model.variables.data.*;
import com.thoughtworks.xstream.XStream;
import com.tracer.model.methods.data.MethodEntryInfo;
import com.tracer.model.methods.data.MethodExitInfo;

public abstract class XStreamUtil {
	
	public static String TAG_TRACE = "trace";
	public static String TAG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static String TAG_METHOD_CALL = "method-call";
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
	public static String TAG_RETURN = "return";
	public static String TAG_CHANGES = "changes";
	public static String TAG_CHANGE = "change";
	public static String TAG_ARGUMENTS = "arguments";
	public static String TAG_METHOD_INFO = "info";
	public static String TAG_CALLED_FROM_CLASS = "calledFromClass";
	public static String TAG_METHOD_NAME = "methodame";
	public static String TAG_THREAD = "thread";
	public static String TAG_THREAD_INFO = "thread-info";
	public static String TAG_EXCEPTION = "exception";
	
	public static String ATTR_ID = "id";
	
	public static String DOUBLE_QUOTES = "\"";
	
	protected XStream xStream;
	
	public XStreamUtil(){
		this.xStream = new XStream();
		addAlias();
	}

	private void addAlias() {
		xStream.alias(TAG_METHOD_INFO, MethodInfo.class);
		xStream.alias(TAG_CHANGE, ChangeInfo.class);
		
		//ThreadInfo alias
		xStream.alias(TAG_THREAD_INFO,ThreadInfo.class);
		
		//MethodEntryInfo alias
		xStream.alias(TAG_METHOD_ENTRY_EVENT,MethodEntryInfo.class);
		xStream.aliasField(TAG_THIS, MethodEntryInfo.class,"this_data");
		xStream.aliasField(TAG_ARGUMENTS, MethodEntryInfo.class, "arguments");
		xStream.aliasField(TAG_CALLED_FROM_CLASS, MethodEntryInfo.class,"calledFromClass");
		xStream.aliasField(TAG_METHOD_NAME, MethodEntryInfo.class, "methodName");
		
		//MethodExitInfo alias
		xStream.alias(TAG_METHOD_EXIT_EVENT,MethodExitInfo.class);
		
		xStream.aliasField(TAG_THIS, MethodExitInfo.class,"this_data");
		xStream.aliasField(TAG_ARGUMENTS, MethodExitInfo.class, "arguments");
		xStream.aliasField(TAG_RETURN, MethodExitInfo.class,"return_data");
		
		//MethodInfo alias
		xStream.aliasField(TAG_ARGUMENTS, MethodInfo.class, "arguments");
		xStream.aliasField(TAG_THIS, MethodInfo.class,"this_data");
		xStream.aliasField(TAG_RETURN, MethodInfo.class,"return_data");
		
		//Data alias
		xStream.aliasField(TAG_CONTENT, ArrayData.class,"value");
		xStream.alias(TAG_SIMPLE_DATA, SimpleData.class);
		xStream.alias(TAG_OBJECT, ObjectData.class);
		xStream.alias(TAG_IGNORED, IgnoredData.class);
		xStream.alias(TAG_ARRAY, ArrayData.class);
		xStream.alias(TAG_STRING,StringData.class);
		xStream.alias(TAG_NULL,NullData.class);
	}

	protected String startTag(String tag){
		return "<" + tag + ">";
	}

	protected String endTag(String tag) {
		return "</" + tag + ">";
	}
	
	protected String nodeToString(Node node) {

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
