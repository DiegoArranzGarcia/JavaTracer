package com.javatracer.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
		view = new TraceInpectorView(this);
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
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filtroImagen=new FileNameExtensionFilter("xml","xml");
	    chooser.setFileFilter(filtroImagen);
		//Title window
		chooser.setDialogTitle("Java Tracer");
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		//return directory file
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				tree = treeGenerator.loadFromFile(chooser.getSelectedFile().getCanonicalPath());
				view.createTree(tree);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else chooser.cancelSelection();
		
	}
	
}
