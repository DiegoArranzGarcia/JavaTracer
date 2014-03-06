package com.general.view.jtreetable;

import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class JTreeTable extends JTable {

	protected TreeModel treeModel;
	private DefaultTableModel tableModel;
	
	public JTreeTable(){
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void setRoot(TableRowData rootNode){
		treeModel = new TreeModel(new TableTreeNode(rootNode));

		tableModel = new DefaultTableModel(rootNode.getValues(),0) {
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		
		setModel(tableModel);
	}
	
	public void setModel(DefaultTableModel model){
		this.tableModel = model;
		super.setModel(model);
	}
	
	public void setCellRenderer(DefaultTableCellRenderer renderer){
		setDefaultRenderer(Object.class,renderer);
	}
	
	public void collapseRow(int selectedRow) {
		treeModel.collapseRow(selectedRow);
		refreshTable(selectedRow);
	}

	public void expandRow(int selectedRow) {
		treeModel.expandRow(selectedRow);
		refreshTable(selectedRow);
	}	

	public void clearTable() {
		treeModel.removeAllUnlessRoot();
		int numRows = tableModel.getRowCount();
		for (int i=0;i<numRows;i++){
			tableModel.removeRow(0);
		}	
	}

	public TableTreeNode getRoot() {
		return treeModel.getRoot();
	}
	
	public boolean isExpanded(int i) {
		return treeModel.isExpanded(i);
	}

	public void refreshTable(int selectedRow) {
		
		int numRows = tableModel.getRowCount();
		for (int i=0;i<numRows;i++){
			tableModel.removeRow(0);
		}	
		
		List<TableTreeNode> children = treeModel.getRoot().getVisibleNodes();
		children.remove(0);
		
		for (int i=0;i<children.size();i++){
			TableTreeNode node = children.get(i);
			TableRowData data = (TableRowData) node.getUserObject();
			tableModel.addRow(data.getValues());
		}
		
		if (selectedRow != -1)
			setRowSelectionInterval(selectedRow-1, selectedRow-1);
		
	}

}
