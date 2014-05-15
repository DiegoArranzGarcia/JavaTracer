
package com.inspector.objectinspector.presenter;
import java.util.*;

import com.general.model.data.ChangeInfo;
import com.general.model.data.MethodInfo;
import com.general.model.data.variables.Data;
import com.general.model.data.variables.InfoVisitor;
import com.general.view.jtreetable.TableTreeNode;
import com.inspector.model.TreeManager;
import com.inspector.objectinspector.model.ChangeInfoVisitor;
import com.inspector.objectinspector.model.VariablesVisitor;
import com.inspector.objectinspector.view.*;
import com.inspector.presenter.InspectorPresenter;

public class ObjectInspectorPresenter implements ObjectInspectorPresenterInterface {
	
	private InspectorPresenter presenter;
	private TreeManager treeManager;
	
	private List<Data> variables;
	private ObjectInspectorViewInterface view;
	
	public ObjectInspectorPresenter(TreeManager treeManager){
		this.treeManager = treeManager;
		this.variables =  new ArrayList<>();
	}
	
	public ObjectInspectorViewInterface getView(){
		if (view == null) {
			view = new ObjectInspectorView();
			view.setController(this);
		}
		return view;
	}
	
	public void clear() {
		view.clearTable();
	}

	public void addNodeInfo(MethodInfo method) {
		
		InfoVisitor visitor = new VariablesVisitor(view.getRoot());
		List<Data> arguments = method.getArguments();
		Data thisValue = method.getThis_data();
		Data returnValue = method.getReturn_data();		
		Data calledValue = method.getCalledFromClass();
		
		variables.add(calledValue);
		calledValue.accept(visitor);
		
		variables.add(thisValue);
		if (thisValue != null)
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
		 
		proccesChange(method);
		view.refreshTable();
		
	}
	

	/**
	 * @param method
	 */
	
    private void proccesChange(MethodInfo method) {
    	
    	List<ChangeInfo> changesList = method.getChanges();
    	
    	int i = 0;
    	while (i<changesList.size()) {
    		ChangeInfo changeInfo = changesList.get(i);
    		String change = changeInfo.getVariable();
    		List<String> parseChange= parseMethod(change);
    		TableTreeNode node = foundNode(parseChange);
    		VariableRowData nodeInfo = (VariableRowData)node.getUserObject();
    		nodeInfo.setChanged(true); 
    		modifyTable(node,changeInfo.getValue());
    		i++;
    	}
	    
    }
    
    private void modifyTable(TableTreeNode node, Data value) {
    	ChangeInfoVisitor changeVisitor = new ChangeInfoVisitor();
    	changeVisitor.setRootNode(node);
    	value.accept(changeVisitor);
    }

	/**
	 * @param parseChange
	 * @return
	 */
    
	private TableTreeNode foundNode(List<String> parseChange) {
		List<TableTreeNode> children = view.getRoot().getChildren();
		TableTreeNode node = null ;
		int i = 0;
		int j = 0;
		boolean found = false;
		while (i<parseChange.size()) {
			j = 0;
			found = false;
			while (j<children.size() && !found) {
				VariableRowData nodeInfo = (VariableRowData)children.get(j).getUserObject();
				found = parseChange.get(i).equals(nodeInfo.getName());
				if (found) {
					node = children.get(j);
					children = node.getChildren();
				} else 
					j++;
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

	public void setController(InspectorPresenter inspectorController) {
		this.presenter = inspectorController;
	}

	public void showTable() {
		if (view == null) {
			view = new ObjectInspectorView();
			view.setController(this);
		}
	}

	public InspectorPresenter getPresenter() {
	    return presenter;
    }

	public void setPresenter(InspectorPresenter p_presenter) {
	    presenter = p_presenter;
    }

	public TreeManager getTreeManager() {
	    return treeManager;
    }

	public void setTreeManager(TreeManager p_treeManager) {
	    treeManager = p_treeManager;
    }

}
