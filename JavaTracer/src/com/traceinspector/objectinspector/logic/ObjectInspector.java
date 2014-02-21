package com.traceinspector.objectinspector.logic;

import java.util.ArrayList;
import java.util.List;
import com.general.model.data.MethodInfo;
import com.general.model.variables.data.Data;
import com.general.model.variables.data.InfoVisitor;
import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.objectinspector.view.ObjectInspectorView;

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
		
		view.showVariables();
	}

}
