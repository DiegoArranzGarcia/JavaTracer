package com.javatracer.controller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.javatracer.treeinspectorview.tree.TextInBoxExt;
import com.traceinspector.model.TreeManager;
import com.traceinspector.view.TraceInpectorView;
import com.traceinspector.viewlogic.ObjectInspector;
import com.traceinspector.viewlogic.TreeInspector;

public class TraceInspectorController {

	TraceInpectorView view;
	
	TreeInspector treeInspector;
	ObjectInspector objectInspector;
	
	TreeManager treeManager;
	
	public TraceInspectorController(){
		view = new TraceInpectorView(this);
		view.setVisible(true);
	}
	
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
				
				treeManager = new TreeManager(chooser.getSelectedFile().getCanonicalPath());
				
				treeInspector =  new TreeInspector(this,treeManager);				
				view.loadTreeInspector(treeInspector.getView());
				
				objectInspector = new ObjectInspector(this,treeManager);
				view.loadObjectInspector(objectInspector.getView());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else chooser.cancelSelection();	
	}

	public void clickedOnNode(TextInBoxExt box) {
		
		treeInspector.selectNode(box);
		
		objectInspector.clear();
		
		if (box!=null)
			objectInspector.addVariables(box);
		
	}
	
}
