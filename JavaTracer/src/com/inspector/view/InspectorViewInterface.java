
package com.inspector.view;

import com.inspector.presenter.InspectorPresenterInterface;

public interface InspectorViewInterface {

    void setTitle(String p_name);
    void setController(InspectorPresenterInterface p_inspectorPresenter);
    void setVisible(boolean p_b);
    public InspectorView getView();

}
