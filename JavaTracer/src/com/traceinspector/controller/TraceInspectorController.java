package com.traceinspector.controller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.general.model.data.MethodInfo;
import com.traceinspector.model.TreeManager;
import com.traceinspector.objectinspector.logic.ObjectInspector;
import com.traceinspector.treeinspector.data.Box;
import com.traceinspector.treeinspector.data.MethodBox;
import com.traceinspector.treeinspector.logic.TreeInspector;
import com.traceinspector.view.TraceInpectorView;

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
	    FileNameExtensionFilter filtroXml=new FileNameExtensionFilter("xml","xml");
	    chooser.setFileFilter(filtroXml);
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

	public void clickedOnNode(Box box) {
		
		treeInspector.selectNode(box);
		
		objectInspector.clear();
		
		if (box!=null && box instanceof MethodBox){
			MethodInfo method = ((MethodBox)box).getMethodInfo(); 
			objectInspector.addNodeInfo(method);
		}
	}
	
}
