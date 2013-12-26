package com.traceinspector.objectinspectorview;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeTableCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 5593629042737938947L;

	public TreeTableCellRenderer() {

    	setOpenIcon(new ImageIcon("resource/minus.png"));
    	setClosedIcon(new ImageIcon("resource/plus.png"));
    	
    }

	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,
			int row,boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,false, leaf, row,hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if(node != null && node.getUserObject() != null && (node.getUserObject() instanceof TableRowData))
        {
        	TableRowData item = (TableRowData)(node.getUserObject());
        	setText(item.getName());
        	if(item.isExpandable())
        	{
            	setOpenIcon(new ImageIcon("resource/minus.png"));
            	setClosedIcon(new ImageIcon("resource/plus.png"));        		
        	}
        	else
        	{
            	setOpenIcon(null);
        	}
        }
        else
        {
        	setIcon(null);
        }
        return this;

    }

}
