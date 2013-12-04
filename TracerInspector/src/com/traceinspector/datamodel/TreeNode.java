package com.traceinspector.datamodel;

import java.util.ArrayList;
import java.util.List;

public class TreeNode{

	MethodNode node;
	List<TreeNode> calledMethods;
	
	public TreeNode(MethodNode methodName) {
		this.node = methodName;
		this.calledMethods = new ArrayList<>();
	}
	
	public TreeNode() {
		this.calledMethods = new ArrayList<>();
	}
	
	/**
	 * @return the node
	 */
	
	public MethodNode getNode() {
		return node;
	}
	/**
	 * @return the calledMethods
	 */
	
	public List<TreeNode> getCalledMethods() {
		return calledMethods;
	}
	
	/**
	 * @param node the node to set
	 */
	
	public void setNode(MethodNode node) {
		this.node = node;
	}
	
	/**
	 * @param calledMethods the calledMethods to set
	 */
	
	public void setCalledMethods(List<TreeNode> calledMethods) {
		this.calledMethods = calledMethods;
	}
	
	public TreeNode searchNode(TreeNode tree,long id){
		TreeNode node = null;
		if (tree.node.getId()==id)
			node = tree;
		else {
			
			boolean found = false;
			int i = 0;
			List<TreeNode> childs = tree.calledMethods;
			
			while (!found && i<childs.size()){
				node = searchNode(childs.get(i), id);
				found = (node != null);
				i++;
			}
			
			if (!found) node = null;
						
		}
		
		return node;
	}

	public void addChilds(List<TreeNode> childs) {
		this.calledMethods.addAll(childs);		
	}
	
}
