package com.inspector.objectinspector.model;

import com.general.model.variables.data.ArrayData;
import com.general.model.variables.data.IgnoredData;
import com.general.model.variables.data.InfoVisitor;
import com.general.model.variables.data.NullData;
import com.general.model.variables.data.ObjectData;
import com.general.model.variables.data.SimpleData;
import com.general.model.variables.data.StringData;

public class MainInfoVisitor implements InfoVisitor{
	
	private String info;
	
	public MainInfoVisitor(){
		info = "";
	}
	
	public void visit(SimpleData simpleData){
		info = simpleData.getValue().toString();
	}
	
	public void visit(ArrayData arrayData) {
		info = getSimpleClassName(arrayData.getClassName()) + "[] (id="+ arrayData.getId() +")";
	}

	public void visit(StringData stringData) {
		info = "\"" + stringData.getValue() + "\"";
	}

	public void visit(NullData nullData) {
		info = "null";
	}

	public void visit(ObjectData ObjectData) {
		info = getSimpleClassName(ObjectData.getClassName()) + " (id=" + ObjectData.getId() + ")";
	}

	public void visit(IgnoredData ignoredClass){
		info = ignoredClass.getClassName();
	}
	
	public String getSimpleClassName(String completeClass){
		String result = "";
		if (completeClass.contains("."))
			result = completeClass.substring(completeClass.lastIndexOf('.')+1,completeClass.length());
		else 
			result = completeClass;
		
		return result;
	}

	public String getInfo() {
		return info;
	}
	
}
