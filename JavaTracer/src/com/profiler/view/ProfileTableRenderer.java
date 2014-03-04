package com.profiler.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class ProfileTableRenderer extends DefaultTableCellRenderer{
		
	private List<Color> colors;
	
	public ProfileTableRenderer(){
		this.colors = new ArrayList<>();
	}
	
	public void setColors(List<Color> colors){
		this.colors = colors;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel d = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
	    // You'll need some way to supply the filter value, may via a centralised 
	    // manager of some kind.
		
		if (column == 3)
			d.setHorizontalAlignment(JLabel.CENTER);
		else 
			d.setHorizontalAlignment(JLabel.LEFT);
		
		if (column == 1){
			d.setBackground(colors.get(row));
		} else if (!isSelected){
			d.setBackground(Color.WHITE);
		}
		
		d.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
	    return this;
	}
	
}
