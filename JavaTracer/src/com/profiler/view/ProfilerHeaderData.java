package com.profiler.view;

import com.general.view.jtreetable.TableRowData;

public class ProfilerHeaderData implements TableRowData{

	public static String COLOR_TITLE = "";
	public static String SIMPLE_NAME_TITLE = "Name";
	public static String COMPLETE_NAME_TITLE = "Complete name";
	public static String COUNT_TITLE = "Count";
	public static String USED_TITLE = "Used";
	
	public String[] getValues() {
		return new String[]{COLOR_TITLE,SIMPLE_NAME_TITLE,COMPLETE_NAME_TITLE,COUNT_TITLE,USED_TITLE};
	}

}
