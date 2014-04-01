package com.inspector.treeinspector.data;

public abstract class Box {

	protected long id;
	protected String path;
	protected boolean expanded;
	protected boolean selected;
	protected boolean haveChildren;
	private boolean visible;
	private boolean loaded;
	
	public abstract String getBoxText();
	
	public Box(){
		this.expanded = false;
		this.selected = false;
		this.haveChildren = false;
		this.visible = true;
		this.loaded = false;
	}
		
	public Box(String path, long id, boolean haveChildren) {
		this.path = path;
		this.id = id;
		this.expanded = false;
		this.selected = false;
		this.visible = true;
		this.haveChildren = haveChildren;
		this.loaded = !haveChildren;
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

	public boolean isVisible() {
		return visible;
	}
	
	public void setLoaded(boolean loaded){
		this.loaded = loaded;
	}
	
	public boolean isLoaded() {
		return loaded;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
