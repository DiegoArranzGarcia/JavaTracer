package com.traceinspector.objectinspectorview;

public class TableRowData
{
	private String name;
	private String value;
	private boolean isExpandable;

	public TableRowData(String name, String value,boolean isExpandable)
	{
		this.name = name;
		this.value = value;
		this.isExpandable = isExpandable;
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
