package com.traceinspector.treeinspectorview;

import java.util.List;

import com.javatracer.model.data.ArrayInfo;
import com.javatracer.model.data.NullObject;
import com.javatracer.model.data.ObjectInfo;
import com.javatracer.model.data.VariableInfo;


public class TextInBoxExt {

	private long id;
	private String methodName;
	private List<VariableInfo> arguments;
	private VariableInfo returnValue;
	private VariableInfo thisValue;
	private boolean expanded;
	private boolean selected;
	
	public TextInBoxExt(){
		this.expanded = true;
		this.selected = false;
	}

	public TextInBoxExt(long id, String name, List<VariableInfo> arguments,	VariableInfo returnValue,VariableInfo thisValue) {
		this.id = id;
		this.methodName = name;
		this.arguments = arguments;
		this.returnValue = returnValue;
		this.thisValue = thisValue;
		this.expanded = false;
		this.selected = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<VariableInfo> getArguments() {
		return arguments;
	}

	public void setArguments(List<VariableInfo> arguments) {
		this.arguments = arguments;
	}

	public VariableInfo getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(VariableInfo returnValue) {
		this.returnValue = returnValue;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String giveMeTextInBox() {
		
		 List<VariableInfo> var = getArguments();
		 int i=0;
		 
		 String name = getMethodName() + " (";
		 
		 while(i<var.size()){
			 
			name= name + completeArgumentString(var.get(i));
	    	i++;
		 }		 
		 
		 if(i==0)
			 name = name.substring(0, name.length()) +" )";
		 else 
			 name = name.substring(0, name.length()-2) +" )";
		 
		 return name;
	} 

	public String completeArgumentString(VariableInfo var) {
		
		String name=var.getName();
		Object value = var.getValue();
		
		if (value instanceof ArrayInfo)
			name=((ArrayInfo)value).getClassName() +"[ ]"+" "+ name + ", " ;
		else if (value instanceof ObjectInfo)
			name=((ObjectInfo)value).getClassName() +" "+ name + ", " ;
		else if (value instanceof NullObject)
			name= name + " = null, " ;
		else if (value instanceof String)
			name= name + " = \"" + value.toString() + "\", " ;
		else
			name= name + " = " + value.toString() + ", " ;		
		
		return name;
	}

	public VariableInfo getThisValue() {
		return thisValue;
	}

	public void setThisValue(VariableInfo thisValue) {
		this.thisValue = thisValue;
	}
	
	

}
