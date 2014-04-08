package com.inspector.objectinspector.view;

import java.awt.Color;

import com.general.view.jtreetable.JTreeTable;
import com.inspector.objectinspector.presenter.ObjectInspectorPresenter;
import com.inspector.objectinspector.presenter.ObjectInspectorPresenterInterface;

@SuppressWarnings("serial")
public class ObjectInspectorView extends JTreeTable implements ObjectInspectorViewInterface {
	
	private ObjectInspectorPresenterInterface presenter;
	
	public ObjectInspectorView(){
		VariableRowData rootNode = new VariableRowData("Name", "Value");
		setRoot(rootNode);
		setCellRenderer(new CellRenderer(treeModel));
		setGridColor(Color.BLACK);
	}

	public void refreshTable() {
		refreshTable(-1);
	}

	public void setController(ObjectInspectorPresenterInterface objectInspectorController) {
		this.presenter = objectInspectorController;
	}

	public ObjectInspectorPresenterInterface getPresenter() {
	    return presenter;
    }

	public void setPresenter(ObjectInspectorPresenter presenter) {
	    this.presenter = presenter;
    }

	
	public ObjectInspectorView getView() {
		return this;
	}


}
