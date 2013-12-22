package com.traceinspector.view;

import java.awt.Component;
import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import javax.swing.JScrollPane;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import com.javatracer.treeinspectorview.tree.DefaultTreeLayout;
import com.javatracer.treeinspectorview.tree.TextInBoxExt;
import com.javatracer.treeinspectorview.tree.TextInBoxNodeExtentProvider;
import com.javatracer.treeinspectorview.tree.TreePanel;
import com.traceinspector.viewlogic.TreeInspector;

@SuppressWarnings("serial")
public class TreeInspectorView extends JScrollPane {

	TreeInspector controller;
	
	private static TreeLayout<TextInBoxExt> treeLayout;
	
	static TextInBoxNodeExtentProvider nodeExtentProvider;
	static DefaultConfiguration<TextInBoxExt> configuration;
	
	static TreePanel panel;
	
	public TreeInspectorView(DefaultTreeLayout<TextInBoxExt> root,TreeInspector controller) {
		super(createTreePanel(root,controller));
		
		this.controller = controller;
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
	}
	 
	public static Component createTreePanel(DefaultTreeLayout<TextInBoxExt> root,TreeInspector controller) {
		
		//setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		configuration = new DefaultConfiguration<TextInBoxExt>(gapBetweenLevels, gapBetweenNodes);

		// create the NodeExtentProvider for TextInBox nodes
		nodeExtentProvider = new TextInBoxNodeExtentProvider();	
			
		//create the layout
		setTreeLayout(new TreeLayout<TextInBoxExt>(root,nodeExtentProvider,configuration));

		// Create a panel that draws the nodes and edges and show the panel
		panel = new TreePanel(getTreeLayout());
		panel.addMouseListener(controller);
		
		return panel;

	}
	
	public void repaintTree(DefaultTreeLayout<TextInBoxExt> tree) {
		
		setTreeLayout(new TreeLayout<TextInBoxExt>(tree,nodeExtentProvider,configuration));
		
		panel.setTree(getTreeLayout());
		panel.repaint();
		
		this.updateUI();
	}

	public Map<TextInBoxExt, Double> getNodeBounds() {
		return getTreeLayout().getNodeBounds();
	}

	public static TreeLayout<TextInBoxExt> getTreeLayout() {
		return treeLayout;
	}

	public static void setTreeLayout(TreeLayout<TextInBoxExt> treeLayout) {
		TreeInspectorView.treeLayout = treeLayout;
	}
}