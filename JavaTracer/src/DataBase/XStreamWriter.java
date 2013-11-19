package DataBase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Info.ArrayInfo;
import Info.MethodEntryInfo;
import Info.MethodExitInfo;
import Info.ObjectInfo;

import com.thoughtworks.xstream.XStream;

public class XStreamWriter implements DataBaseWriter {

	private final String OUTPUT_PATH = "C:\\Users\\Diego\\Desktop\\";
	private final String OUTPUT_NAME = "output.xml";
	private final String TAG_TRACE = "trace";
	private XStream xstream;
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	public XStreamWriter(){
		try {
			this.fileWriter = new FileWriter(OUTPUT_NAME);
			this.bufferedWriter = new BufferedWriter(fileWriter);
			this.xstream = new XStream();
			bufferedWriter.write("<" + TAG_TRACE + ">" + "\n");
			xstream.alias("array",ArrayInfo.class);
			xstream.alias("methodentryevent",MethodEntryInfo.class);
			xstream.alias("methodexitevent",MethodExitInfo.class);
			xstream.alias("object",ObjectInfo.class);
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
			bufferedWriter.write("</" + TAG_TRACE + ">");
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
