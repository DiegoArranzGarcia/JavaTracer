package com.inspector.objectinspector.model;

import java.util.ArrayList;
import java.util.List;

import com.general.model.variables.data.ArrayData;
import com.general.model.variables.data.IgnoredData;
import com.general.model.variables.data.InfoVisitor;
import com.general.model.variables.data.NullData;
import com.general.model.variables.data.ObjectData;
import com.general.model.variables.data.SimpleData;
import com.general.model.variables.data.StringData;
import com.inspector.objectinspector.view.TableRowData;
import com.inspector.objectinspector.view.TableTreeNode;

public class ChangeInfoVisitor implements InfoVisitor {
	
	private static String IN = "in";
	private static String OUT = "out";
	
	private TableTreeNode rootNode;

	public void visit(ArrayData arrayData) {
		
		MainInfoVisitor mainInfoVisitor = new MainInfoVisitor();
		mainInfoVisitor.visit(arrayData);
		
		String rootValue = ((TableRowData)rootNode.getUserObject()).getValue();		
		((TableRowData)rootNode.getUserObject()).setValue("");

		//Configure in node
		TableTreeNode in = new TableTreeNode(new TableRowData(IN,rootValue,true));
		List<TableTreeNode> children = getChildrenOf(rootNode);
		rootNode.removeAllChildren();
		rootNode.add(in);
		for (int i=0;i<children.size();i++){
			in.add(children.get(i));
		}
		
		//Configure out node
		VariablesVisitor variableVisitor = new VariablesVisitor(rootNode);
		variableVisitor.visit(arrayData);
		((TableRowData)((TableTreeNode) rootNode.getChildAt(1)).getUserObject()).setName(OUT);
		
	}

	public void visit(StringData info) {
		
	}

	public void visit(NullData nullData) {
		MainInfoVisitor mainInfoVisitor = new MainInfoVisitor();
		mainInfoVisitor.visit(nullData);
		
		String rootValue = ((TableRowData)rootNode.getUserObject()).getValue();		
		((TableRowData)rootNode.getUserObject()).setValue("");

		//Configure in node
		TableTreeNode in = new TableTreeNode(new TableRowData(IN,rootValue,true));
		List<TableTreeNode> children = getChildrenOf(rootNode);
		rootNode.removeAllChildren();
		rootNode.add(in);
		for (int i=0;i<children.size();i++){
			in.add(children.get(i));
		}
		
		//Configure out node
		VariablesVisitor variableVisitor = new VariablesVisitor(rootNode);
		variableVisitor.visit(nullData);
		((TableRowData)((TableTreeNode) rootNode.getChildAt(1)).getUserObject()).setName(OUT);
	}
	
	public void visit(IgnoredData ignoredClass) {
		//Not used
	}

	public void visit(ObjectData objectInfo) {
		MainInfoVisitor mainInfoVisitor = new MainInfoVisitor();
		mainInfoVisitor.visit(objectInfo);
		
		String rootValue = ((TableRowData)rootNode.getUserObject()).getValue();		
		((TableRowData)rootNode.getUserObject()).setValue("");

		//Configure in node
		TableTreeNode in = new TableTreeNode(new TableRowData(IN,rootValue,true));
		List<TableTreeNode> children = getChildrenOf(rootNode);
		rootNode.removeAllChildren();
		rootNode.add(in);
		for (int i=0;i<children.size();i++){
			in.add(children.get(i));
		}
		
		//Configure out node
		VariablesVisitor variableVisitor = new VariablesVisitor(rootNode);
		variableVisitor.visit(objectInfo);
		((TableRowData)((TableTreeNode) rootNode.getChildAt(1)).getUserObject()).setName(OUT);
	}

	public void visit(SimpleData simpleData) {
		TableRowData rowData = (TableRowData) rootNode.getUserObject();
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
