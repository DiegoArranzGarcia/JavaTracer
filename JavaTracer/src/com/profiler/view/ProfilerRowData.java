package com.profiler.view;

import com.general.view.jtreetable.TableRowData;

public class ProfilerRowData implements TableRowData{

	private boolean used;
	private String color;
	private String nameClass;
	private Integer count;
	
	public ProfilerRowData(boolean used, String color, String nameClass, Integer count) {
		this.used = used;
		this.color = color;
		this.nameClass = nameClass;
		this.count = count;
	}

	public Object[] getValues() {	
		return new Object[]{used,color,nameClass,count};
	}
	
}
