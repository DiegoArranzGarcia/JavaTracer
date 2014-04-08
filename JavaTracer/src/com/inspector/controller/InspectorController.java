package com.inspector.controller;

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
import com.inspector.objectinspector.controller.ObjectInspectorController;
import com.inspector.treeinspector.controller.TreeInspectorController;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.data.MethodBox;
import com.inspector.treeinspector.data.ThreadBox;
import com.inspector.treeinspector.view.DefaultTreeLayout;
import com.inspector.view.InspectorLoadingView;
import com.inspector.view.InspectorView;

public class InspectorController {

	private JavaTracerPresenter controller;
	private InspectorView view;
	private InspectorLoadingView loadingView;
	
	private TreeInspectorController treeInspector;
	private ObjectInspectorController objectInspector;
	
	private	TreeManager treeManager;
	
	
	public InspectorController(){
		treeManager = new TreeManager();
		treeInspector = new TreeInspectorController(treeManager);
		objectInspector = new ObjectInspectorController(treeManager);
		treeInspector.setController(this);
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
    
    public void updateInfo(int numNodes, int total,int percentage,XmlManager xml) {
    	
    	if(total==0)
    		{total=getNumberNodes(xml);
    		percentage=(numNodes*100)/total;
    		}
    		
    	   
    	
    	if (loadingView != null)
    		loadingView.updateInfo(numNodes,total,percentage);
    	
	}

	private void createLoadingView() {
		if (loadingView == null){
			loadingView = new InspectorLoadingView(view);
		}
		loadingView.setPercentage(0);
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
