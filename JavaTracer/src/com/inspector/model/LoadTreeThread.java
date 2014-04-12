
package com.inspector.model;

import java.util.List;

import javax.swing.JOptionPane;

import org.w3c.dom.Node;

import com.general.model.data.MethodInfo;
import com.general.model.data.ThreadInfo;
import com.general.settings.model.Settings;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.data.MethodBox;
import com.inspector.treeinspector.data.ThreadBox;
import com.inspector.treeinspector.view.DefaultTreeLayout;

public class LoadTreeThread extends Thread{
	
	private int numNodes;
	private int max_depth;
	private int max_num_nodes;
	private DefaultTreeLayout<Box> tree;
	private UpdateNotifier notifier;
	private XmlManager xml;
	private String xmlName;
	private boolean unlimited_levels;
	private boolean unlimited_nodes;
	
	public LoadTreeThread(String xmlName,UpdateNotifier notifier){
		
		Settings configuration = Settings.getInstance();
		
		this.xmlName = xmlName;
		this.notifier = notifier;
		numNodes = 0;
		unlimited_nodes = configuration.isUnlimitedNodes();
		unlimited_levels = configuration.isUnlimitedLevels();
		max_depth = configuration.getNumLevels();
		max_num_nodes = configuration.getNumNodes();
	    
	}
	
	public void run() {
		
		notifier.opening();
		try{
			xml = new XmlManager(xmlName);
			notifier.opened(xml);
			Node rootNode = xml.getRootNode();
			ThreadBox threadBox = getThreadBoxFromNode(rootNode);
			tree = new DefaultTreeLayout<Box>(threadBox); 
			generateTree();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Corrupted trace file. The program will not load this file.",
					"Error", JOptionPane.ERROR_MESSAGE);
			tree = null;
		}
		notifier.finishLoading(tree);
		
	}
	
	private void generateTree() {
		
		int depth = 0;
		boolean finished = false;
		while (!finished && canLoadNextLevel(depth)){
			int loadedNodes = loadLevel(depth);
			finished = (loadedNodes == 0);
			depth++;
		}
		
	}

	private boolean canLoadNextLevel(int depth) {
		return (!unlimited_levels && depth<max_depth) || unlimited_levels;
	}

	private ThreadBox getThreadBoxFromNode(Node rootNode) throws Exception{
		ThreadBox threadBox = null;
		ThreadInfo threadInfo = xml.getThreadName(rootNode);
		threadBox = new ThreadBox("/trace/thread",0,threadInfo.getNameThread());
		return threadBox;
	}

	private int loadLevel(int level){
		
		int i = 0;
		List<Box> nodes = tree.getNodesAtLevel(level);
		while (i<nodes.size()){
			loadNode(nodes.get(i));
			i++;
		}
		
		return nodes.size();
		
	}

	private boolean canLoadMoreNodes() {
		return (!unlimited_nodes && numNodes<max_num_nodes) || unlimited_nodes;
	}

	private void loadNode(Box box) {
		
		int i = 1;
		boolean stop = false;
		
		while (!stop && canLoadMoreNodes()){
			
			String path = xml.getPath(box,i);
	
			stop = !xml.exist(path);
			
			if (!stop){
				MethodInfo nodeInfo = xml.getBoxFromNode(path);
				long id = xml.getIdFromNode(path);
				boolean haveChildren = xml.hasChildrenNode(path);
				MethodBox node = new MethodBox(path,id,nodeInfo,haveChildren);
			
				tree.addChild(box,node);
			
				numNodes++;
				notifier.updateInfo(numNodes);
				i++;
			}
			
		}
		
		box.setLoaded(stop);
		box.setExpanded(true);
	}

}
