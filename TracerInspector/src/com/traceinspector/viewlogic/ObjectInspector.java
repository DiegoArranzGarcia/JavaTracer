package com.traceinspector.viewlogic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.javatracer.model.variables.data.NullData;
import com.javatracer.model.variables.data.ObjectData;
import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.model.TreeManager;
import com.traceinspector.objectinspectorview.ObjectInspectorView;
import com.traceinspector.treeinspectorview.TextInBoxExt;

public class ObjectInspector {

	private static String LENGTH = "length";
	private static String VALUE = "value";
	private static String DOUBLE_QUOTES = "\"";
	
	private TraceInspectorController controller;
	private TreeManager treeManager;
	
	private List<Object> variables;
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
		
		variables = box.getArguments();
		
		for (int i=0;i<variables.size();i++){
			//VariableInfo variable = variables.get(i);
			//addVariable(variable,null);
		}
		
		view.showVariables();
	}

	/*private void addVariable(VariableInfo variable,DefaultMutableTreeNode parent) {
		
		MainInfoVisitor infoVisitor = new MainInfoVisitor();
		ExpandableVisitor expandableVisitor = new ExpandableVisitor();
		
		Object value = variable.getValue();
		String name = variable.getName();
		
		if (value instanceof ArrayInfo){
			
			ArrayInfo array = ((ArrayInfo)value);
			List<Object> values = array.getValues();
			
			infoVisitor.visit(array);
			DefaultMutableTreeNode node = view.addVariable(name,infoVisitor.getMainInfo(),true);
			
			view.addVariableToNode(LENGTH,Integer.toString(array.getLength()),false,node);
			
			for (int i=0;i<values.size();i++){
				infoVisitor.visit(values.get(i));
				expandableVisitor.visit(values.get(i));
				view.addVariableToNode(name + "[" + i + "]",infoVisitor.getMainInfo(),expandableVisitor.isExpandable(),node);
			}
			
		} else if (value instanceof StringInfo) {
			
			StringInfo info = (StringInfo) value;
			infoVisitor.visit(info);
				
			DefaultMutableTreeNode node = view.addVariable(name,infoVisitor.getMainInfo(),true);
			view.addVariableToNode(VALUE,DOUBLE_QUOTES+info.getValue()+DOUBLE_QUOTES,true,node);		
			
		} else if (value instanceof ObjectData) {
			
			ObjectData object = ((ObjectData)value);
			List<VariableInfo> values = object.getFields();
			
			infoVisitor.visit(object);
			DefaultMutableTreeNode node = view.addVariable(name,infoVisitor.getMainInfo(),true);
			
			for (int i=0;i<values.size();i++){
				VariableInfo field = values.get(i);
				infoVisitor.visit(field);
				expandableVisitor.visit(field);
				view.addVariableToNode(field.getName(),infoVisitor.getMainInfo(),expandableVisitor.isExpandable(),node);
			}
			
		} else if (value instanceof NullData) {
			
			NullData info = (NullData) value;
			infoVisitor.visit(info);
				
			view.addVariable(name,infoVisitor.getMainInfo(),false);
			
		} else {
			
			infoVisitor.visit(value);
			view.addVariable(name,infoVisitor.getMainInfo(),false);
			
		}
		
	}*/
		
	public void clear() {
		view.clear();
	}

}
