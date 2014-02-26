package com.javatracer.model.writers;

import java.io.FileWriter;

import com.general.model.data.ThreadInfo;
import com.javatracer.model.methods.data.*;

public class TraceWriter extends XStreamUtil{

	public static String FILE_EXT = "_temp.xml";
		
	private FileWriter fileWriter;
	private int depth;
	
	public TraceWriter(String nameXlm){
		try {
			this.fileWriter = new FileWriter(nameXlm + FILE_EXT);
			this.depth = 1;
			
			write(TAG_XML);
			write(startTag(TAG_TRACE));
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}
	
	public void writeMethodEntryInfo(MethodEntryInfo info){
		
		try {
			String xmlString = xStream.toXML(info);
			write(startTag(TAG_METHOD_CALL));
			write(xmlString);
			write(startTag(TAG_CALLED_METHODS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		depth++;
	}
	
	public void writeMethodExitInfo(MethodExitInfo info){
		
		try {
			String xmlString = xStream.toXML(info);
			write(endTag(TAG_CALLED_METHODS));
			write(xmlString);
			write(endTag(TAG_METHOD_CALL));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		depth--;
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
	
	public void close(){
		
		try {
						
			while (depth>=1){
				
				if (depth == 1){
					write(endTag(TAG_CALLED_METHODS));
					write(endTag(TAG_TRACE));
				} else {
					write(endTag(TAG_CALLED_METHODS));
					write(endTag(TAG_METHOD_CALL));
				}
				
				depth--;
				
			}			

			fileWriter.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
	private void write(String string) throws Exception {
		fileWriter.write(string + "\n");
	}
	
}
