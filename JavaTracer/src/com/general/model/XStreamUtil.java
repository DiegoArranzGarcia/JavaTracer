package com.general.model;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.general.model.data.ChangeInfo;
import com.general.model.data.MethodInfo;
import com.general.model.data.ThreadInfo;
import com.general.model.data.variables.ArrayData;
import com.general.model.data.variables.IgnoredData;
import com.general.model.data.variables.NullData;
import com.general.model.data.variables.ObjectData;
import com.general.model.data.variables.SimpleData;
import com.general.model.data.variables.StringData;
import com.profiler.model.data.ClassMethods;
import com.profiler.model.data.ExcludedClassesMethods;
import com.thoughtworks.xstream.XStream;
import com.tracer.model.data.methods.MethodEntryInfo;
import com.tracer.model.data.methods.MethodExitInfo;

public abstract class XStreamUtil {
	
	
	public static String TAG_TRACE = "trace";
	public static String TAG_XML = "?xml version=\"1.0\" encoding=\"ISO-8859-1\"?";
	
	public static String CONFIGURATION_DTD_NAME = "settings.dtd";
	public static String DTD_DIRECTORY = ".." + FileUtilities.SEPARATOR + "dtd" + FileUtilities.SEPARATOR;
	
	public static String TAG_CONFIGURATION_DTD = "!DOCTYPE configuration SYSTEM \""  + DTD_DIRECTORY + CONFIGURATION_DTD_NAME + "\"";
	
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
	
	public static String TAG_CONFIGURATION ="configuration";
	public static String TAG_EXCLUDES="excludes";
	public static String TAG_NUM_LEVELS ="num-levels";
	public static String TAG_NUM_NODES = "num-nodes";
	public static String TAG_METHODS_EXCLUDES = "methods-excludes";
	public static String TAG_EXCLUDED_THIS = "exclude-this";
	public static String TAG_EXCLUDED_LIBRARIES = "exclude-libraries";
	public static String TAG_EXCLUDED_DATA_STRUCTURE = "exclude-data-structure";
	public static String TAG_UNLIMITED_LEVELS = "unlimited-depth-tree";
	public static String TAG_UNLIMITED_NODES = "unlimited-nodes";
	public static String TAG_EXCLUDED_CLASSES_METHODS = "excluded-classes-methods";
	public static String TAG_CLASS_METHODS = "class-methods";
		
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
		
		//Profiler alias
		xStream.alias(TAG_EXCLUDED_CLASSES_METHODS,ExcludedClassesMethods.class);
		xStream.alias(TAG_CLASS_METHODS,ClassMethods.class);
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
