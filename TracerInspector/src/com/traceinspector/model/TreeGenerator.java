package com.traceinspector.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.javatracer.model.configuration.Configuration;
import com.traceinspector.datamodel.MethodNode;
import com.traceinspector.datamodel.TreeNode;

public class TreeGenerator {

	private int DEFAULT_NUM_LEVELS_DEPTH;
	private int DEFAULT_NUM_NODES;
	
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
		Configuration configuration = new Configuration();
		DEFAULT_NUM_LEVELS_DEPTH = configuration.getDefaultNumLevelsDepth();
		DEFAULT_NUM_NODES = configuration.getDefaultNumNodes();
		
		MethodNode methodInfo = xml.getMethodInfoFromNode(node);
		tree.setNode(methodInfo);		
		numNodes++;
		
		if (currentLevel<DEFAULT_NUM_LEVELS_DEPTH){
			
			int i = 0;
			NodeList childs = xml.getChildsOfNode(node);
			while (numNodes<DEFAULT_NUM_NODES && i < childs.getLength()){
				TreeNode treeChild = new TreeNode();
				numNodes = generateTree(childs.item(i),treeChild,currentLevel+1,numNodes);
				tree.getCalledMethods().add(treeChild);
				i++;
			}
			
		}

		return numNodes;
		
	}
	
}
