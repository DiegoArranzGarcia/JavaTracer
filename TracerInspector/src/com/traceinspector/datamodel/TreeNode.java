package com.traceinspector.datamodel;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	MethodNode node;
	List<TreeNode> calledMethods;
	
	public TreeNode(MethodNode methodName) {
		this.node = methodName;
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
	
}
