package com.traceinspector.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.StringValue;

import com.traceinspector.objectinspectorview.MyTreeModel;
import com.traceinspector.objectinspectorview.TableRowData;
import com.traceinspector.objectinspectorview.TreeTableCellRenderer;
import com.traceinspector.viewlogic.ObjectInspector;

@SuppressWarnings("serial")
public class ObjectInspectorView extends JScrollPane{
	
	static MyTreeModel model;
	static JXTreeTable binTree;
	private ObjectInspector controller;
	
	public ObjectInspectorView(ObjectInspector controller) {
   	    super(createTable());
   	    this.controller = controller;
	}

	private static Component createTable(){
		
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new TableRowData("Name","Value",true));
		model = new MyTreeModel(rootNode);
		binTree = new JXTreeTable(model);
    	
    	Highlighter highligher = HighlighterFactory.createSimpleStriping(HighlighterFactory.BEIGE);
    	binTree.setHighlighters(highligher);
        binTree.setShowsRootHandles(false);
        configureCommonTableProperties(binTree);
        binTree.setTreeCellRenderer(new TreeTableCellRenderer());
        
        return binTree;
    }
    
    private static void  configureCommonTableProperties(JXTable table) {
        table.setColumnControlVisible(true);
        StringValue toString = new StringValue() {

            public String getString(Object value) {
                if (value instanceof Point) {
                    Point p = (Point) value;
                    return createString(p.x, p.y);
                } else if (value instanceof Dimension) {
                    Dimension dim = (Dimension) value;
                    return createString(dim.width, dim.height);
                }
               return "";
            }

            private String createString(int width, int height) {
                return "(" + width + ", " + height + ")";
            }
            
        };
        TableCellRenderer renderer = new DefaultTableRenderer(toString);
        table.setDefaultRenderer(Point.class, renderer);
        table.setDefaultRenderer(Dimension.class, renderer);
	}

	public DefaultMutableTreeNode addVariable(String name, String value) {
		
		DefaultMutableTreeNode node = model.addNodeToRoot(name,value,true);		
		binTree.updateUI();
		return node;
	}

	
	public DefaultMutableTreeNode addVariableToNode(String name, String value,DefaultMutableTreeNode node) {
		
		DefaultMutableTreeNode addedNode = model.addNodeToNode(node,name,value,false);		
		binTree.updateUI();
		return addedNode;
	}

	public void clear() {
				
		model.clearTable();
		binTree.updateUI();

	}

	public void showVariables() {
        binTree.expandPath(new TreePath(model.getRoot()));
        binTree.updateUI();
	}

}
