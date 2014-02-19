package com.traceinspector.treeinspectorview;

import java.awt.Component;
import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import javax.swing.JScrollPane;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import com.traceinspector.treeinspector.data.MethodBox;
import com.traceinspector.treeinspector.logic.TreeInspector;

@SuppressWarnings("serial")
public class TreeInspectorView extends JScrollPane {

	TreeInspector controller;
	static double GAP_BETWEEN_LEVELS = 50;
	static double GAP_BETWEEN_NODES= 10;
	
	private static TreeLayout<MethodBox> treeLayout;
	
	static TextInBoxNodeExtentProvider nodeExtentProvider;
	static DefaultConfiguration<MethodBox> configuration;
	
	static TreePanel panel;
	
	public TreeInspectorView(DefaultTreeLayout<MethodBox> root,TreeInspector controller) {
		super(createTreePanel(root,controller));
		
		this.controller = controller;
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	
	}
	 
	public static Component createTreePanel(DefaultTreeLayout<MethodBox> root,TreeInspector controller) {
		
		//setup the tree layout configuration

		configuration = new DefaultConfiguration<MethodBox>(GAP_BETWEEN_LEVELS,GAP_BETWEEN_NODES);

		// create the NodeExtentProvider for TextInBox nodes
		nodeExtentProvider = new TextInBoxNodeExtentProvider();	
			
		//create the layout
		setTreeLayout(new TreeLayout<MethodBox>(root,nodeExtentProvider,configuration));

		// Create a panel that draws the nodes and edges and show the panel
		panel = new TreePanel(getTreeLayout());
		panel.addMouseListener(controller);
		
		return panel;

	}
	
	public void repaintTree(DefaultTreeLayout<MethodBox> tree) {
		
		setTreeLayout(new TreeLayout<MethodBox>(tree,nodeExtentProvider,configuration));
		
		panel.setTree(getTreeLayout());
		panel.repaint();
		
		this.updateUI();
	}

	public Map<MethodBox, Double> getNodeBounds() {
		return getTreeLayout().getNodeBounds();
	}

	public static TreeLayout<MethodBox> getTreeLayout() {
		return treeLayout;
	}

	public static void setTreeLayout(TreeLayout<MethodBox> treeLayout) {
		TreeInspectorView.treeLayout = treeLayout;
	}
	
}