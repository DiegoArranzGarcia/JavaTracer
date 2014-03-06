package com.profiler.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class ProfileTableRenderer extends DefaultTableCellRenderer{
		
	private List<Color> colors;
	private Border paddingBorder;
	
	public ProfileTableRenderer(){
		this.colors = new ArrayList<>();
		this.paddingBorder = BorderFactory.createEmptyBorder(0,6,0,0);
	}
	
	public void setColors(List<Color> colors){
		this.colors = colors;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel d = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
		
		/*if (column == 0){
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected((boolean) value);
			checkBox.setHorizontalAlignment(JCheckBox.CENTER);
			return checkBox;
		}*/
		
		if (column == 1)
			d.setBackground(colors.get(row));
		else if (!isSelected)
			d.setBackground(Color.WHITE);	
		
		if (column == 2)
			d.setBorder(paddingBorder);
		else
			d.setBorder(null);
		
		if (column == 3)
			d.setHorizontalAlignment(JLabel.CENTER);
		else 
			d.setHorizontalAlignment(JLabel.LEFT);
		
	    return this;
	}
	
}
