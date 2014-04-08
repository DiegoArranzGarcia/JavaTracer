
package com.inspector.objectinspector.view;

import com.general.view.jtreetable.TableTreeNode;
import com.inspector.objectinspector.presenter.ObjectInspectorPresenterInterface;

public interface ObjectInspectorViewInterface {

	public void setController(ObjectInspectorPresenterInterface objectInspectorPresenter);
	public void clearTable();
	public TableTreeNode getRoot();
	public void refreshTable();
    public ObjectInspectorView getView() ;
}
