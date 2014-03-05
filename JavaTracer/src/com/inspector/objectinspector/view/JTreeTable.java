package com.inspector.objectinspector.view;

import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class JTreeTable extends JTable {

	private TreeModel treeModel;
	private DefaultTableModel tableModel;
	
	public JTreeTable(){
		TableRowData rootNode = new TableRowData("Name", "Value", false);
		treeModel = new TreeModel(new TableTreeNode(rootNode));

		// Cell's are not editable
		tableModel = new DefaultTableModel(new String[]{rootNode.getName(),rootNode.getValue()},0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		setModel(tableModel);
		
		CellRenderer renderer = new CellRenderer(treeModel);
		setDefaultRenderer(Object.class,renderer);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void collapseRow(int selectedRow) {
		treeModel.collapseRow(selectedRow);
		refreshTable(selectedRow);
		/*List<TableTreeNode> children = treeModel.getNodeFromRow(selectedRow).getChildren();
		for (int i=0;i<children.size();i++){
			tableModel.removeRow(selectedRow);
		}*/
	}

	public void expandRow(int selectedRow) {
			
		treeModel.expandRow(selectedRow);
		refreshTable(selectedRow);
		/*List<TableTreeNode> children = treeModel.getNodeFromRow(selectedRow).getChildren();
		
		for (int i=0;i<children.size();i++){
			TableTreeNode node = children.get(i);
			TableRowData data = (TableRowData) node.getUserObject();
			String tab = getTab(node.getDepth());
			tableModel.insertRow(selectedRow+i,new String[]{tab + data.getName(),data.getValue()});
		}*/
	}	

	private String getTab(long depth) {
		String tab = "";
		for (int i=1;i<depth;i++)
			tab += "  ";
		return tab;
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
			TableRowData data = (TableRowData) children.get(i).getUserObject();
			tableModel.addRow(new String[]{data.getName(),data.getValue()});
		}
		
		if (selectedRow != -1)
			setRowSelectionInterval(selectedRow-1, selectedRow-1);
		
	}

}
