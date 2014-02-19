package com.traceinspector.objectinspector.logic;

import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.Data;
import com.general.model.variables.data.InfoVisitor;
import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.objectinspector.view.ObjectInspectorView;
import com.traceinspector.treeinspector.data.MethodBox;

public class ObjectInspector {
	
	private TraceInspectorController controller;
	private TreeManager treeManager;
	
	private List<Data> variables;
	private ObjectInspectorView view;
	
	public ObjectInspector(TraceInspectorController controller, TreeManager treeManager){
		
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

	public void addNodeInfo(Data thisValue, Data returnValue,List<Data> variables) {
		
		this.variables = variables;
		InfoVisitor visitor = new ObjectInspectorVisitor(view);
		
		thisValue.accept(visitor);
		if (returnValue != null)
			returnValue.accept(visitor);
		
		for (int i=0;i<variables.size();i++)
			variables.get(i).accept(visitor);
		
		view.showVariables();
	}

}
