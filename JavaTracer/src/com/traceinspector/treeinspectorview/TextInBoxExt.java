package com.traceinspector.treeinspectorview;

import java.util.List;

import com.javatracer.model.variables.data.ArrayData;
import com.javatracer.model.variables.data.Data;
import com.javatracer.model.variables.data.NullData;
import com.javatracer.model.variables.data.ObjectData;
import com.javatracer.model.variables.data.SimpleData;
import com.javatracer.model.variables.data.StringData;


public class TextInBoxExt {

	private long id;
	private String methodName;
	private List<Data> arguments;
	private Data returnValue;
	private Data thisValue;
	private boolean expanded;
	private boolean selected;
	private boolean haveChildren;
	
	public TextInBoxExt(){
		this.expanded = true;
		this.selected = false;
	}

	public TextInBoxExt(long id, String name, List<Data> arguments,	Data returnValue,Data thisValue,boolean haveChildren) {
		this.id = id;
		this.methodName = name;
		this.arguments = arguments;
		this.returnValue = returnValue;
		this.thisValue = thisValue;
		this.expanded = false;
		this.selected = false;
		this.setHaveChildren(haveChildren);
	}
	
	public String giveMeTextInBox() {
		
		 int i=0;
		 
		 String name = getMethodName() + " (";
		 
		 while(i<arguments.size()){
			 
			name= name + completeArgumentString(arguments.get(i));
	    	i++;
		 }		 
		 
		 if(i==0)
			 name = name.substring(0, name.length()) +" )";
		 else 
			 name = name.substring(0, name.length()-2) +" )";
		 
		 
		 if(returnValue != null)
			name += " return " + returnString(returnValue);
		 
		 return name;
	} 

	private String returnString(Data var) {
		String text = "";
		
		if (var instanceof ArrayData){
			ArrayData array = ((ArrayData)var);
			text= array.getClassName() +"[ ]";
		}else if (var instanceof ObjectData){
			ObjectData object = ((ObjectData)var);
			text= object.getClassName();
		}else if (var instanceof NullData){
			text= "null" ;
		}else if (var instanceof StringData){
			StringData string = ((StringData)var);
			text= "\"" + string.getValue() + "\"" ;
		}else if (var instanceof SimpleData){
			SimpleData data = ((SimpleData)var);
			text= data.getValue().toString();		
		}
		
		return text;
	}

	public String completeArgumentString(Object var) {

		String text = "";
		
		if (var instanceof ArrayData){
			ArrayData array = ((ArrayData)var);
			text= array.getClassName() +"[ ]"+" "+ array.getName() + ", " ;
		}else if (var instanceof ObjectData){
			ObjectData object = ((ObjectData)var);
			text= object.getClassName() +" "+ object.getName() + ", " ;
		}else if (var instanceof NullData){
			text= ((NullData) var).getName() + " = null, " ;
		}else if (var instanceof StringData){
			StringData string = ((StringData)var);
			text= string.getName() + " = \"" + string.getValue() + "\", " ;
		}else if (var instanceof SimpleData){
			SimpleData data = ((SimpleData)var);
			text= data.getName() + " = " + data.getValue().toString() + ", " ;		
		}
		
		return text;
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

	public List<Data> getArguments() {
		return arguments;
	}

	public void setArguments(List<Data> arguments) {
		this.arguments = arguments;
	}

	public Data getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Data returnValue) {
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
	
	public Data getThisValue() {
		return thisValue;
	}

	public void setThisValue(Data thisValue) {
		this.thisValue = thisValue;
	}

	public boolean haveChildren() {
		return haveChildren;
	}

	public void setHaveChildren(boolean haveChildren) {
		this.haveChildren = haveChildren;
	}
	
	

}
