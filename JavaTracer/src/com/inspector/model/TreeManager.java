package com.inspector.model;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.general.model.configuration.JavaTracerConfiguration;
import com.general.model.data.MethodInfo;
import com.general.model.data.ThreadInfo;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.data.MethodBox;
import com.inspector.treeinspector.data.ThreadBox;
import com.inspector.treeinspector.view.DefaultTreeLayout;

public class TreeManager {

	private int DEFAULT_DEPTH;
	private int DEFAULT_NUM_NODES;
	private DefaultTreeLayout<Box> lastTree;
	
	private XmlManager xml;
	private int numNodes;
	
	public void loadTree(String xmlName) {
		
		xml = new XmlManager(xmlName);
		JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
		try {
	        DEFAULT_DEPTH = configuration.getNumLevelsFromFile();
	        DEFAULT_NUM_NODES = configuration.getNumNodesFromFile();
        }
        catch (Exception ex) {
	        ex.printStackTrace();
        }
		
		numNodes = 0;
			
		Node rootNode = xml.getRootNode();
		ThreadBox threadBox = getThreadBoxFromNode(rootNode);
		DefaultTreeLayout<Box> tree = new DefaultTreeLayout<Box>(threadBox); 
		generateTree(tree);
		this.lastTree = tree;
	}

	private void generateTree(DefaultTreeLayout<Box> tree) {
		
		int depth = 0;
		while (depth<DEFAULT_DEPTH && numNodes<DEFAULT_NUM_NODES){
			loadLevel(tree,depth);
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

	private void loadLevel(DefaultTreeLayout<Box> tree,int level){
		
		int i = 0;
		List<Box> nodes = tree.getNodesAtLevel(level);
		while (i<nodes.size() && numNodes<DEFAULT_NUM_NODES){
			loadNode(tree,nodes.get(i));
			i++;
		}
		
	}

	private void loadNode(DefaultTreeLayout<Box> tree, Box box) {
		
		int i = 1;
		boolean stop = false;
		//int childs = xml.getNumChildrenOfNode(box);
		
		while (!stop && numNodes<DEFAULT_NUM_NODES){
			
			String path = xml.getPath(box,i);
			
			System.out.println("Procesando: " + path);
			stop = !xml.exist(path);
			
			if (!stop){
				MethodInfo nodeInfo = xml.getBoxFromNode(path);
				long id = xml.getIdFromNode(path);
				boolean haveChildren = xml.hasChildrenNode(path);
				MethodBox node = new MethodBox(path,id,nodeInfo,haveChildren);
			
				tree.addChild(box,node);
			
				numNodes++;
				i++;
			}
			
			System.out.println("Procesado: " + path);
		}
		
		
		box.setExpanded(true);
	}
	
	public MethodBox getTextInBoxFromId(DefaultTreeLayout<MethodBox> tree, long id){
		
		MethodBox textInBoxExt = null;
		boolean found = false;
		Iterator<MethodBox> allChildren = tree.getChildren(tree.getRoot()).iterator();
		
		while(!found && allChildren.hasNext()){
			
			MethodBox child = allChildren.next();
			found = (child.getId() == id);
			if (found){
				textInBoxExt = child;
			}
			
		}
		
		return textInBoxExt;
	}

	public void expandNode(DefaultTreeLayout<Box> tree,Box box) {
		int i = 1;
		int childs = xml.getNumChildrenOfNode(box);
		
		while (i <= childs){
			
			String path = xml.getPath(box,i);
			
			MethodInfo nodeInfo = xml.getBoxFromNode(path);
			long id = xml.getIdFromNode(path);
			boolean haveChildren = xml.hasChildrenNode(path);
			MethodBox node = new MethodBox(path,id,nodeInfo,haveChildren);
			
			tree.addChild(box,node);
			
			i++;
		}
		
		
		box.setExpanded(true);
	}

	public void foldNode(DefaultTreeLayout<Box> tree,Box treeNode) {
		
		tree.removeChilds(treeNode);
		treeNode.setExpanded(false);
		
	}

	public DefaultTreeLayout<Box> getTree() {
		return lastTree;
	}

}
