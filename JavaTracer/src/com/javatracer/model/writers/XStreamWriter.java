package com.javatracer.model.writers;

import java.io.FileWriter;

import com.javatracer.model.methods.data.MethodEntryInfo;
import com.javatracer.model.methods.data.MethodExitInfo;
import com.javatracer.model.variables.data.ArrayData;
import com.javatracer.model.variables.data.IgnoredData;
import com.javatracer.model.variables.data.NullData;
import com.javatracer.model.variables.data.ObjectData;
import com.javatracer.model.variables.data.SimpleData;
import com.javatracer.model.variables.data.StringData;
import com.thoughtworks.xstream.XStream;

public class XStreamWriter {

	public static String FILE_NAME = "output.xml";
	
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
	
	private XStream xStream;
	private FileWriter fileWriter;
	
	public XStreamWriter(String nameXlm){
		try {
			fileWriter = new FileWriter(nameXlm+"_temp.xml");
			xStream = new XStream();

			addAlias();
			
			write(TAG_XML);
			write(startTag(TAG_TRACE));
			
		} catch (Exception e) {
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

	public void writeOutput(MethodEntryInfo info){
			try {
				String xmlString = xStream.toXML(info);
				processMethodEntryEvent(xmlString);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void writeOutput(MethodExitInfo info){
		try {
			String xmlString = xStream.toXML(info);
			processMethodExitEvent(xmlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			write(endTag(TAG_TRACE));
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void write(String string) throws Exception {
		fileWriter.write(string + "\n");
		System.out.flush();
	}

	private String startTag(String tag){
		return "<" + tag + ">";
	}

	private String endTag(String tag) {
		return "</" + tag + ">";
	}

	private void processMethodExitEvent(String info) throws Exception {
		write(endTag(TAG_CALLED_METHODS));
		write(info);
		write(endTag(TAG_METHOD));
	}
	
	private void processMethodEntryEvent(String info) throws Exception {
		write(startTag(TAG_METHOD));
		write(info);
		write(startTag(TAG_CALLED_METHODS));
	}
	
}
