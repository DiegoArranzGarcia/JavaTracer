package com.traceinspector.viewlogic;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.variables.data.Data;
import com.javatracer.model.variables.data.InfoVisitor;
import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.objectinspectorview.ObjectInspectorView;
import com.traceinspector.treeinspectorview.TextInBoxExt;

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

	public void addVariables(TextInBoxExt box) {
		
		InfoVisitor visitor = new ObjectInspectorVisitor(view);
		variables = box.getArguments();
		
		for (int i=0;i<variables.size();i++)
			variables.get(i).accept(visitor);
		
		view.showVariables();
	}
	
	public void clear() {
		view.clear();
	}

}
