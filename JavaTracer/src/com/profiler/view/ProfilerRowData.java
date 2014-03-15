package com.profiler.view;

import java.awt.Color;

import com.general.view.jtreetable.TableRowData;

public class ProfilerRowData implements TableRowData{

	private String name;
	private Color color;
	private String completeName;
	private Integer count;
	private boolean excluded;
	
	public ProfilerRowData(String name, Color color,String completeName,Integer count,boolean excluded) {
		this.name = name;
		this.color = color;
		this.completeName = completeName;
		this.count = count;
		this.excluded = excluded;
	}

	public Object[] getValues() {	
		return new Object[]{"",name,completeName,count,excluded};
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
		return excluded;
	}

	public void setUsed(boolean excluded) {
		this.excluded = excluded;
	}
	
}
