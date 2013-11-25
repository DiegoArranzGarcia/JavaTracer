package com.traceinspector.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.traceinspector.datamodel.MethodNode;
import com.traceinspector.datamodel.TreeNode;

public class TreeGenerator {

	private final int DEFAULT_NUM_LEVELS_DEPTH = 4;
	private final int DEFAULT_NUM_NODES = 30;
	
	private XmlManager xml;
	
	public TreeNode loadFromFile(String string) {
		
		this.xml = new XmlManager(string);
		
		int currentLevel = 0;
		int numNodes = 0;
		TreeNode tree = new TreeNode();
		
		Node rootNode = xml.getRootNode();
		
		generateTree(rootNode,tree,currentLevel, numNodes);
		
		return tree;
	}

	private int generateTree(Node node,TreeNode tree, int currentLevel, int numNodes) {
		
		TreeNode treeNode = new TreeNode();
		MethodNode methodInfo = xml.getMethodInfoFromNode(node);
		treeNode.setNode(methodInfo);
		
		tree.getCalledMethods().add(treeNode);
		numNodes++;
		
		System.out.println("Num nodes: " + numNodes);
		System.out.println("Depth: " + currentLevel);
		System.out.println(methodInfo.getMethodName());
		
		if (currentLevel<DEFAULT_NUM_LEVELS_DEPTH){
			
			int i = 0;
			NodeList childs = xml.getChildsOfNode(node);
			while (numNodes<DEFAULT_NUM_NODES && i < childs.getLength()){
				numNodes = generateTree(childs.item(i),treeNode,currentLevel+1,numNodes);
				i++;
			}
			
		}

		return numNodes;
		
	}
	
}
