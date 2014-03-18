package com.inspector.model;

import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.view.DefaultTreeLayout;

public interface UpdateNotifier {

	public void updateInfo(int current,int total,int percentage);
	public void finishLoading(DefaultTreeLayout<Box> loadedTree);
	
}
