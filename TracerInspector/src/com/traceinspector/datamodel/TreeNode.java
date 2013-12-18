package com.traceinspector.datamodel;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.data.VariableInfo;

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
	
	public TreeNode getNode(long id){
		TreeNode node = null;
		if (this.node.getId()==id)
			node = this;
		else {
			boolean found = false;
			int i = 0;
			List<TreeNode> childs = this.calledMethods;
			
			while (!found && i<childs.size()){
				node = childs.get(i).getNode(id);
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

	public boolean hasChilds() {
		return (calledMethods!=null && calledMethods.size()>0);
	}

	public void clearCalledMethods() {
		calledMethods.clear();		
	}

	public List<VariableInfo> getVariablesFrom(long id) {
		
		List<VariableInfo> variables = new ArrayList<VariableInfo>();
		
		TreeNode node = getNode(id);
		if (node!=null) 
			variables = node.getNode().getVariables();
		
		return variables;
	}
	
}
