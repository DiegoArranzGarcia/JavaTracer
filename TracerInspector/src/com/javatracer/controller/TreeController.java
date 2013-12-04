package com.javatracer.controller;

import com.javatracer.view.TextInBoxExt;
import com.javatracer.view.TreeInspector;
import com.traceinspector.datamodel.TreeNode;
import com.traceinspector.model.TreeGenerator;

public class TreeController {

	TreeGenerator treeGenerator;
	TreeNode tree;
	TreeInspector view;
	
	public TreeController(){
		treeGenerator = new TreeGenerator();
		tree = treeGenerator.loadFromFile("trace.xml");
		view = new TreeInspector(tree,this);
	}

	public void expandTree(TextInBoxExt textInBoxExt) {
		TreeNode node = treeGenerator.expandTreeWithNode(tree,textInBoxExt.getId());
		view.expandTreeNode(textInBoxExt,node);
	}
	
}
