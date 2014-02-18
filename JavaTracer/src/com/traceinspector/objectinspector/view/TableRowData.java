package com.traceinspector.objectinspector.view;

public class TableRowData
{
	private String name;
	private String value;
	private boolean expanded;
	private boolean isExpandable;

	public TableRowData(String name, String value,boolean isExpandable)
	{
		this.name = name;
		this.value = value;
		this.isExpandable = isExpandable;
		this.expanded = false;
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
}
