package com.inspector.objectinspector.view;

import java.util.ArrayList;
import java.util.List;

public class TableTreeNode {

	private Object userObject;
	private boolean expanded;
	private boolean isExpandable;
	private long depth;
	private List<TableTreeNode> children;
	private TableTreeNode parent;
	
	public TableTreeNode(Object data){
		this.userObject = data;
		this.expanded = false;
		this.children = new ArrayList<>();
		this.parent = null;
		this.depth = 0;
	}
		
	public Object getUserObject() {
		return userObject;
	}
	
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	public List<TableTreeNode> getChildren() {
		return children;
	}
	
	public void setChildren(List<TableTreeNode> children) {
		this.children = children;
	}
	
	public TableTreeNode getParent() {
		return parent;
	}
	
	public void setParent(TableTreeNode parent) {
		this.parent = parent;
	}

	public long getDepth() {
		return depth;
	}

	public void setDepth(long depth) {
		this.depth = depth;
	}
	
	public List<TableTreeNode> getVisibleNodes() {
		List<TableTreeNode> result = new ArrayList<>();
		getVisibleNodesRec(this,result);
		return result;
	}

	private void getVisibleNodesRec(TableTreeNode currentNode,List<TableTreeNode> result) {
		result.add(currentNode);
		if (currentNode.isExpanded()){
			List<TableTreeNode> children = currentNode.getChildren();
			for (int i=0;i<children.size();i++)
				getVisibleNodesRec(children.get(i),result);
		}
	}
	
	public List<TableTreeNode> getPreorder() {
		List<TableTreeNode> result = new ArrayList<>();
		getPreorderRec(this,result);
		return result;
	}

	private void getPreorderRec(TableTreeNode currentNode,List<TableTreeNode> result) {
		result.add(currentNode);
		List<TableTreeNode> children = currentNode.getChildren();
		for (int i=0;i<children.size();i++)
			getPreorderRec(children.get(i),result);
	}

	public void add(TableTreeNode node) {
		node.parent = this;
		node.depth = depth+1;
		this.children.add(node);
	}

	public void removeAllChildren() {
		this.children.clear();
	}

	public TableTreeNode getChildAt(int i) {
		return children.get(i);
	}

	public int getChildCount() {
		return children.size();
	}

	public boolean isExpandable() {
		return !children.isEmpty();
	}

	public void setExpandable(boolean isExpandable) {
		this.isExpandable = isExpandable;
	}
	
	
}
