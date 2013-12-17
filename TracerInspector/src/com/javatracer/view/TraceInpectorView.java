package com.javatracer.view;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import com.javatracer.controller.TreeController;
import com.javatracer.view.tree.TextInBoxExt;
import com.traceinspector.datamodel.TreeNode;

@SuppressWarnings("serial")
public class TraceInpectorView extends JFrame implements ComponentListener{
	
	private TreeInspector treeInspector;
	private ObjectInspector objectInspector;
	private JSplitPane split;
	
	public TraceInpectorView(TreeNode tree, TreeController treeController) {
		
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TraceInspector");
		addComponentListener(this);
		
		treeInspector = new TreeInspector(tree, treeController);
		objectInspector = new ObjectInspector();
		
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(treeInspector);
		split.setRightComponent(objectInspector);
		split.setEnabled(false);
				
		setContentPane(split);
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

	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
		split.setDividerLocation(0.7f);
	}

	@Override
	public void componentShown(ComponentEvent e) {}

}
