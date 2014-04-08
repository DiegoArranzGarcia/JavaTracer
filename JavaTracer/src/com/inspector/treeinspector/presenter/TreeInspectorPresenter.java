package com.inspector.treeinspector.presenter;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.inspector.model.TreeManager;
import com.inspector.presenter.InspectorPresenter;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.view.TreeInspectorView;
import com.inspector.treeinspector.view.TreeInspectorViewInterface;

public class TreeInspectorPresenter implements MouseListener, TreeInspectorPresenterInterface{
		
	private InspectorPresenter controller;
	private TreeInspectorViewInterface view;
	private TreeManager treeManager;
	
	private Box lastSelected;
	
	public TreeInspectorPresenter(TreeManager treeManager){
		
		this.treeManager = treeManager;
		this.lastSelected = null;
		
		//centerScroll();
	}
	
	public void centerScroll(){
		
		/*
		Rectangle bounds = view.getViewport().getViewRect();
		Dimension size = view.getViewport().getViewSize();
		    
		int x = (int) ((size.width - bounds.width) / 2.5);
		int y = (int) ((size.height - bounds.height) / 2.5);
	        
		view.getViewport().setViewPosition(new Point(x, y));
		*/
	}
	
	public void doubleClickedOnNode(Box box) {
			
		if (box!=null){
			
			if (!box.isLoaded() || !box.isExpanded())
				expand(box);
			else 
				fold(box);
			    
		}	
	}

	private void expand(Box box) {
		
		if (!box.isLoaded())
			treeManager.expandNode(box);
		
		Iterator<Box> iterator = treeManager.getTree().getChildren(box).iterator();
		box.setExpanded(true);
		
		while (iterator.hasNext()){
			iterator.next().setVisible(true);
		}
		
		view.repaintTree(treeManager.getTree());
		
	}

	private void fold(Box box) {	

		Iterator<Box> iterator = treeManager.getTree().getChildren(box).iterator();
		box.setExpanded(false);
		
		while (iterator.hasNext()){
			Box child = iterator.next();
			child.setVisible(false);
			fold(child);			
		}
		
		view.repaintTree(treeManager.getTree());
	}


	public TreeInspectorViewInterface getView() {
		if (view == null){
			view = new TreeInspectorView();
			view.setController(this);
		}
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
			view.repaintTree(treeManager.getTree());
		
	}

	public void setController(InspectorPresenter inspectorController) {
		this.controller = inspectorController;
	}

	public void showTree(){
		if (view == null){
			view = new TreeInspectorView();
			view.setController(this);
		}
		view.repaintTree(treeManager.getTree());
	}
	
}
