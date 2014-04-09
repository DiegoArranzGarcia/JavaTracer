package com.inspector.model;

import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.view.DefaultTreeLayout;

public interface UpdateNotifier {

	public void updateInfo(int current);
	public void finishLoading(DefaultTreeLayout<Box> loadedTree);
	public void opening();
	public void opened(XmlManager xml);
	
}
