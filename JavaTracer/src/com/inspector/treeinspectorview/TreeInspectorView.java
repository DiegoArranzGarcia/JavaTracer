package com.inspector.treeinspectorview;

import java.awt.Component;
import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import javax.swing.JScrollPane;

import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.model.TreeInspector;

@SuppressWarnings("serial")
public class TreeInspectorView extends JScrollPane {

	static double GAP_BETWEEN_LEVELS = 50;
	static double GAP_BETWEEN_NODES= 10;
	
	private static TreeLayout<Box> treeLayout;
	
	static NodeExtentProvider<Box> nodeExtentProvider;
	static DefaultConfiguration<Box> configuration;
	
	static TreePanel panel;
	
	public TreeInspectorView(DefaultTreeLayout<Box> root,TreeInspector controller) {
		super(createTreePanel(root,controller));
		
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	
	}
	 
	public static Component createTreePanel(DefaultTreeLayout<Box> root,TreeInspector controller) {
		
		//setup the tree layout configuration

		configuration = new DefaultConfiguration<Box>(GAP_BETWEEN_LEVELS,GAP_BETWEEN_NODES);

		// create the NodeExtentProvider for TextInBox nodes
		nodeExtentProvider = new TextInBoxNodeExtentProvider();	
			
		//create the layout
		setTreeLayout(new TreeLayout<Box>(root,nodeExtentProvider,configuration));

		// Create a panel that draws the nodes and edges and show the panel
		panel = new TreePanel(getTreeLayout());
		panel.addMouseListener(controller);
		
		return panel;

	}
	
	public void repaintTree(DefaultTreeLayout<Box> tree) {
		
		setTreeLayout(new TreeLayout<Box>(tree,nodeExtentProvider,configuration));
		
		panel.setTree(getTreeLayout());
		panel.repaint();
		
		this.updateUI();
	}

	public Map<Box, Double> getNodeBounds() {
		return getTreeLayout().getNodeBounds();
	}

	public static TreeLayout<Box> getTreeLayout() {
		return treeLayout;
	}

	public static void setTreeLayout(TreeLayout<Box> treeLayout) {
		TreeInspectorView.treeLayout = treeLayout;
	}
	
}