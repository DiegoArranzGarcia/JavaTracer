package com.inspector.controller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.general.model.data.MethodInfo;
import com.general.presenter.JavaTracerPresenter;
import com.inspector.model.TreeManager;
import com.inspector.objectinspector.controller.ObjectInspectorController;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.data.MethodBox;
import com.inspector.treeinspector.model.TreeInspector;
import com.inspector.view.InpectorView;

public class InspectorController {

	private JavaTracerPresenter controller;
	private InpectorView view;
	
	private TreeInspector treeInspector;
	private ObjectInspectorController objectInspector;
	
	private	TreeManager treeManager;

	public void open() {
		this.view = new InpectorView();
		view.setController(this);
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
				
				objectInspector = new ObjectInspectorController(this,treeManager);
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
	
	public void setController(JavaTracerPresenter javaTracerController) {
		this.controller = javaTracerController;
	}

	public void back() {
		view.setVisible(false);
		controller.back();		
	}

}
