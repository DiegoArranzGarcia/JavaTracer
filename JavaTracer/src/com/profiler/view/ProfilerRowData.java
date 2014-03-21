package com.profiler.view;

import java.awt.Color;

import com.general.view.jtreetable.TableRowData;

public class ProfilerRowData implements TableRowData{
	
	public enum TypeData{PACKAGE,CLASS,METHOD};

	private Color color;
	private TypeData type;
	private String name;
	private String completeName;
	private Integer count;
	private boolean excluded;
	
	public ProfilerRowData(TypeData type,Color color,String name,String completeName,Integer count,boolean excluded) {
		this.type = type;
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

	public boolean isExcluded() {
		return excluded;
	}

	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}

	public TypeData getType() {
		return type;
	}

	public void setType(TypeData type) {
		this.type = type;
	}
	
}
