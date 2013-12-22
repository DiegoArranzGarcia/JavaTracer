package com.traceinspector.viewlogic;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.VariableInfo;
import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.treeinspectorview.TextInBoxExt;
import com.traceinspector.view.ObjectInspectorView;

public class ObjectInspector {

	private TraceInspectorController controller;
	private TreeManager treeManager;
	
	private List<VariableInfo> variables;
	private ObjectInspectorView view;
	
	public ObjectInspector(TraceInspectorController controller, TreeManager treeManager){
		
		this.controller = controller;
		this.treeManager = treeManager;
		
		this.variables =  new ArrayList<>();
		this.view = new ObjectInspectorView();
	}
	
	public ObjectInspectorView getView(){
		return view;
	}

	public void addVariables(TextInBoxExt box) {
		
		variables = box.getArguments();
		
		for (int i=0;i<variables.size();i++){
			VariableInfo variable = variables.get(i);
			String value = getValueFromVariable(variable.getValue());
			view.addVariable(variable.getName(),value);
		}
		
		view.showVariables();
	}

	private String getValueFromVariable(Object value) {
		
		String stringValue = "";
		if (value instanceof ArrayInfo){
			ArrayInfo array = ((ArrayInfo)value);
			List<Object> values = array.getValues();
			stringValue = "[";
			for (int i=0;i<values.size();i++){
				if (i != 0){
					stringValue += ",";
				} 
				stringValue += values.get(i).toString();
			}
			
			stringValue += "]";
		} else if (value instanceof ObjectInfo){
			stringValue = "(id=" + ((ObjectInfo)value).getId() + ")";
		} else if (value instanceof NullObject){
			stringValue = "null";
		} else {
			stringValue = value.toString();
		}
		
		return stringValue;
	}

	public void clear() {
		view.clear();
	}
	
}
