package com.inspector.objectinspector.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import com.general.resources.ImageLoader;
import com.general.view.jtreetable.TableTreeNode;
import com.general.view.jtreetable.TreeModel;

@SuppressWarnings("serial")
public class CellRenderer extends DefaultTableCellRenderer{
	
	private TreeModel treeModel;
	private ImageLoader imageLoader;
	private Border paddingBorder;
	
	public CellRenderer(TreeModel treeModel) {
		this.treeModel = treeModel;
		this.imageLoader = ImageLoader.getInstance();
		this.paddingBorder = BorderFactory.createEmptyBorder(0,6,0,0);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel d = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 

		try{
			TableTreeNode node = treeModel.getNodeFromRow(row+1);
			VariableRowData data = ((VariableRowData) node.getUserObject());
			
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
			
			if (column == 0){
				int margin = 7*(node.getDepth()-1);
				if (!expandable)
					margin += 16;
				
				d.setBorder(BorderFactory.createEmptyBorder(0,margin,0,0));
			} else if (column == 1){
				d.setBorder(paddingBorder);
			} else
				d.setBorder(null);
			
			if (column == 1){
				setToolTipText(value.toString());
			} else {
				setToolTipText(null);
			}
		} catch (Exception e){
			
		}
		
		return this;
	}
	
}
