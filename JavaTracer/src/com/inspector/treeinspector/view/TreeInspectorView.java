package com.inspector.treeinspector.view;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.util.DefaultConfiguration;

import com.inspector.treeinspector.view.TreeLayout;
import com.inspector.treeinspector.controller.TreeInspectorController;
import com.inspector.treeinspector.data.Box;

@SuppressWarnings("serial")
public class TreeInspectorView extends TreePanel {

	public static double GAP_BETWEEN_LEVELS = 50;
	public static double GAP_BETWEEN_NODES= 10;
		
	private NodeExtentProvider<Box> nodeExtentProvider;
	private DefaultConfiguration<Box> configuration;
	
	public TreeInspectorView(){
		configuration = new DefaultConfiguration<Box>(GAP_BETWEEN_LEVELS,GAP_BETWEEN_NODES);
		nodeExtentProvider = new TextInBoxNodeExtentProvider();	
	}
			 	
	public void repaintTree(DefaultTreeLayout<Box> tree) {
		
		treeLayout = new TreeLayout<Box>(tree,nodeExtentProvider,configuration);
		setTree(treeLayout);
		updateUI();
		
	}

	public Map<Box, Double> getNodeBounds() {
		return getTreeLayout().getNodeBounds();
	}

	public TreeLayout<Box> getTreeLayout() {
		return treeLayout;
	}

	public void setTreeLayout(TreeLayout<Box> treeLayout) {
		this.treeLayout = treeLayout;
	}

	public void setController(TreeInspectorController treeInspector) {
		addMouseListener(treeInspector);
	}
	
}