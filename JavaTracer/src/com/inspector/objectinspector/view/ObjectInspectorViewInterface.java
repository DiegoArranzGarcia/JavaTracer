
package com.inspector.objectinspector.view;

import com.general.view.jtreetable.TableTreeNode;
import com.inspector.objectinspector.presenter.ObjectInspectorPresenterInterface;

public interface ObjectInspectorViewInterface {

    void setController(ObjectInspectorPresenterInterface objectInspectorPresenter);
    void clearTable();
    TableTreeNode getRoot();
    void refreshTable();
    public ObjectInspectorView getView() ;
}
