package com.inspector.model;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.general.model.configuration.JavaTracerConfigurationXml;
import com.general.model.data.MethodInfo;
import com.general.model.data.ThreadInfo;
import com.inspector.treeinspector.data.*;
import com.inspector.treeinspector.view.DefaultTreeLayout;

public class TreeManager {

	private int DEFAULT_NUM_LEVELS_DEPTH;
	private int DEFAULT_NUM_NODES;
	private DefaultTreeLayout<Box> lastTree;
	
	private XmlManager xml;
	
	
	public void loadTree(String xmlName) {
		
		xml = new XmlManager(xmlName);
		//TraceInspectorConfiguration configuration = new TraceInspectorConfiguration();
		JavaTracerConfigurationXml configuration = JavaTracerConfigurationXml.getInstance();
		/*DEFAULT_NUM_LEVELS_DEPTH = configuration.getDefaultNumLevelsDepth();
		DEFAULT_NUM_NODES = configuration.getDefaultNumNodes();*/
		try {
	        DEFAULT_NUM_LEVELS_DEPTH = configuration.getNumLevelsFromFile();
	        DEFAULT_NUM_NODES = configuration.getNumNodesFromFile();
        }
        catch (Exception ex) {
	        // TODO Auto-generated catch block
	        ex.printStackTrace();
        }
		
		
		int currentLevel = 0;
		int numNodes = 0;
			
		Node rootNode = xml.getRootNode();
		ThreadBox threadBox = getThreadBoxFromNode(rootNode);
		DefaultTreeLayout<Box> tree = new DefaultTreeLayout<Box>(threadBox); 
				
		generateTree(tree,threadBox,rootNode,currentLevel, numNodes);
		
		this.lastTree = tree;
	}

	private ThreadBox getThreadBoxFromNode(Node rootNode) {
		ThreadBox threadBox = null;
		try{
			ThreadInfo threadInfo = xml.getThreadName(rootNode);
			threadBox = new ThreadBox(0,threadInfo.getNameThread());
		} catch (Exception e){
			e.printStackTrace();
		}
		return threadBox;
	}

	private MethodBox getTextInBoxExtFromNode(Node node) {
		MethodBox root = null;
		try {
			long id = xml.getIdFromNode(node);
			MethodInfo method = xml.getInfoMethod(node);
			boolean haveChildren = xml.haveChildrenOfNode(node);
			root = new MethodBox(id,method,haveChildren);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return root;
	}



	private int generateTree(DefaultTreeLayout<Box> tree,Box treeNode, Node node,int currentLevel, int numNodes){
				
		if (currentLevel<DEFAULT_NUM_LEVELS_DEPTH){
			
			
			treeNode.setExpanded(true);
			int i = 0;
			NodeList childs = xml.getChildsOfNode(node);
			
			while (numNodes<DEFAULT_NUM_NODES && i < childs.getLength()){
				
				Node childNode = childs.item(i);
				MethodBox childTreeNode = getTextInBoxExtFromNode(childNode);
				tree.addChild(treeNode,childTreeNode);
				
				numNodes = generateTree(tree,childTreeNode,childNode,currentLevel+1,numNodes);
				i++;
			}
			
		}

		return numNodes;
		
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

	public void expandNode(DefaultTreeLayout<Box> tree,Box treeNode) {
		
		Node node=null;
		
		if (treeNode.getId()==0)
			node=xml.getRootNode();
		else
			node = xml.getNode(treeNode.getId());
		
		NodeList nodeChilds = xml.getChildsOfNode(node);
		treeNode.setExpanded(true);
		
		
		for (int i=0;i<nodeChilds.getLength();i++){
			Node childNode = nodeChilds.item(i);
			MethodBox child = getTextInBoxExtFromNode(childNode);
			tree.addChild(treeNode, child);
		}
				
		
	}

	public void foldNode(DefaultTreeLayout<Box> tree,Box treeNode) {
		
		tree.removeChilds(treeNode);
		treeNode.setExpanded(false);
		
	}

	public DefaultTreeLayout<Box> getTree() {
		return lastTree;
	}

}
