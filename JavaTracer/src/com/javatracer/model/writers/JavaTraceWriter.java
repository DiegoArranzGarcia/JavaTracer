package com.javatracer.model.writers;

import java.io.FileWriter;

import com.general.model.data.ThreadInfo;
import com.javatracer.model.methods.data.*;

public class JavaTraceWriter extends XStreamUtil{

	public static String FILE_EXT = "_temp.xml";
		
	private FileWriter fileWriter;
	
	public JavaTraceWriter(String nameXlm){
		try {
			fileWriter = new FileWriter(nameXlm + FILE_EXT);
			
			write(TAG_XML);
			write(startTag(TAG_TRACE));
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}
	
	public void writeMethodEntryInfo(MethodEntryInfo info){
		
		try {
			String xmlString = xStream.toXML(info);
			write(startTag(TAG_METHOD));
			write(xmlString);
			write(startTag(TAG_CALLED_METHODS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeMethodExitInfo(MethodExitInfo info){
		
		try {
			String xmlString = xStream.toXML(info);
			write(endTag(TAG_CALLED_METHODS));
			write(xmlString);
			write(endTag(TAG_METHOD));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeThreadInfo(ThreadInfo threadInfo){
		try{
			String xmlString = xStream.toXML(threadInfo);
			write(startTag(TAG_THREAD));
			write(xmlString);
			write(endTag(TAG_THREAD));
			write(startTag(TAG_CALLED_METHODS));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeExceptionInfo(ExceptionInfo threadInfo){
		try{
			String xmlString = xStream.toXML(threadInfo);
			write(startTag(TAG_EXCEPTION));
			write(xmlString);
			write(endTag(TAG_EXCEPTION));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void close(){
		
		try {
			write(endTag(TAG_CALLED_METHODS));
			write(endTag(TAG_TRACE));
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
	private void write(String string) throws Exception {
		fileWriter.write(string + "\n");
	}
	
}
