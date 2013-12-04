package com.traceinspector.model;

import java.util.ArrayList;
import java.util.List;

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
		
		Configuration configuration = new Configuration();
		DEFAULT_NUM_LEVELS_DEPTH = configuration.getDefaultNumLevelsDepth();
		DEFAULT_NUM_NODES = configuration.getDefaultNumNodes();
		
		int currentLevel = 0;
		int numNodes = 0;
		TreeNode tree = new TreeNode();
		
		Node rootNode = xml.getRootNode();
		
		generateTree(rootNode,tree,currentLevel, numNodes);
		
		return tree;
	}

	private int generateTree(Node node,TreeNode tree, int currentLevel, int numNodes) {
		
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

	public TreeNode expandTreeWithNode(TreeNode tree, long id) {
		TreeNode nodeToExpand = tree.searchNode(tree,id);
		if (nodeToExpand != null){
			List<TreeNode> childs = loadChildsOfNode(id);
			nodeToExpand.addChilds(childs);
		}
		
		return nodeToExpand;
	}

	private List<TreeNode> loadChildsOfNode(long id) {
		List<TreeNode> childs = new ArrayList<>();
		
		Node node = xml.getNode(id);
		NodeList xmlNodechilds = xml.getChildsOfNode(node);
		
		for (int i=0;i<xmlNodechilds.getLength();i++){
			TreeNode treeNode = new TreeNode(xml.getMethodInfoFromNode(xmlNodechilds.item(i)));
			childs.add(treeNode);
		}
		
		return childs;
	}
	
}
