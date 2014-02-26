package com.inspector.objectinspector.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.tree.DefaultMutableTreeNode;

import com.general.model.data.ChangeInfo;
import com.general.model.data.MethodInfo;
import com.general.model.variables.data.Data;
import com.general.model.variables.data.InfoVisitor;
import com.inspector.controller.InspectorController;
import com.inspector.model.TreeManager;
import com.inspector.objectinspector.view.ObjectInspectorView;
import com.inspector.objectinspector.view.TableRowData;

public class ObjectInspector {
	
	private InspectorController controller;
	private TreeManager treeManager;
	
	private List<Data> variables;
	private ObjectInspectorView view;
	
	public ObjectInspector(InspectorController controller, TreeManager treeManager){
		
		this.controller = controller;
		this.treeManager = treeManager;
		
		this.variables =  new ArrayList<>();
		this.view = new ObjectInspectorView(this);
	}
	
	public ObjectInspectorView getView(){
		return view;
	}
	
	public void clear() {
		view.clear();
	}

	public void addNodeInfo(MethodInfo method) {
		
		InfoVisitor visitor = new ObjectInspectorVisitor(view);
		List<Data> arguments = method.getArguments();
		Data thisValue = method.getThis_data();
		Data returnValue = method.getReturn_data();
		
		
		variables.add(thisValue);
		thisValue.accept(visitor);
		
		if (returnValue != null){
			returnValue.accept(visitor);
			variables.add(returnValue);
		}
		
		
		
		for (int i=0;i<arguments.size();i++){
			Data data = arguments.get(i);
			data.accept(visitor);
			variables.add(data);
		}
		
	
		List <DefaultMutableTreeNode>rootChildren =view.getChildrenRoot(); 
		
		proccesChange(method);
		
	
		view.showVariables();
	}
	

 /**
	 * @param method
	 */
    private void proccesChange(MethodInfo method) {
    	
    	List<ChangeInfo> changesList = method.getChanges();
    	int i = 0;
    	while (i<changesList.size()) {
    		String change = changesList.get(i).getVariable();
    		List<String> parseChange= parseMethod(change);
    		DefaultMutableTreeNode node = foundNode(parseChange);
    		TableRowData nodeInfo = (TableRowData)node.getUserObject();
    		nodeInfo.setChanged(true); 
    		i++;
    	}
	    
    }
    
	    /**
	 * @param parseChange
	 * @return
	 */
	private DefaultMutableTreeNode foundNode(List<String> parseChange) {
		List<DefaultMutableTreeNode> children = view.getChildrenRoot();
		DefaultMutableTreeNode node = null ;
		int i = 0;
		int j = 0;
		boolean found = false;
		while (i<parseChange.size()) {
			j = 0;
			found = false;
			while (j<children.size() && !found) {
				TableRowData nodeInfo = (TableRowData)children.get(j).getUserObject();
				found = parseChange.get(i).equals(nodeInfo.getName());
				if (found) {
					node = children.get(j);
					children = view.getChildrenaAt(node);
					
				} else j++;
			}
			i++;
		}
		
		return node;
	}

	/**
     * 
     * @param s
     * @return
     */
    private List<String> parseMethod(String s){
		StringTokenizer separators = new StringTokenizer(s,"[.");
		List<String>array = new ArrayList<>();
		
		while (separators.hasMoreTokens()) {
			String token = separators.nextToken();
			array.add(token);
		}
		
		for (int i =0;i<array.size();i++) {
			String element = array.get(i);
			if (element.contains("]")) {
				array.set(i, "["+element);
			}
			
		} 
    	
		return array;
    }


}
