
package com.inspector.treeinspector.view;

import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import com.inspector.treeinspector.data.Box;
import com.inspector.treeinspector.presenter.TreeInspectorPresenter;

public interface TreeInspectorViewInterface {


    void repaintTree(DefaultTreeLayout<Box> p_tree);
    void setController(TreeInspectorPresenter p_treeInspectorPresenter);
    Map<Box, Double> getNodeBounds();
    public TreeInspectorView getView();

}
