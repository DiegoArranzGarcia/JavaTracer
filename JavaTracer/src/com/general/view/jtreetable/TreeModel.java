package com.general.view.jtreetable;

import java.util.List;


public class TreeModel {

	private TableTreeNode rootNode;
	
	public TreeModel(TableTreeNode rootNode) {
		this.rootNode = rootNode;
		rootNode.setExpanded(true);
	}
	
	public TableTreeNode getNodeFromRow(int numRow){
		
		List<TableTreeNode> nodes = rootNode.getVisibleNodes();
		return nodes.get(numRow);
	}

	public boolean isExpanded(int numRow) {
		TableTreeNode node = getNodeFromRow(numRow);
		return node.isExpanded();
	}

	public void expandRow(int numRow) {
		TableTreeNode node = getNodeFromRow(numRow);
		node.setExpanded(true);
	}

	public TableTreeNode getRoot() {
		return rootNode;
	}

	public void collapseRow(int numRow) {
		TableTreeNode node = getNodeFromRow(numRow);
		node.setExpanded(false);
		List<TableTreeNode> preorder = node.getPreorder();
		for (int i=0;i<preorder.size();i++)
			preorder.get(i).setExpanded(false);
	}

	public void removeRoot() {
		this.rootNode = null;
	}

	public void removeAllUnlessRoot() {
		rootNode.removeAllChildren();
	}
	
}
