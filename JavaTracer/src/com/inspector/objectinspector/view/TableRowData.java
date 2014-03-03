package com.inspector.objectinspector.view;

public class TableRowData
{
	private String name;
	private String value;
	private boolean expanded;
	private boolean isExpandable;
	private boolean changed;

	public TableRowData(String name, String value, boolean isExpandable)
	{
		this.name = name;
		this.value = value;
		this.isExpandable = isExpandable;
		this.expanded = false;
		this.changed = false;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isExpandable() {
		return isExpandable;
	}

	public void setExpandable(boolean isExpandable) {
		this.isExpandable = isExpandable;
	}

	public boolean isChanged() {
	    return changed;
    }

	public void setChanged(boolean changed) {
	   this. changed = changed;
    }
}
