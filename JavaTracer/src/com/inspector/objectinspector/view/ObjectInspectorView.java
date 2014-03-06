package com.inspector.objectinspector.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.general.view.jtreetable.JTreeTable;
import com.inspector.objectinspector.controller.ObjectInspectorController;

@SuppressWarnings("serial")
public class ObjectInspectorView extends JTreeTable implements KeyListener,MouseListener{
	
	private ObjectInspectorController controller;
	
	public ObjectInspectorView() {	    
		addMouseListener(this);
   	    addKeyListener(this);
		VariableRowData rootNode = new VariableRowData("Name", "Value");
		setRoot(rootNode);
		setCellRenderer(new CellRenderer(treeModel));
	}
	
	public void mouseClicked(MouseEvent e) {
		
		int selectedRow = getSelectedRow()+1;
		
		if (!isExpanded(selectedRow))
			expandRow(selectedRow);
		else if (isExpanded(selectedRow))
			collapseRow(selectedRow);
				
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void keyPressed(KeyEvent e) {
		int selectedRow = getSelectedRow()+1;
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			
			if (!isExpanded(selectedRow)){
				expandRow(selectedRow);
			}
			
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT){
			
			if (isExpanded(selectedRow)){
				collapseRow(selectedRow);
			}
			
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void refreshTable() {
		refreshTable(-1);
	}

	public void setController(ObjectInspectorController objectInspectorController) {
		this.controller = objectInspectorController;
	}

}
