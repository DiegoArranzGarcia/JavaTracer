package com.javatracer.viewlogic;

import java.util.ArrayList;
import java.util.List;

import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.VariableInfo;
import com.javatracer.view.ObjectInspectorView;

public class ObjectInspector {

	private int numFilledrows;
	private List<VariableInfo> variables;
	private ObjectInspectorView view;
	
	public ObjectInspector(){
		numFilledrows = 0;
		variables =  new ArrayList<>();
		view = new ObjectInspectorView();
	}
	
	public ObjectInspectorView getView(){
		return view;
	}

	public void loadVariables(List<VariableInfo> variables) {
		this.variables = variables;
		numFilledrows += variables.size();
		
		for (int i=0;i<variables.size();i++){
			VariableInfo variable = variables.get(i);
			String value = getValueFromVariable(variable.getValue());
			view.addVariable(variable.getName(),value);
		}
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
		numFilledrows =  0;
		variables = null;
		view.clear();
	}
	
}
