package com.inspector.treeinspector.view;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.util.DefaultConfiguration;

import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.presenter.TreeInspectorPresenter;

@SuppressWarnings("serial")
public class TreeInspectorView extends TreePanel implements TreeInspectorViewInterface {

	public static double GAP_BETWEEN_LEVELS = 50;
	public static double GAP_BETWEEN_NODES= 10;
		
	private NodeExtentProvider<Box> nodeExtentProvider;
	private DefaultConfiguration<Box> configuration;
	
	public TreeInspectorView(){
		configuration = new DefaultConfiguration<Box>(GAP_BETWEEN_LEVELS,GAP_BETWEEN_NODES);
		nodeExtentProvider = new BoxExtentProvider();	
	}
			 	
	public void repaintTree(DefaultTreeLayout<Box> tree) {
		
		treeLayout = new TreeLayout<Box>(tree,nodeExtentProvider,configuration);
		setTree(treeLayout);
		
		updateUI();
		
		setBackground(Color.WHITE); 
		
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

	public void setController(TreeInspectorPresenter treeInspector) {
		addMouseListener(treeInspector);
	}

	public TreeInspectorView getView() {
		return this;
	}
	
}