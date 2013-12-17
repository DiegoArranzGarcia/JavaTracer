package com.javatracer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import com.javatracer.controller.TraceInspectorController;
import com.javatracer.view.tree.TextInBoxExt;
import com.traceinspector.datamodel.TreeNode;

@SuppressWarnings("serial")
public class TraceInpectorView extends JFrame implements ComponentListener,ActionListener{
	
	private TreeInspector treeInspector;
	private TraceInspectorController controller;
	private ObjectInspector objectInspector;
	private JSplitPane split;
	private JMenuItem[] items;
	
	public TraceInpectorView(TreeNode tree, TraceInspectorController controller) {
		
		this.controller = controller;
		
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TraceInspector");
		addComponentListener(this);
		
		treeInspector = new TreeInspector(tree, controller);
		objectInspector = new ObjectInspector();
		
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(treeInspector);
		split.setRightComponent(objectInspector);
		split.setEnabled(false);
				
		setContentPane(split);
		
		createMenu(controller);
	}	

	private void createMenu(TraceInspectorController controller) {
		JMenuBar menuBar = new JMenuBar();
		items = new JMenuItem[1];
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem menuOpen = new JMenuItem("Open");
		menuFile.add(menuOpen,0);
		items[0] = menuOpen;
		
		setJMenuBar(menuBar);
		
		menuOpen.addActionListener(this);
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
		split.setDividerLocation(0.75f);
	}

	public void componentShown(ComponentEvent e) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == items[0]){
			controller.clickedOpen();
		}
	}

}
