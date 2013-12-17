package com.javatracer.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.javatracer.controller.TreeController;
import com.javatracer.view.tree.TextInBoxExt;
import com.traceinspector.datamodel.TreeNode;

@SuppressWarnings("serial")
public class TraceInpectorView extends JFrame{
	
	private TreeInspector treeInspector;
	private ObjectInspector objectInspector;

	public TraceInpectorView(TreeNode tree, TreeController treeController) {
		
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TraceInspector");
		setLayout(new BorderLayout());
		
		treeInspector = new TreeInspector(tree, treeController);
		objectInspector = new ObjectInspector(this.getPreferredSize().height);
		
	    add(treeInspector,BorderLayout.WEST);
	    add(objectInspector,BorderLayout.EAST);
	}

	public TextInBoxExt clickedOnTree(int x, int y) {
		return treeInspector.clickedOnTree(x, y);
	}

	public void repaintTree() {
		treeInspector.repaintTree();
	}

	public void expandTreeNode(TextInBoxExt textInBoxExt, TreeNode node) {
		treeInspector.expandTreeNode(textInBoxExt, node);
	}

	public void foldNode(TextInBoxExt textInBoxExt) {
		treeInspector.foldNode(textInBoxExt);
	}

}
