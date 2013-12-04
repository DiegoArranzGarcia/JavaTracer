package com.javatracer.view;


public class TextInBoxExt extends TextInBox {

	private long id;
	
	public TextInBoxExt(long id,String text, int width, int height) {
		super(text, width, height);
		this.id = id;
	}

	public long getId() {
		return this.id;
	}
	

}
