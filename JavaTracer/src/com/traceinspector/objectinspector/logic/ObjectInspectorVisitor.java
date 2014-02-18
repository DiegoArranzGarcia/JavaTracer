package com.traceinspector.objectinspector.logic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.javatracer.model.variables.data.ArrayData;
import com.javatracer.model.variables.data.Data;
import com.javatracer.model.variables.data.IgnoredData;
import com.javatracer.model.variables.data.InfoVisitor;
import com.javatracer.model.variables.data.NullData;
import com.javatracer.model.variables.data.ObjectData;
import com.javatracer.model.variables.data.SimpleData;
import com.javatracer.model.variables.data.StringData;
import com.traceinspector.objectinspector.view.ObjectInspectorView;

public class ObjectInspectorVisitor implements InfoVisitor{
	
	private static String LENGTH = "length";
	private static String VALUE = "value";
	private static String DOUBLE_QUOTES = "\"";
	private static String EMPTY = "";
	
	private ObjectInspectorView view;
	private int currentLevel;
	private List<DefaultMutableTreeNode> parents;
	
	public ObjectInspectorVisitor(ObjectInspectorView view){
		this.view = view;
		this.currentLevel = 0;
		this.parents = new ArrayList<>();
	}

	public void visit(ArrayData array) {
		
		DefaultMutableTreeNode node = addVariable(array.getName(),getMainInfo(array),true);
				
		//We add the node to parents because ArrayData is a recursive DataStructure
		parents.add(node);
		currentLevel++;
	
		view.addVariableToNode(LENGTH,Integer.toString(array.getLength()),false,getLastParent());
			
		List<Data> array_values = array.getValue();
		
		if (array_values!=null){
			for (int i=0;i<array_values.size();i++){
				Data data = array_values.get(i);
				data.accept(this);
			}
		} else {
			view.addVariableToNode(EMPTY,EMPTY,false,getLastParent());
		}
		
		removeLastParent();
		currentLevel--;
	
	}

	public void visit(StringData string) {
		
		DefaultMutableTreeNode node = addVariable(string.getName(),getMainInfo(string),true);
		view.addVariableToNode(VALUE,DOUBLE_QUOTES + string.getValue() + DOUBLE_QUOTES,false,node);
		
	}

	public void visit(NullData null_data) {
		
		addVariable(null_data.getName(),getMainInfo(null_data),false);
	
	}

	public void visit(ObjectData object) {
		
		DefaultMutableTreeNode node = addVariable(object.getName(),getMainInfo(object),true);
		
		//We add the node to parents because ArrayData is a recursive DataStructure
		parents.add(node);
		currentLevel++;
				
		List<Data> object_fields = object.getValue();
		
		if (object_fields!=null){
			for (int i=0;i<object_fields.size();i++){
				Data data = object_fields.get(i);
				data.accept(this);
			}
		} else {
			view.addVariableToNode(EMPTY,EMPTY,false,getLastParent());
		}
		
		removeLastParent();
		currentLevel--;
		
	}

	public void visit(SimpleData simple_data) {
	
		addVariable(simple_data.getName(),getMainInfo(simple_data),false);
		
	}

	public void visit(IgnoredData ignored_data) {
		addVariable(ignored_data.getName(),getMainInfo(ignored_data),false);
	}
	
	// Add row to the tabe
	
	private DefaultMutableTreeNode addVariable(String name,String value,boolean expandable){
		
		DefaultMutableTreeNode node;
		
		if (currentLevel == 0)
			node = view.addVariable(name,value,expandable);
		else 
			node = view.addVariableToNode(name,value,expandable,getLastParent());
		
		return node;
	}
	
	// Parents methods
	
	private void removeLastParent() {
		parents.remove(parents.size()-1);		
	}
	
	private DefaultMutableTreeNode getLastParent() {
		return parents.get(parents.size()-1);
	}
	
	
	// GetMainInfo methods
	
	public String getMainInfo(SimpleData info){
		return info.getValue().toString();
	}
	
	public String getMainInfo(ArrayData info) {
		return getSimpleClassName(info.getClassName()) + "[] (id="+ info.getId() +")";
	}

	public String getMainInfo(StringData info) {
		return "String (id=" + info.getId() + ")";
	}

	public String getMainInfo(NullData info) {
		return "null";
	}

	public String getMainInfo(ObjectData info) {
		return getSimpleClassName(info.getClassName()) + " (id=" + info.getId() + ")";
	}

	public String getMainInfo(IgnoredData ignoredClass){
		return "ignored_value";
	}
	
	public String getSimpleClassName(String completeClass){
		String result = "";
		if (completeClass.contains("."))
			result = completeClass.substring(completeClass.lastIndexOf('.')+1,completeClass.length());
		else 
			result = completeClass;
		
		return result;
	}

}
