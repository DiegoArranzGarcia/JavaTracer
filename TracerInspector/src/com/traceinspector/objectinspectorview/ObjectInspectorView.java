package com.traceinspector.objectinspectorview;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

import com.traceinspector.viewlogic.ObjectInspector;

@SuppressWarnings("serial")
public class ObjectInspectorView extends JScrollPane implements KeyListener,MouseListener{
	
	static MyTreeModel model;
	static JXTreeTable binTree;
	
	public ObjectInspectorView(ObjectInspector controller) {
   	    super(createTable(controller));   	    
   	    binTree.addMouseListener(this);
   	    binTree.addKeyListener(this);
	}

	private static Component createTable(ObjectInspector controller){
		
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

	public DefaultMutableTreeNode addVariable(String name, String value,boolean isExpandable) {
		
		DefaultMutableTreeNode node = model.addNodeToRoot(name,value,isExpandable);		
		binTree.updateUI();
		return node;
	}

	
	public DefaultMutableTreeNode addVariableToNode(String name, String value,boolean isExpandable,DefaultMutableTreeNode node) {
		
		DefaultMutableTreeNode addedNode = model.addNodeToNode(node,name,value,isExpandable);		
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

	public void mouseClicked(MouseEvent e) {
		
		if (e.getX() < 20){
			TreePath path = binTree.getPathForLocation(e.getX(),e.getY());
			if (path != null)
				expandOrCollapse(path);
		}
				
	}

	private void expandOrCollapse(TreePath path) {
		if (binTree.isExpanded(path))
			binTree.collapsePath(path);
		else
			binTree.expandAll();
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void keyPressed(KeyEvent e) {
		int selectedRow = binTree.getSelectedRow();
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			
			if (!binTree.isExpanded(selectedRow)){
				binTree.expandRow(selectedRow);
			}
			
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT){
			
			if (binTree.isExpanded(selectedRow)){
				binTree.collapseRow(selectedRow);
			}
			
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

}
