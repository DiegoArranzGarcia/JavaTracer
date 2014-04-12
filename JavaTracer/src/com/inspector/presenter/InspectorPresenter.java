package com.inspector.presenter;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;

import com.general.model.FileUtilities;
import com.general.model.data.MethodInfo;
import com.general.presenter.JavaTracerPresenter;
import com.inspector.model.TreeManager;
import com.inspector.model.XmlManager;
import com.inspector.objectinspector.presenter.ObjectInspectorPresenter;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.data.MethodBox;
import com.inspector.treeinspector.presenter.TreeInspectorPresenter;
import com.inspector.view.*;

public class InspectorPresenter implements InspectorPresenterInterface {

	private JavaTracerPresenter controller;
	private InspectorViewInterface view;
	private InspectorLoadingView loadingView;
	
	private TreeInspectorPresenter treeInspector;
	private ObjectInspectorPresenter objectInspector;
	
	private	TreeManager treeManager;
	
	
	public InspectorPresenter(){
		treeManager = new TreeManager();
		treeInspector = new TreeInspectorPresenter(treeManager);
		objectInspector = new ObjectInspectorPresenter(treeManager);
		treeInspector.setPresenter(this);
		objectInspector.setController(this);
		treeManager.setController(this);
		objectInspector.showTable();
		}

	public void clickedOnLoadTrace(JFrame parent){
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filtroXml=new FileNameExtensionFilter("xml","xml");
	    chooser.setFileFilter(filtroXml);
		//Title window
		chooser.setDialogTitle("Open trace");
		chooser.setCurrentDirectory(new File(FileUtilities.CURRENT_DIR));
		chooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		//return directory file
		
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			try {
				String xmlName = chooser.getSelectedFile().getCanonicalPath();
				open(xmlName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			chooser.cancelSelection();	
		}
		
	}
	
	public void open(String xmlPath) {
		
		if (view == null){
			view = new InspectorView(treeInspector,objectInspector);
			view.setTitle(new File(xmlPath).getName());
			view.setController(this);
		}
		view.setVisible(true);
		//controller.setVisible(false);
		
		treeManager.showTree(xmlPath);	
		createLoadingView();
	}
    
    public void finishLoading(){
    	treeInspector.showTree();
    	if (loadingView != null)
    		loadingView.dispose();
    }
    
    public void updateInfo(int numNodes) {
    	    		   	
    	if (loadingView != null)
    		loadingView.updateInfo(numNodes);
    	
	}

	private void createLoadingView() {
		if (loadingView == null){
			loadingView = new InspectorLoadingView(view.getView());
		}
		loadingView.reset();
		loadingView.setVisible(true);
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
		//controller.back();		
	}

	public void opening() {
		if (loadingView != null)
			loadingView.opening();
	}

	public void clickedOnSettings() {
		controller.clickedOnSettings();
	}

	public void clickedOnHelp() {
		controller.clickedOnHelp();
	}

	public void clickedOnAbout() {
		controller.clickedOnAbout();
	}

	private int getNumberNodes(XmlManager xml){
		int i=0;
		
		
		Node node=xml.getNode(i);
		
		while(node!=null){
			i++;
			node=xml.getNode(i);
		}
		
		
	 return i+1;
	}


	
}
