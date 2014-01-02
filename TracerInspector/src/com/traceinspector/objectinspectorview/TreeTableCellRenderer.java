package com.traceinspector.objectinspectorview;

import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeTableCellRenderer extends DefaultTreeCellRenderer {

	public TreeTableCellRenderer(){
		setOpenIcon(new ImageIcon(getClass().getResource("plus.gif")));
    	setClosedIcon(new ImageIcon(getClass().getResource("minus.gif")));
	}
	
	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,
			int row,boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,expanded,leaf,row,hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if(node != null && node.getUserObject() != null && (node.getUserObject() instanceof TableRowData))
        {
        	TableRowData item = (TableRowData)(node.getUserObject());
        	setText(item.getName());
        	if (item.isExpandable())
        	{
        		setClosedIcon(new ImageIcon(getClass().getResource("plus.gif")));
        		setOpenIcon(new ImageIcon(getClass().getResource("plus.gif")));
        	}
        	else
        	{
            	setIcon(null);
        	}
        }
        else
        {
        	setIcon(null);
        }
        return this;

    }

}
