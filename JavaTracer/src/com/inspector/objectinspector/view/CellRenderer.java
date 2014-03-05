package com.inspector.objectinspector.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.general.imageresources.ImageLoader;

@SuppressWarnings("serial")
public class CellRenderer extends DefaultTableCellRenderer{
	
	private TreeModel treeModel;
	private ImageLoader imageLoader;
	
	public CellRenderer(TreeModel treeModel) {
		this.treeModel = treeModel;
		this.imageLoader = ImageLoader.getInstance();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 

		try{
			TableTreeNode node = treeModel.getNodeFromRow(row+1);
			TableRowData data = ((TableRowData) node.getUserObject());
		
			boolean changed = data.isChanged();
			boolean expandable = node.isExpandable();
			boolean expanded = node.isExpanded();
		
			if (!isSelected){
			    if (changed) {
			        setBackground(Color.RED);
			    } else {
			    	setBackground(Color.WHITE);
			    }
			}
		
		
			if (expandable && column == 0){
				if (expanded){
					setIcon(imageLoader.getFoldedIcon());
				} else {
					setIcon(imageLoader.getExpandedIcon());
				}
			}
			else {
				setIcon(null);
			}
		} catch (Exception e){
			
		}
		
		return this;
	}
	
}
