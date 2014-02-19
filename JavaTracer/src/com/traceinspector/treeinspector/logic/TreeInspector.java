package com.traceinspector.treeinspector.logic;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.awt.geom.Rectangle2D.Double;

import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.treeinspector.data.MethodBox;
import com.traceinspector.treeinspectorview.DefaultTreeLayout;
import com.traceinspector.treeinspectorview.TreeInspectorView;

public class TreeInspector implements MouseListener {
		
	private TraceInspectorController controller;
	private TreeInspectorView view;
	private TreeManager treeManager;
	
	private DefaultTreeLayout<MethodBox> tree;
	private MethodBox lastSelected;
	
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
	
	
	public void doubleClickedOnNode(MethodBox box) {
			
		if (box!=null){
			
			if (box.isExpanded())
				fold(box);
			else
				expand(box);
			
		}	
	}

	private void expand(MethodBox box) {
		
		treeManager.expandNode(tree,box);
		view.repaintTree(tree);
		
	}


	private void fold(MethodBox box) {	
		treeManager.foldNode(tree,box);
		view.repaintTree(tree);
	}


	public TreeInspectorView getView() {
		return view;
	}

	public void mouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1){
			
			MethodBox box = clickedOnTree(e.getPoint());
			
			if (e.getClickCount() == 1)
				controller.clickedOnNode(box);		
			
			else if (e.getClickCount() == 2)
				doubleClickedOnNode(box);
										
		}
			
	}

	public MethodBox clickedOnTree(Point clickPoint) {
		
		MethodBox textInBoxExt = null;
		
		boolean clickedOnNode = false;
		Set<Entry<MethodBox, Double>> nodes = view.getNodeBounds().entrySet();
		Iterator<Entry<MethodBox,Double>> iterator = nodes.iterator();
		
		while (iterator.hasNext() && !clickedOnNode){
			Entry<MethodBox,Double> entry = iterator.next();
			Double rec = entry.getValue();
			if (rec.contains(clickPoint)) {
				textInBoxExt = (MethodBox)entry.getKey();
				clickedOnNode = true;
			}
		}
		
		return textInBoxExt;
	}
	
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void selectNode(MethodBox box) {
		
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
