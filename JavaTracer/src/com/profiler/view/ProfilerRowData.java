package com.profiler.view;

import java.awt.Color;

import com.general.view.jtreetable.TableRowData;

public class ProfilerRowData implements TableRowData{

	private String nameClass;
	private Color color;
	private String completeNameClass;
	private Integer count;
	private boolean used;
	
	public ProfilerRowData(String nameClass, Color color,String completeNameClass,Integer count,boolean used) {
		this.nameClass = nameClass;
		this.color = color;
		this.completeNameClass = completeNameClass;
		this.count = count;
		this.used = used;
	}

	public Object[] getValues() {	
		return new Object[]{"",nameClass,completeNameClass,count,used};
	}
	
	public String getNameClass() {
		return nameClass;
	}

	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getCompleteNameClass() {
		return completeNameClass;
	}

	public void setCompleteNameClass(String completeNameClass) {
		this.completeNameClass = completeNameClass;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
	
}
