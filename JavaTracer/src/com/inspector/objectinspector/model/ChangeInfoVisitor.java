package com.inspector.objectinspector.model;

import java.util.ArrayList;
import java.util.List;

import com.general.model.data.variables.ArrayData;
import com.general.model.data.variables.IgnoredData;
import com.general.model.data.variables.InfoVisitor;
import com.general.model.data.variables.NullData;
import com.general.model.data.variables.ObjectData;
import com.general.model.data.variables.SimpleData;
import com.general.model.data.variables.StringData;
import com.general.view.jtreetable.TableTreeNode;
import com.inspector.objectinspector.view.VariableRowData;

public class ChangeInfoVisitor implements InfoVisitor {
	
	private static String IN = "in";
	private static String OUT = "out";
	
	private TableTreeNode rootNode;

	public void visit(ArrayData arrayData) {
		
		MainInfoVisitor mainInfoVisitor = new MainInfoVisitor();
		mainInfoVisitor.visit(arrayData);
		
		String rootValue = ((VariableRowData)rootNode.getUserObject()).getValue();		
		((VariableRowData)rootNode.getUserObject()).setValue("");

		//Configure in node
		TableTreeNode in = new TableTreeNode(new VariableRowData(IN,rootValue));
		List<TableTreeNode> children = getChildrenOf(rootNode);
		rootNode.removeAllChildren();
		rootNode.add(in);
		for (int i=0;i<children.size();i++){
			in.add(children.get(i));
		}
		
		//Configure out node
		VariablesVisitor variableVisitor = new VariablesVisitor(rootNode);
		variableVisitor.visit(arrayData);
		((VariableRowData)((TableTreeNode) rootNode.getChildAt(1)).getUserObject()).setName(OUT);
		
		
	}

	public void visit(StringData stringData) {
		VariableRowData rowData = (VariableRowData) rootNode.getUserObject();
		String oldValue = rowData.getValue();
		String newValue = oldValue + " -> " + "\"" + stringData.getValue() + "\"";
		rowData.setValue(newValue);
		
	}

	public void visit(NullData nullData) {
		MainInfoVisitor mainInfoVisitor = new MainInfoVisitor();
		mainInfoVisitor.visit(nullData);
		
		String rootValue = ((VariableRowData)rootNode.getUserObject()).getValue();		
		((VariableRowData)rootNode.getUserObject()).setValue("");

		//Configure in node
		TableTreeNode in = new TableTreeNode(new VariableRowData(IN,rootValue));
		List<TableTreeNode> children = getChildrenOf(rootNode);
		rootNode.removeAllChildren();
		rootNode.add(in);
		for (int i=0;i<children.size();i++){
			in.add(children.get(i));
		}
		
		//Configure out node
		VariablesVisitor variableVisitor = new VariablesVisitor(rootNode);
		variableVisitor.visit(nullData);
		((VariableRowData)((TableTreeNode) rootNode.getChildAt(1)).getUserObject()).setName(OUT);
		
	}
	
	public void visit(IgnoredData ignoredClass) {
		//Not used
	}

	public void visit(ObjectData objectInfo) {
		MainInfoVisitor mainInfoVisitor = new MainInfoVisitor();
		mainInfoVisitor.visit(objectInfo);
		
		String rootValue = ((VariableRowData)rootNode.getUserObject()).getValue();		
		((VariableRowData)rootNode.getUserObject()).setValue("");

		//Configure in node
		TableTreeNode in = new TableTreeNode(new VariableRowData(IN,rootValue));
		List<TableTreeNode> children = getChildrenOf(rootNode);
		rootNode.removeAllChildren();
		rootNode.add(in);
		for (int i=0;i<children.size();i++){
			in.add(children.get(i));
		}
		
		//Configure out node
		VariablesVisitor variableVisitor = new VariablesVisitor(rootNode);
		variableVisitor.visit(objectInfo);
		((VariableRowData)((TableTreeNode) rootNode.getChildAt(1)).getUserObject()).setName(OUT);
		
	}

	public void visit(SimpleData simpleData) {
		VariableRowData rowData = (VariableRowData) rootNode.getUserObject();
		String oldValue = rowData.getValue();
		String newValue = oldValue + " -> " + simpleData.getValue();
		rowData.setValue(newValue);
	}
	
	public void setRootNode(TableTreeNode node) {
		this.rootNode = node;
	}
	
	private List<TableTreeNode> getChildrenOf(TableTreeNode node){
		List<TableTreeNode> children = new ArrayList<>();
		for (int i=0;i<node.getChildCount();i++){
			children.add((TableTreeNode) node.getChildAt(i));
		}
		return children;
	}

}
