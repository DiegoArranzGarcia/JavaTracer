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
import com.profiler.view.ProfilerRowData.TypeData;

@SuppressWarnings("serial")
public class ProfileCellRenderer extends DefaultTableCellRenderer{
		
	private Border paddingBorder;
	private TreeModel treeModel;
	private ImageLoader imageLoader;
	
	public ProfileCellRenderer(TreeModel treeModel){
		this.paddingBorder = BorderFactory.createEmptyBorder(0,4,0,0);
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
			
			if (column == 1){
				if (data.getType().equals(TypeData.CLASS)){
					setIcon(imageLoader.getClassIcon());
				} else if (data.getType().equals(TypeData.METHOD)){
					setIcon(imageLoader.getMethodIcon());
				} else if (data.getType().equals(TypeData.PACKAGE)){
					setIcon(imageLoader.getPackageIcon());
				} 
			} else if (expandable && column == 2){
				if (expanded){
					setIcon(imageLoader.getFoldedIcon());
				} else {
					setIcon(imageLoader.getExpandedIcon());
				}
			} else {
				setIcon(null);
			}
			
			if (column == 2){
				d.setBorder(BorderFactory.createEmptyBorder(0,7*(node.getDepth()-1),0,0));
			} else if (column == 3 || column == 1){
				d.setBorder(paddingBorder);
			} else {
				d.setBorder(null);
			}
			
			if (column == 4)
				d.setHorizontalAlignment(JLabel.CENTER);
			else 
				d.setHorizontalAlignment(JLabel.LEFT);
			
			if (column == 2 || column == 3)
				setToolTipText((String) value);
			else 
				setToolTipText(null);
			
		} catch (Exception e){
			
		}
	    return this;
	}
	
}
