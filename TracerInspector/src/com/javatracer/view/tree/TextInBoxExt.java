package com.javatracer.view.tree;


public class TextInBoxExt extends TextInBox {

	private long id;
	private boolean expanded; 
	
	public TextInBoxExt(long id,String text, int width, int height) {
		super(text, width, height);
		this.id = id;
		this.expanded = false;
	}

	public long getId() {
		return this.id;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	
	

}
