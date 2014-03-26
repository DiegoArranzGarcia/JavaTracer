package com.inspector.model;

import java.util.List;

import org.w3c.dom.Node;

import com.general.model.configuration.JavaTracerConfiguration;
import com.general.model.data.MethodInfo;
import com.general.model.data.ThreadInfo;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.data.MethodBox;
import com.inspector.treeinspector.data.ThreadBox;
import com.inspector.treeinspector.view.DefaultTreeLayout;

public class LoadTreeThread extends Thread{
	
	private int numNodes;
	private int DEFAULT_DEPTH;
	private int DEFAULT_NUM_NODES;
	private DefaultTreeLayout<Box> tree;
	private UpdateNotifier notifier;
	private XmlManager xml;
	private String xmlName;
	
	public LoadTreeThread(String xmlName,UpdateNotifier notifier){
		
		JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
		
		this.xmlName = xmlName;
		this.notifier = notifier;
		numNodes = 0;
		DEFAULT_DEPTH = configuration.getNumLevels();
	    DEFAULT_NUM_NODES = configuration.getNumNodes();
	    
	}
	
	public void run() {
		
		notifier.opening();
		xml = new XmlManager(xmlName);
		notifier.opened(xml);
		Node rootNode = xml.getRootNode();
		ThreadBox threadBox = getThreadBoxFromNode(rootNode);
		tree = new DefaultTreeLayout<Box>(threadBox); 
		generateTree();
		notifier.finishLoading(tree);
		
	}
	
	private void generateTree() {
		
		int depth = 0;
		while (depth<DEFAULT_DEPTH && numNodes<DEFAULT_NUM_NODES){
			loadLevel(depth);
			depth++;
		}
		
	}

	private ThreadBox getThreadBoxFromNode(Node rootNode) {
		ThreadBox threadBox = null;
		try{
			ThreadInfo threadInfo = xml.getThreadName(rootNode);
			threadBox = new ThreadBox("/trace/thread",0,threadInfo.getNameThread());
		} catch (Exception e){
			e.printStackTrace();
		}
		return threadBox;
	}

	private void loadLevel(int level){
		
		int i = 0;
		List<Box> nodes = tree.getNodesAtLevel(level);
		while (i<nodes.size() && numNodes<DEFAULT_NUM_NODES){
			loadNode(nodes.get(i));
			i++;
		}
		
	}

	private void loadNode(Box box) {
		
		int i = 1;
		boolean stop = false;
		
		while (!stop && numNodes<DEFAULT_NUM_NODES){
			
			String path = xml.getPath(box,i);
	
			stop = !xml.exist(path);
			
			if (!stop){
				MethodInfo nodeInfo = xml.getBoxFromNode(path);
				long id = xml.getIdFromNode(path);
				boolean haveChildren = xml.hasChildrenNode(path);
				MethodBox node = new MethodBox(path,id,nodeInfo,haveChildren);
			
				tree.addChild(box,node);
			
				numNodes++;
				notifier.updateInfo(numNodes,DEFAULT_NUM_NODES,(int)calculatePercentage());
				i++;
			}
			
		}
		
		
		box.setExpanded(true);
	}
	
	private double calculatePercentage() {
		double percentage = ((double)numNodes/DEFAULT_NUM_NODES)*100;
		return percentage;
	}

}
