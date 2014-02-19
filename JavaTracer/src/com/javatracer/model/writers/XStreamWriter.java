package com.javatracer.model.writers;

import com.general.model.variables.data.ArrayData;
import com.general.model.variables.data.IgnoredData;
import com.general.model.variables.data.NullData;
import com.general.model.variables.data.ObjectData;
import com.general.model.variables.data.SimpleData;
import com.general.model.variables.data.StringData;
import com.javatracer.model.methods.data.ChangeInfo;
import com.javatracer.model.methods.data.MethodEntryInfo;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.methods.data.MethodInfo;
import com.thoughtworks.xstream.XStream;

public abstract class XStreamWriter {
	
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
	public static String TAG_RETURN = "return";
	public static String TAG_CHANGES = "changes";
	public static String TAG_CHANGE = "change";
	public static String TAG_ARGUMENTS = "arguments";
	public static String TAG_METHOD_INFO = "info";
	public static String TAG_CALLED_FROM_CLASS = "calledFromClass";
	public static String TAG_METHOD_NAME = "methodName";
	
	public static String ATTR_ID = "id=\"";
	
	public static String DOUBLE_QUOTES = "\"";
	
	protected XStream xStream;
	
	public XStreamWriter(){
		this.xStream = new XStream();
		addAlias();
	}

	private void addAlias() {
		xStream.alias(TAG_METHOD_INFO, MethodInfo.class);
		xStream.alias(TAG_CHANGE, ChangeInfo.class);
		
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
		xStream.aliasField(TAG_FIELDS, ObjectData.class,"value");
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
	
}
