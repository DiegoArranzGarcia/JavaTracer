package com.traceinspector.treeinspector.logic;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.treeinspector.data.Box;
import com.traceinspector.treeinspectorview.DefaultTreeLayout;
import com.traceinspector.treeinspectorview.TreeInspectorView;

public class TreeInspector implements MouseListener {
		
	private TraceInspectorController controller;
	private TreeInspectorView view;
	private TreeManager treeManager;
	
	private DefaultTreeLayout<Box> tree;
	private Box lastSelected;
	
	public TreeInspector(TraceInspectorController controller, TreeManager treeManager){
		this.controller = controller;
		this.treeManager = treeManager;
		this.tree = treeManager.loadTree();
		this.view = new TreeInspectorView(tree, this);
		
		///////center scroll ////////////////
		Rectangle bounds = view.getViewport().getViewRect();
	    Dimension size = view.getViewport().getViewSize();
	    
	    int x = (size.width - bounds.width) / 3;
	    int y = (size.height - bounds.height) / 3;
        
	    view.getViewport().setViewPosition(new Point(x, y));
		///////////////////////////////////////
        
        this.lastSelected = null;
	   
	 
	}
	
	
	public void doubleClickedOnNode(Box box) {
			
		if (box!=null){
			
			if (box.isExpanded())
				fold(box);
			else
				expand(box);
			
		}	
	}

	private void expand(Box box) {
		
		treeManager.expandNode(tree,box);
		view.repaintTree(tree);
		
	}

	private void fold(Box box) {	
		treeManager.foldNode(tree,box);
		view.repaintTree(tree);
	}


	public TreeInspectorView getView() {
		return view;
	}

	public void mouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1){
			
			Box box = clickedOnTree(e.getPoint());
			
			if (e.getClickCount() == 1)
				controller.clickedOnNode(box);		
			
			else if (e.getClickCount() == 2)
				doubleClickedOnNode(box);
										
		}
			
	}

	public Box clickedOnTree(Point clickPoint) {
		
		Box textInBoxExt = null;
		
		boolean clickedOnNode = false;
		Set<Entry<Box, Double>> nodes = view.getNodeBounds().entrySet();
		Iterator<Entry<Box,Double>> iterator = nodes.iterator();
		
		while (iterator.hasNext() && !clickedOnNode){
			Entry<Box,Double> entry = iterator.next();
			Double rec = entry.getValue();
			if (rec.contains(clickPoint)) {
				textInBoxExt = (Box)entry.getKey();
				clickedOnNode = true;
			}
		}
		
		return textInBoxExt;
	}
	
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void selectNode(Box box) {
		
		boolean needRepaint = false;
		
		if (lastSelected != null){
			lastSelected.setSelected(false);
			needRepaint = true;
		}
		
		if (box != null){
			box.setSelected(true);
			needRepaint = true;
		}
		
		lastSelected = box;
		
		if (needRepaint) 
			view.repaintTree(tree);
		
	}

	
}
