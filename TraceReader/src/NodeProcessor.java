import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;


public class NodeProcessor {

	private final String OUTPUT = "trace.xml";
	private final String METHOD_XML_NAME = "method-call";
	private final String CALLED_METHODS_XML_NAME = "called-methods";
	
	private int currentLevel;
	private BufferedWriter bf;
	
	public NodeProcessor(){
		try {
			this.currentLevel = 0;
			FileWriter fw = new FileWriter(OUTPUT);
			bf = new BufferedWriter(fw);
			write(startTag("trace"));
		} catch (IOException e) {
			
		}	
	}
	
	private void write(String string) throws IOException {
		bf.write(string + "\n");
	}

	public void finishProcess(){
		try {
			write(endTag("trace"));
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processNode(Node node) throws Exception {
		if (node.getNodeName().equals("methodentryevent")){
			processMethodEntryEvent(node);
		}
		else if (node.getNodeName().equals("methodexitevent")){
			processMethodExitEvent(node);
		}
	}

	private void processMethodExitEvent(Node node) throws Exception {
		this.currentLevel--;
		write(endTag(CALLED_METHODS_XML_NAME));
		write(nodeToString(node));
		write(endTag(METHOD_XML_NAME));		
	}
	
	private void processMethodEntryEvent(Node node) throws Exception {
		this.currentLevel++;
		write(startTag(METHOD_XML_NAME));
		write(nodeToString(node));
		write(startTag(CALLED_METHODS_XML_NAME));
	}
	
	private String startTag(String tag){
		return "<" + tag + ">";
	}

	private String endTag(String tag) {
		return "</" + tag + ">";
	}

	private String nodeToString(Node node) throws Exception{
		StringWriter sw = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.transform(new DOMSource(node), new StreamResult(sw));
		return sw.toString();
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

}
