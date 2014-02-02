package com.javatracer.model.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.IgnoredClass;
import com.javatracer.model.data.MethodEntryInfo;
import com.javatracer.model.data.MethodExitInfo;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.StringInfo;
import com.javatracer.model.data.VariableInfo;
import com.thoughtworks.xstream.XStream;

public class XStreamWriter {

	public static String FILE_NAME = "output.xml";
	
	public static String TAG_TRACE = "trace";
	public static String TAG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static String TAG_METHOD = "method-call";
	public static String TAG_CALLED_METHODS = "called-methods";
	public static String TAG_METHOD_ENTRY_EVENT = "method-entry-event";
	public static String TAG_METHOD_EXIT_EVENT = "method-exit-event";
	public static String TAG_VARIABLE = "variable";
	public static String TAG_OBJECT = "object-info";
	public static String TAG_ARRAY = "array";
	public static String TAG_STRING = "string";
	public static String TAG_NULL = "null";
	public static String TAG_IGNORED = "ignored";
	public static String TAG_THIS = "object-this";
	
	private XStream xStream;
	private BufferedWriter bufferedWriter;
	
	public XStreamWriter(String nameXlm){
		try {
			FileWriter fileWriter = new FileWriter(nameXlm+"_temp.xml");
			this.bufferedWriter = new BufferedWriter(fileWriter);
			
			this.xStream = new XStream();

			addAlias();
			
			write(TAG_XML);
			write(startTag(TAG_TRACE));
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	private void addAlias() {
		xStream.alias(TAG_METHOD_ENTRY_EVENT,MethodEntryInfo.class);
		xStream.alias(TAG_METHOD_EXIT_EVENT,MethodExitInfo.class);
		xStream.aliasField(TAG_THIS, MethodEntryInfo.class,"objectThis");
		xStream.aliasField(TAG_THIS, MethodExitInfo.class,"objectThis");
		xStream.alias(TAG_VARIABLE,VariableInfo.class);
		xStream.alias(TAG_OBJECT, ObjectInfo.class);
		xStream.alias(TAG_IGNORED, IgnoredClass.class);
		xStream.alias(TAG_ARRAY, ArrayInfo.class);
		xStream.alias(TAG_STRING,StringInfo.class);
		xStream.alias(TAG_NULL,NullObject.class);
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
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
