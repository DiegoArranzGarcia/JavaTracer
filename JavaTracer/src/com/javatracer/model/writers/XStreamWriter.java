package com.javatracer.model.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.javatracer.model.data.ArgumentInfo;
import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.InterfaceInfo;
import com.javatracer.model.data.MethodEntryInfo;
import com.javatracer.model.data.MethodExitInfo;
import com.javatracer.model.data.ObjectInfo;
import com.thoughtworks.xstream.XStream;

public class XStreamWriter implements DataBaseWriter {

	public static String FILE_NAME = "output.xml";
	
	public static String TAG_TRACE = "trace";
	public static String TAG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static String TAG_METHOD = "method-call";
	public static String TAG_CALLED_METHODS = "called-methods";
	public static String TAG_METHOD_ENTRY_EVENT = "method-entry-event";
	public static String TAG_METHOD_EXIT_EVENT = "methodExitEvent";
	
	private XStream xStream;
	private BufferedWriter bufferedWriter;
	
	public XStreamWriter(){
		try {
			FileWriter fileWriter = new FileWriter(FILE_NAME);
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
		xStream.alias("array",ArrayInfo.class);
		xStream.alias(TAG_METHOD_ENTRY_EVENT,MethodEntryInfo.class);
		xStream.alias(TAG_METHOD_EXIT_EVENT,MethodExitInfo.class);
		xStream.alias("object",ObjectInfo.class);
		xStream.alias("argument",ArgumentInfo.class);
	}

	public void writeOutput(InterfaceInfo info){
		try {
			String xmlString = xStream.toXML(info);
			if (info instanceof MethodEntryInfo){
				processMethodEntryEvent(xmlString);
			}
			else if (info instanceof MethodExitInfo){
				processMethodExitEvent(xmlString);
			}			
		} catch (Exception e) {
			
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