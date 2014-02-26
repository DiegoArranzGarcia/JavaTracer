package com.inspector.objectinspector.view;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.general.imageresources.ImageLoader;
import com.inspector.objectinspector.model.TableRowData;

@SuppressWarnings("serial")
public class TreeTableCellRenderer extends DefaultTreeCellRenderer {
	
	ImageLoader imageLoader;

	public TreeTableCellRenderer(){
		this.imageLoader = ImageLoader.getInstance();
		setClosedIcon(imageLoader.getExpandedIcon());
		setOpenIcon(imageLoader.getFoldedIcon());
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
        		setClosedIcon(imageLoader.getExpandedIcon());
        		setOpenIcon(imageLoader.getFoldedIcon());
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
