package com.profiler.view;

import java.awt.Color;

import com.general.view.jtreetable.TableRowData;

public class ProfilerRowData implements TableRowData{

	private String name;
	private Color color;
	private String completeName;
	private Integer count;
	private boolean used;
	
	public ProfilerRowData(String name, Color color,String completeName,Integer count,boolean used) {
		this.name = name;
		this.color = color;
		this.completeName = completeName;
		this.count = count;
		this.used = used;
	}

	public Object[] getValues() {	
		return new Object[]{"",name,completeName,count,used};
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getCompleteNameClass() {
		return completeName;
	}

	public void setCompleteNameClass(String completeNameClass) {
		this.completeName = completeNameClass;
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
