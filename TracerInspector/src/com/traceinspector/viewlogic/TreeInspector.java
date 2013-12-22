package com.traceinspector.viewlogic;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.awt.geom.Rectangle2D.Double;

import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.treeinspectorview.DefaultTreeLayout;
import com.traceinspector.treeinspectorview.TextInBoxExt;
import com.traceinspector.view.TreeInspectorView;

public class TreeInspector implements MouseListener {
		
	private TraceInspectorController controller;
	private TreeInspectorView view;
	private TreeManager treeManager;
	
	private DefaultTreeLayout<TextInBoxExt> tree;
	private TextInBoxExt lastSelected;
	
	public TreeInspector(TraceInspectorController controller, TreeManager treeManager){
		this.controller = controller;
		this.treeManager = treeManager;
		this.tree = treeManager.loadTree();
		
		this.view = new TreeInspectorView(tree, this);
		this.lastSelected = null;
	}
	
	
	public void doubleClickedOnNode(TextInBoxExt box) {
			
		if (box!=null){
			
			if (box.isExpanded())
				fold(box);
			else
				expand(box);
			
		}	
	}

	private void expand(TextInBoxExt box) {
		
		treeManager.expandNode(tree,box);
		view.repaintTree(tree);
		
	}


	private void fold(TextInBoxExt box) {
		
		treeManager.foldNode(tree,box);
		view.repaintTree(tree);
		
	}


	public TreeInspectorView getView() {
		return view;
	}

	public void mouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1){
			
			TextInBoxExt box = clickedOnTree(e.getPoint());
			
			if (e.getClickCount() == 1)
				controller.clickedOnNode(box);		
			
			else if (e.getClickCount() == 2)
				doubleClickedOnNode(box);
										
		}
			
	}

	public TextInBoxExt clickedOnTree(Point clickPoint) {
		
		TextInBoxExt textInBoxExt = null;
		
		boolean clickedOnNode = false;
		Set<Entry<TextInBoxExt, Double>> nodes = view.getNodeBounds().entrySet();
		Iterator<Entry<TextInBoxExt,Double>> iterator = nodes.iterator();
		
		while (iterator.hasNext() && !clickedOnNode){
			Entry<TextInBoxExt,Double> entry = iterator.next();
			Double rec = entry.getValue();
			if (rec.contains(clickPoint)) {
				textInBoxExt = (TextInBoxExt)entry.getKey();
				clickedOnNode = true;
			}
		}
		
		return textInBoxExt;
	}
	
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void selectNode(TextInBoxExt box) {
		
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
