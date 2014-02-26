package com.inspector.treeinspector.model;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D.Double;
import java.util.*;
import java.util.Map.Entry;

import com.inspector.controller.InspectorController;
import com.inspector.model.TreeManager;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspectorview.DefaultTreeLayout;
import com.inspector.treeinspectorview.TreeInspectorView;

public class TreeInspector implements MouseListener {
		
	private InspectorController controller;
	private TreeInspectorView view;
	private TreeManager treeManager;
	
	private DefaultTreeLayout<Box> tree;
	private Box lastSelected;
	
	public TreeInspector(InspectorController controller, TreeManager treeManager){
		this.controller = controller;
		this.treeManager = treeManager;
		this.tree = treeManager.loadTree();
		this.view = new TreeInspectorView(tree, this);
		this.centerScroll();
        
        this.lastSelected = null;
	   
	 
	}
	
	public void centerScroll(){
		
		
			Rectangle bounds = view.getViewport().getViewRect();
		    Dimension size = view.getViewport().getViewSize();
		    
		    int x = (int) ((size.width - bounds.width) / 2.5);
		    int y = (int) ((size.height - bounds.height) / 2.5);
	        
		    view.getViewport().setViewPosition(new Point(x, y));
			
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
