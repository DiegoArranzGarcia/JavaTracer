package com.traceinspector.objectinspectorview;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class MyTreeModel extends AbstractTreeTableModel
{
	private String [] titles = {"Name","Value"};

	
	public MyTreeModel(DefaultMutableTreeNode root)
	{
		super(root);
	}
	 
	/**
	 * Table Columns
	 */
	public String getColumnName(int column) {
		if (column < titles.length)
			return (String) titles[column];
		else
			return "";
	}

	public int getColumnCount()
	{
		return titles.length;
	}
	
	public Class<?> getColumnClass(int column)
	{
		return String.class;
	}

	public Object getValueAt(Object arg0, int arg1)
	{
		if(arg0 instanceof TableRowData)
		{
			TableRowData data = (TableRowData)arg0;
			if(data != null)
			{
				switch(arg1)
				{
				case 0: return data.getName();
				case 1: return data.getValue();
				}
			}
			
		}
		
		if(arg0 instanceof DefaultMutableTreeNode)
		{
			DefaultMutableTreeNode dataNode = (DefaultMutableTreeNode)arg0;
			TableRowData data = (TableRowData)dataNode.getUserObject();
			if(data != null)
			{
				switch(arg1)
				{
				case 0: return data.getName();
				case 1: return data.getValue();
				}
			}
			
		}
		return null;
	}
	
	public Object getChild(Object arg0, int arg1)
	{
		
		if(arg0 instanceof DefaultMutableTreeNode)
		{
			DefaultMutableTreeNode nodes = (DefaultMutableTreeNode)arg0;
			return nodes.getChildAt(arg1);
		}
		return null;
	}

	public int getChildCount(Object arg0)
	{
		
		if(arg0 instanceof DefaultMutableTreeNode)
		{
			DefaultMutableTreeNode nodes = (DefaultMutableTreeNode)arg0;
			return nodes.getChildCount();
		}
		return 0;
	}

	public int getIndexOfChild(Object arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	 public boolean isLeaf(Object node) 
	 {
	        return getChildCount(node) == 0;
	 }

	public void clearTable(){
			
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
		root.removeAllChildren();
			
	}
	 
	public DefaultMutableTreeNode addNodeToRoot(String name, String value, boolean b) {
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TableRowData(name,value,b));
		root.add(node);
		return node;
	}

	public DefaultMutableTreeNode addNodeToNode(DefaultMutableTreeNode node,
			String name, String value, boolean b) {
		
		DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(new TableRowData(name,value,b));
		node.add(addedNode);
		return addedNode;
	}

}
