package com.inspector.treeinspector.data;

public abstract class Box {

	protected long id;
	protected String path;
	protected boolean expanded;
	protected boolean selected;
	protected boolean haveChildren;
	
	public abstract String getBoxText();
	
	public Box(){
		this.expanded = false;
		this.selected = false;
		this.haveChildren = false;
	}
		
	public Box(String path, long id, boolean haveChildren) {
		this.path = path;
		this.id = id;
		this.expanded = false;
		this.selected = false;
		this.haveChildren = haveChildren;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean haveChildren() {
		return haveChildren;
	}

	public void setHaveChildren(boolean haveChildren) {
		this.haveChildren = haveChildren;
	}

	public String getPath() {
		return this.path;
	}
}
