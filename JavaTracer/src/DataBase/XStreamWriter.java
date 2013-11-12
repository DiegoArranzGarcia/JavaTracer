package DataBase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Tracer.ArrayInfo;

import com.thoughtworks.xstream.XStream;

public class XStreamWriter implements DataBaseWriter {

	private static String OUTPUT_PATH = "C:\\Users\\Diego\\Desktop\\";
	private static String OUTPUT_NAME = "output.xml"; 
	private XStream xstream;
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	public XStreamWriter(){
		try {
			this.fileWriter = new FileWriter(OUTPUT_NAME);
			this.bufferedWriter = new BufferedWriter(fileWriter);
			this.xstream = new XStream();
			xstream.alias("array",ArrayInfo.class);
			xstream.alias("methodentryevent",MethodEntryInfo.class);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public void writeOutput(Object output){
		try {
			String xmlString = xstream.toXML(output);
			bufferedWriter.write(xmlString+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
