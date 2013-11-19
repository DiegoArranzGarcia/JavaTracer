
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TraceReader {

	public final String OUTPUT_NAME = "output.xml";
	
	private Document xmlDocument;
	private NodeProcessor processor;
	
	public TraceReader() {
		try {
			xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("C:\\Users\\Diego\\Documents\\GitHub\\JavaTracer\\JavaTracer\\" + OUTPUT_NAME);
			processor = new NodeProcessor();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void read(){	
		try {
			String expression = "/trace/*[1]";	
			XPath xPath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpression = xPath.compile(expression);
			Node node = (Node) xPathExpression.evaluate(xmlDocument,XPathConstants.NODE);
			do {
				processor.processNode(node);
				node = node.getNextSibling();			
			} while (node != null);
	
		} catch (Exception e){
			e.printStackTrace();
		}
		processor.finishProcess();
	}

}
