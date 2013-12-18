package com.javatracer.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.javatracer.view.TraceInpectorView;
import com.javatracer.view.tree.TextInBoxExt;
import com.traceinspector.datamodel.TreeNode;
import com.traceinspector.model.TreeGenerator;

public class TraceInspectorController implements MouseListener{

	TreeGenerator treeGenerator;
	TreeNode tree;
	TraceInpectorView view;
	
	TextInBoxExt lastSelected;
	
	public TraceInspectorController(){
		treeGenerator = new TreeGenerator();
		tree = treeGenerator.loadFromFile("trace.xml");
		view = new TraceInpectorView(tree,this);
		view.setVisible(true);
	}

	public void clickedOnNode(Point point) {
		
		TextInBoxExt textInBoxExt = view.clickedOnTree(point.x, point.y);
				
		if (lastSelected!=null)
			lastSelected.selected = false;
		
		if (textInBoxExt!=null){
			textInBoxExt.selected = true;
			view.loadNewVariables(tree.getVariablesFrom(textInBoxExt.getId()));
		}
		lastSelected = textInBoxExt;
		view.repaintTree();
	}

	public void doubleClickedOnNode(Point point) {
		
		TextInBoxExt textInBoxExt = view.clickedOnTree(point.x, point.y);
		
		if (textInBoxExt!=null){
			
			TreeNode node = tree.getNode(textInBoxExt.getId());
			
			if (!node.hasChilds()){
				treeGenerator.expandNode(node,textInBoxExt.getId());
				view.expandTreeNode(textInBoxExt,node);
			}
			else {
				treeGenerator.foldNode(node);
				view.foldNode(textInBoxExt);
			}
			
		}
	}

	public void mouseClicked(MouseEvent e) {
		
		if (e.getButton()==MouseEvent.BUTTON1){
			
			if (e.getClickCount()==2)
				doubleClickedOnNode(e.getPoint());				
			else
				clickedOnNode(e.getPoint());		
		}
	}

	public void mouseEntered(MouseEvent e){}

	public void mouseExited(MouseEvent e){}

	public void mousePressed(MouseEvent e){}

	public void mouseReleased(MouseEvent e){}

	public void clickedOpen(){
		System.out.println("olaf q ase");
	}
	
}
