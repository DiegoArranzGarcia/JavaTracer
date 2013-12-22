package com.traceinspector.view;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ObjectInspectorView extends JScrollPane{
	
	static DefaultTableModel model;
	
	public ObjectInspectorView() {
   	    super(createTable());   
	}

	private static Component createTable() {
		
		String[] columnNames = {"Variable","Value"};
		model = new DefaultTableModel(columnNames,0);

		JTable table = new JTable(model);
		return table;
	}

	public void addVariable(String name, String value) {
		Object[] variable = {name,value};
		model.addRow(variable);
	}

	public void clear() {
		model.setRowCount(0);		
	}



}
