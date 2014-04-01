package com.inspector.model;

import com.general.model.data.MethodInfo;
import com.inspector.controller.InspectorController;
import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.data.MethodBox;
import com.inspector.treeinspector.view.DefaultTreeLayout;

public class TreeManager implements UpdateNotifier {

	private DefaultTreeLayout<Box> tree;
	
	private XmlManager xml;
	private InspectorController controller;
		
	public void showTree(String xmlName) {
		
		LoadTreeThread loadTreeThread = new LoadTreeThread(xmlName,this);
		loadTreeThread.start();
		
	}

	public void expandNode(Box box) {
		int i = 1;
		int childs = xml.getNumChildrenOfNode(box);
		
		while (i <= childs){
			
			String path = xml.getPath(box,i);
			
			MethodInfo nodeInfo = xml.getBoxFromNode(path);
			long id = xml.getIdFromNode(path);
			boolean haveChildren = xml.hasChildrenNode(path);
			MethodBox node = new MethodBox(path,id,nodeInfo,haveChildren);
			
			tree.addChild(box,node);
			
			i++;
		}
		
		box.setLoaded(true);
	}

	public DefaultTreeLayout<Box> getTree() {
		return tree;
	}
	
	public void setController(InspectorController controller){
		this.controller = controller;
	}

	public void updateInfo(int current, int total, int percentage) {
		controller.updateInfo(current, total, percentage);
	}

	public void finishLoading(DefaultTreeLayout<Box> loadedTree) {
		this.tree = loadedTree;
		controller.finishLoading();
	}

	public void opening() {
		controller.opening();
	}

	public void opened(XmlManager xml) {
		this.xml = xml;
	}

}
