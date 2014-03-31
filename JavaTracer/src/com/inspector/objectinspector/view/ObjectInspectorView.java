package com.inspector.objectinspector.view;

import java.awt.Color;

import com.general.view.jtreetable.JTreeTable;
import com.inspector.objectinspector.controller.ObjectInspectorController;

@SuppressWarnings("serial")
public class ObjectInspectorView extends JTreeTable {
	
	private ObjectInspectorController controller;
	
	public ObjectInspectorView(){
		VariableRowData rootNode = new VariableRowData("Name", "Value");
		setRoot(rootNode);
		setCellRenderer(new CellRenderer(treeModel));
		setGridColor(Color.BLACK);
	}

	public void refreshTable() {
		refreshTable(-1);
	}

	public void setController(ObjectInspectorController objectInspectorController) {
		this.controller = objectInspectorController;
	}

}
