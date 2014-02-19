package com.traceinspector.model;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.general.model.configuration.TraceInspectorConfiguration;
import com.general.model.variables.data.Data;
import com.traceinspector.treeinspector.data.MethodBox;
import com.traceinspector.treeinspectorview.DefaultTreeLayout;

public class TreeManager {

	private int DEFAULT_NUM_LEVELS_DEPTH;
	private int DEFAULT_NUM_NODES;
	
	private XmlManager xml;
	
	public TreeManager(String xmlDocument){
		xml = new XmlManager(xmlDocument);
	}
	
	public DefaultTreeLayout<MethodBox> loadTree() {
		
		TraceInspectorConfiguration configuration = new TraceInspectorConfiguration();
		DEFAULT_NUM_LEVELS_DEPTH = configuration.getDefaultNumLevelsDepth();
		DEFAULT_NUM_NODES = configuration.getDefaultNumNodes();
		
		int currentLevel = 0;
		int numNodes = 0;
			
		Node rootNode = xml.getRootNode();
		MethodBox root = getTextInBoxExtFromNode(rootNode);
		DefaultTreeLayout<MethodBox> tree = new DefaultTreeLayout<MethodBox>(root); 
				
		generateTree(tree,root,rootNode,currentLevel, numNodes);
		
		return tree;
	}

	private MethodBox getTextInBoxExtFromNode(Node node) {
		MethodBox root = null;
		try {
			long id = xml.getIdFromNode(node);
			String name = xml.getName(node);
			List<Data> arguments = xml.loadArguments(node);
			Data returnValue = xml.loadReturnValue(node);
			Data thisValue = xml.loadThisValue(node);
			boolean haveChildren = xml.haveChildrenOfNode(node);
			root = new MethodBox(id,name,arguments,returnValue,thisValue,haveChildren);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return root;
	}



	private int generateTree(DefaultTreeLayout<MethodBox> tree,MethodBox treeNode, Node node,int currentLevel, int numNodes){
				
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

	public void expandNode(DefaultTreeLayout<MethodBox> tree,MethodBox treeNode) {
				
		Node node = xml.getNode(treeNode.getId());
		NodeList nodeChilds = xml.getChildsOfNode(node);
		treeNode.setExpanded(true);
		
		for (int i=0;i<nodeChilds.getLength();i++){
			Node childNode = nodeChilds.item(i);
			MethodBox child = getTextInBoxExtFromNode(childNode);
			tree.addChild(treeNode, child);
		}
				
	}

	public void foldNode(DefaultTreeLayout<MethodBox> tree,MethodBox treeNode) {
		
		tree.removeChilds(treeNode);
		treeNode.setExpanded(false);
		
	}

}
