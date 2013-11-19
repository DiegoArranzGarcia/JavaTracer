package DataBase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Info.ArrayInfo;
import Info.InterfaceInfo;
import Info.MethodEntryInfo;
import Info.MethodExitInfo;
import Info.ObjectInfo;

import com.thoughtworks.xstream.XStream;

public class XStreamWriter implements DataBaseWriter {

	private final String OUTPUT_NAME = "output.xml";
	private final String TAG_TRACE = "trace";
	private final String XML_TAG = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>";
	private final String METHOD_XML_NAME = "method-call";
	private final String CALLED_METHODS_XML_NAME = "called-methods";
	
	private int currentLevel;
	private XStream xstream;
	private BufferedWriter bufferedWriter;
	
	public XStreamWriter(){
		try {
			FileWriter fileWriter = new FileWriter(OUTPUT_NAME);
			this.bufferedWriter = new BufferedWriter(fileWriter);
			this.xstream = new XStream();
			this.currentLevel = 0;
			write(XML_TAG);
			write(startTag(TAG_TRACE));
			addAlias();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	private void addAlias() {
		xstream.alias("array",ArrayInfo.class);
		xstream.alias("methodEntryEvent",MethodEntryInfo.class);
		xstream.alias("methodExitEvent",MethodExitInfo.class);
		xstream.alias("object",ObjectInfo.class);
	}

	public void writeOutput(InterfaceInfo info){
		try {
			String xmlString = xstream.toXML(info);
			if (info instanceof MethodEntryInfo){
				processMethodEntryEvent(xmlString);
			}
			else if (info instanceof MethodExitInfo){
				processMethodExitEvent(xmlString);
			}			
		} catch (Exception e) {
			
		}
	}
	
	private void write(String string) throws IOException {
		bufferedWriter.write(string + "\n");
	}
	
	public void close() {
		try {
			write(endTag(TAG_TRACE));
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String startTag(String tag){
		return "<" + tag + ">";
	}

	private String endTag(String tag) {
		return "</" + tag + ">";
	}

	private void processMethodExitEvent(String info) throws Exception {
		this.currentLevel--;
		write(endTag(CALLED_METHODS_XML_NAME));
		write(info);
		write(endTag(METHOD_XML_NAME));		
	}
	
	private void processMethodEntryEvent(String info) throws Exception {
		this.currentLevel++;
		write(startTag(METHOD_XML_NAME));
		write(info);
		write(startTag(CALLED_METHODS_XML_NAME));
	}

	public int getCurrentLevel() {
		return currentLevel;
	}
	
}
