package com.inspector.objectinspector.view;

public class TableRowData
{
	private String name;
	private String value;
	private boolean changed;

	public TableRowData(String name, String value, boolean isExpandable)
	{
		this.name = name;
		this.value = value;
		this.changed = false;
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

	public boolean isChanged() {
	    return changed;
    }

	public void setChanged(boolean changed) {
	   this. changed = changed;
    }
}
