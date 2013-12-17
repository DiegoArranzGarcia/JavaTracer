package com.javatracer.controller;

import java.awt.Point;

import com.javatracer.view.TreeInspector;
import com.javatracer.view.tree.TextInBoxExt;
import com.traceinspector.datamodel.TreeNode;
import com.traceinspector.model.TreeGenerator;

public class TreeController {

	TreeGenerator treeGenerator;
	TreeNode tree;
	TreeInspector view;
	
	TextInBoxExt lastSelected;
	
	public TreeController(){
		treeGenerator = new TreeGenerator();
		tree = treeGenerator.loadFromFile("trace.xml");
		view = new TreeInspector(tree,this);
		view.setVisible(true);
	}

	public void clickedOnNode(Point point) {
		
		TextInBoxExt textInBoxExt = view.clickedOnTree(point.x, point.y);
				
		if (lastSelected!=null)
			lastSelected.selected = false;
		
		if (textInBoxExt!=null)
			textInBoxExt.selected = true;
		
		lastSelected = textInBoxExt;
		view.repaintTree();
	}

	public void doubleClickedOnNode(Point point) {
		
		TextInBoxExt textInBoxExt = view.clickedOnTree(point.x, point.y);
		
		if (textInBoxExt!=null){
			
			TreeNode node = tree.getNode(tree,textInBoxExt.getId());
			
			if (!node.hasChilds()){
				treeGenerator.expandNode(node,textInBoxExt.getId());
				view.expandTreeNode(textInBoxExt,node);
			}
			else {
				treeGenerator.foldNode(node);
				view.foldNode(textInBoxExt);
			}
			
		}
	}
	
}
