package com.inspector.objectinspector.view;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;

import com.inspector.objectinspector.controller.ObjectInspectorController;

@SuppressWarnings("serial")
public class ObjectInspectorView extends JScrollPane implements KeyListener,MouseListener{
	
	private static JTreeTable treeTable;
	
	public ObjectInspectorView(ObjectInspectorController controller) {
   	    super(createTable(controller));   	    
   	    treeTable.addMouseListener(this);
   	    treeTable.addKeyListener(this);
	}

	private static Component createTable(ObjectInspectorController controller){
		TableRowData rootNode = new TableRowData("Name", "Value", false);
		treeTable = new JTreeTable(rootNode);
        return treeTable;
    }
	
	public TableTreeNode getRoot(){
		return treeTable.getRoot();
	}
	
	public void clear() {
		treeTable.clearTable();
	}

	public void mouseClicked(MouseEvent e) {
		
		int selectedRow = treeTable.getSelectedRow()+1;
		if (!treeTable.isExpanded(selectedRow)){
			treeTable.expandRow(selectedRow);
		}else if (treeTable.isExpanded(selectedRow)) {
			treeTable.collapseRow(selectedRow);
		}
				
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void keyPressed(KeyEvent e) {
		int selectedRow = treeTable.getSelectedRow()+1;
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			
			if (!treeTable.isExpanded(selectedRow)){
				treeTable.expandRow(selectedRow);
			}
			
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT){
			
			if (treeTable.isExpanded(selectedRow)){
				treeTable.collapseRow(selectedRow);
			}
			
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void refreshTable() {
		treeTable.refreshTable(-1);
	}

}
