package com.inspector.objectinspector.model;

import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.ArrayData;
import com.general.model.variables.data.Data;
import com.general.model.variables.data.IgnoredData;
import com.general.model.variables.data.InfoVisitor;
import com.general.model.variables.data.NullData;
import com.general.model.variables.data.ObjectData;
import com.general.model.variables.data.SimpleData;
import com.general.model.variables.data.StringData;
import com.general.view.jtreetable.TableRowData;
import com.general.view.jtreetable.TableTreeNode;
import com.inspector.objectinspector.view.VariableRowData;
import com.tracer.model.ChangeDetector;

public class VariablesVisitor implements InfoVisitor{
	
	private static String LENGTH = ChangeDetector.LENGTH;
	private static String VALUE = "value";
	private static String DOUBLE_QUOTES = "\"";
	private static String EMPTY = "";
	
	private List<TableTreeNode> parents;
	private MainInfoVisitor mainInfoVisitor;
	
	public VariablesVisitor(TableTreeNode tableTreeNode){
		this.parents = new ArrayList<>();
		this.parents.add(tableTreeNode);
		this.mainInfoVisitor = new MainInfoVisitor();
	}

	public void visit(ArrayData array) {
		
		mainInfoVisitor.visit(array);
		TableTreeNode node = addVariable(array.getName(),mainInfoVisitor.getInfo());
				
		//We add the node to parents because ArrayData is a recursive DataStructure
		parents.add(node);
	
		addVariable(LENGTH,Integer.toString(array.getLength()));
			
		List<Data> array_values = array.getValue();
		
		if (array_values!=null){
			for (int i=0;i<array_values.size();i++){
				Data data = array_values.get(i);
				data.accept(this);
			}
		} else {
			addVariable(EMPTY,EMPTY);
		}
		
		removeLastParent();

	}

	public void visit(StringData string) {
		
		mainInfoVisitor.visit(string);
		TableTreeNode node = addVariable(string.getName(),mainInfoVisitor.getInfo());
		parents.add(node);
		addVariable(VALUE,DOUBLE_QUOTES + string.getValue() + DOUBLE_QUOTES);
		removeLastParent();
		
	}

	public void visit(NullData null_data) {
		
		mainInfoVisitor.visit(null_data);
		addVariable(null_data.getName(),mainInfoVisitor.getInfo());
	
	}

	public void visit(ObjectData object) {
		
		mainInfoVisitor.visit(object);
		TableTreeNode node = addVariable(object.getName(),mainInfoVisitor.getInfo());
		
		//We add the node to parents because ArrayData is a recursive DataStructure
		parents.add(node);
				
		List<Data> object_fields = object.getFields();
		List<Data> object_inherit = object.getInheritData();
		List<Data> object_constant = object.getConstantData();
		
		if (object_fields != null && !object_fields.isEmpty())
			addListToNode("fields",object_fields);
		if (object_inherit != null && !object_inherit.isEmpty())
			addListToNode("inherit",object_inherit);
		if (object_constant != null && !object_constant.isEmpty())
			addListToNode("constants",object_constant);
						
		removeLastParent();
		
	}

	private void addListToNode(String string, List<Data> list_data) {
		TableTreeNode node = addVariable(string,EMPTY);
		parents.add(node);

		for (int i=0;i<list_data.size();i++){
			Data data = list_data.get(i);
			data.accept(this);
		} 
			
		removeLastParent();

	}

	public void visit(SimpleData simple_data) {
		mainInfoVisitor.visit(simple_data);
		addVariable(simple_data.getName(),mainInfoVisitor.getInfo());
	}

	public void visit(IgnoredData ignored_data) {
		mainInfoVisitor.visit(ignored_data);
		addVariable(ignored_data.getName(),mainInfoVisitor.getInfo());
	}
	
	// Add row to the tabe
	
	private TableTreeNode addVariable(String name,String value){
		
		TableTreeNode node;
		TableTreeNode parent = getLastParent();
		TableRowData data = new VariableRowData(name, value);
		
		node = new TableTreeNode(data);
		parent.add(node);
					
		return node;
	}
	
	// Parents methods
	
	private void removeLastParent() {
		parents.remove(parents.size()-1);		
	}
	
	private TableTreeNode getLastParent() {
		return parents.get(parents.size()-1);
	}
	
}
