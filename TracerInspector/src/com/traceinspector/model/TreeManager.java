package com.traceinspector.model;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.javatracer.model.variables.data.Data;
import com.traceinspector.model.configuration.Configuration;
import com.traceinspector.treeinspectorview.DefaultTreeLayout;
import com.traceinspector.treeinspectorview.TextInBoxExt;

public class TreeManager {

	private int DEFAULT_NUM_LEVELS_DEPTH;
	private int DEFAULT_NUM_NODES;
	
	private XmlManager xml;
	
	public TreeManager(String xmlDocument){
		xml = new XmlManager(xmlDocument);
	}
	
	public DefaultTreeLayout<TextInBoxExt> loadTree() {
		
		Configuration configuration = new Configuration();
		DEFAULT_NUM_LEVELS_DEPTH = configuration.getDefaultNumLevelsDepth();
		DEFAULT_NUM_NODES = configuration.getDefaultNumNodes();
		
		int currentLevel = 0;
		int numNodes = 0;
			
		Node rootNode = xml.getRootNode();
		TextInBoxExt root = getTextInBoxExtFromNode(rootNode);
		DefaultTreeLayout<TextInBoxExt> tree = new DefaultTreeLayout<TextInBoxExt>(root); 
				
		generateTree(tree,root,rootNode,currentLevel, numNodes);
		
		return tree;
	}

	private TextInBoxExt getTextInBoxExtFromNode(Node node) {
		TextInBoxExt root = null;
		try {
			long id = xml.getIdFromNode(node);
			String name = xml.getName(node);
			List<Data> arguments = xml.loadArguments(node);
			Data returnValue = xml.loadReturnValue(node);
			Data thisValue = xml.loadThisValue(node);
			boolean haveChildren = xml.haveChildrenOfNode(node);
			root = new TextInBoxExt(id,name,arguments,returnValue,thisValue,haveChildren);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return root;
	}



	private int generateTree(DefaultTreeLayout<TextInBoxExt> tree,TextInBoxExt treeNode, Node node,int currentLevel, int numNodes){
				
		if (currentLevel<DEFAULT_NUM_LEVELS_DEPTH){
			
			
			treeNode.setExpanded(true);
			int i = 0;
			NodeList childs = xml.getChildsOfNode(node);
			
			while (numNodes<DEFAULT_NUM_NODES && i < childs.getLength()){
				
				Node childNode = childs.item(i);
				TextInBoxExt childTreeNode = getTextInBoxExtFromNode(childNode);
				tree.addChild(treeNode,childTreeNode);
				
				numNodes = generateTree(tree,childTreeNode,childNode,currentLevel+1,numNodes);
				i++;
			}
			
		}

		return numNodes;
		
	}

	public TextInBoxExt getTextInBoxFromId(DefaultTreeLayout<TextInBoxExt> tree, long id){
		
		TextInBoxExt textInBoxExt = null;
		boolean found = false;
		Iterator<TextInBoxExt> allChildren = tree.getChildren(tree.getRoot()).iterator();
		
		while(!found && allChildren.hasNext()){
			
			TextInBoxExt child = allChildren.next();
			found = (child.getId() == id);
			if (found){
				textInBoxExt = child;
			}
			
		}
		
		return textInBoxExt;
	}

	public void expandNode(DefaultTreeLayout<TextInBoxExt> tree,TextInBoxExt treeNode) {
				
		Node node = xml.getNode(treeNode.getId());
		NodeList nodeChilds = xml.getChildsOfNode(node);
		treeNode.setExpanded(true);
		
		for (int i=0;i<nodeChilds.getLength();i++){
			Node childNode = nodeChilds.item(i);
			TextInBoxExt child = getTextInBoxExtFromNode(childNode);
			tree.addChild(treeNode, child);
		}
				
	}

	public void foldNode(DefaultTreeLayout<TextInBoxExt> tree,TextInBoxExt treeNode) {
		
		tree.removeChilds(treeNode);
		treeNode.setExpanded(false);
		
	}

}
