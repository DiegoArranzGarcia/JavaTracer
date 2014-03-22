package com.profiler.view;

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
public class ProfileCellRenderer extends DefaultTableCellRenderer{
		
	private Border paddingBorder;
	private TreeModel treeModel;
	private ImageLoader imageLoader;
	
	public ProfileCellRenderer(TreeModel treeModel){
		this.paddingBorder = BorderFactory.createEmptyBorder(0,6,0,0);
		this.treeModel = treeModel;
		this.imageLoader = ImageLoader.getInstance();
	}
		
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		JLabel d = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
		try {
				
			TableTreeNode node = treeModel.getNodeFromRow(row+1);
			ProfilerRowData data = (ProfilerRowData) node.getUserObject();

			boolean expandable = node.isExpandable();
			boolean expanded = node.isExpanded();
			
			if (column == 0)
				d.setBackground(data.getColor());	
			else if (!isSelected)
				d.setBackground(Color.WHITE);
						
			if (expandable && column == 1){
				if (expanded){
					setIcon(imageLoader.getFoldedIcon());
				} else {
					setIcon(imageLoader.getExpandedIcon());
				}
			}
			else {
				setIcon(null);
			}
			
			if (column == 1){
				d.setBorder(BorderFactory.createEmptyBorder(0,7*(node.getDepth()-1),0,0));
			} else if (column == 2){
				d.setBorder(paddingBorder);
			} else
				d.setBorder(null);
			
			if (column == 3)
				d.setHorizontalAlignment(JLabel.CENTER);
			else 
				d.setHorizontalAlignment(JLabel.LEFT);
			
			if (column == 1 || column == 2)
				setToolTipText((String) value);
			
		} catch (Exception e){
			
		}
	    return this;
	}
	
}
