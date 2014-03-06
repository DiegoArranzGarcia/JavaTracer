package com.profiler.view;

import com.general.view.jtreetable.TableRowData;

public class ProfilerHeaderData implements TableRowData{

	String USED_TITLE = "Used Classes";
	String COLOR_TITLE = "";
	String CLASS_TITLE = "Class name";
	String COUNT_TITLE = "Count";
	
	public Object[] getValues() {
		return new Object[]{USED_TITLE,COLOR_TITLE,CLASS_TITLE,COUNT_TITLE};
	}
	
}
