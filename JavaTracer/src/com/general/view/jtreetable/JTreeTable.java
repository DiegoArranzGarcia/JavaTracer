package com.general.view.jtreetable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class JTreeTable extends JTable implements KeyListener,MouseListener{

	protected TreeModel treeModel;
	private int expandbleColumn;
	private DefaultTableModel tableModel;

	public JTreeTable(){
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setExpandbleColumn(0);
	}

	public void setRoot(TableRowData rootNode){
		treeModel = new TreeModel(new TableTreeNode(rootNode));

		tableModel = new DefaultTableModel(rootNode.getValues(),0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		setModel(tableModel);

		addMouseListener(this);
		addKeyListener(this);
	}

	public void setModel(DefaultTableModel model){
		this.tableModel = model;
		super.setModel(model);
	}

	public void setCellRenderer(DefaultTableCellRenderer renderer){
		setDefaultRenderer(Object.class,renderer);
	}

	public void collapseRow(int selectedRow) {
			
		if (selectedRow != -1){				
			TableTreeNode node = treeModel.getNodeFromRow(selectedRow+1);
			int children = node.getVisibleNodes().size() -1;
			
			for (int i=0;i<children;i++){
				tableModel.removeRow(selectedRow+1);
			}
			
			treeModel.collapseRow(selectedRow+1);
			node.setExpanded(false);
		}
	}

	public void expandRow(int selectedRow) {
		
		if (selectedRow != -1){
			
			treeModel.expandRow(selectedRow+1);
			TableTreeNode node = treeModel.getNodeFromRow(selectedRow+1);
			
			for (int i=0;i<node.getChildCount();i++){
				tableModel.insertRow(selectedRow+i+1,node.getChildAt(i).getUserObject().getValues());
			}
		
			node.setExpanded(true);
		}
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
		return treeModel.isExpanded(i+1);
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

	public TreeModel getTreeModel() {
		return treeModel;
	}

	public void keyPressed(KeyEvent e) {

		if (getSelectedRow()!=-1){
			int selectedRow = getSelectedRow();

			if (e.getKeyCode() == KeyEvent.VK_RIGHT && !isExpanded(selectedRow)){
				expandRow(selectedRow);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT && isExpanded(selectedRow)){
				collapseRow(selectedRow);
			}
		}

	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {

		int selectedRow = getSelectedRow();
		int selectedColumn = getSelectedColumn();

		if (selectedColumn == expandbleColumn){
			if (!isExpanded(selectedRow))
				expandRow(selectedRow);
			else if (isExpanded(selectedRow))
				collapseRow(selectedRow);
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public int getExpandbleColumn() {
		return expandbleColumn;
	}

	public void setExpandbleColumn(int expandbleColumn) {
		this.expandbleColumn = expandbleColumn;
	}

}
