package com.javatracer.view;

import java.awt.Container;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.demo.TextInBoxNodeExtentProvider;
import org.abego.treelayout.demo.swing.TextInBoxTreePane;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import com.traceinspector.datamodel.TreeNode;

public class TreeInspector {

	 /**
	  * Returns a "Sample" tree with {@link TextInBox} items as nodes.
	 * @param tree2 
	  */
	
	public TreeInspector(TreeNode root){

		  TreeForTreeLayout<TextInBox> tree = createTree(root);
		    
		  // setup the tree layout configuration
		  double gapBetweenLevels = 50;
		  double gapBetweenNodes = 10;
		  DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
		    gapBetweenLevels, gapBetweenNodes);

		  // create the NodeExtentProvider for TextInBox nodes
		  TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		  // create the layout
		  TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
		    nodeExtentProvider, configuration);

		  // Create a panel that draws the nodes and edges and show the panel
		  TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
		  showInDialog(panel);
	}
 
	 private DefaultTreeForTreeLayout<TextInBox> createTree(TreeNode rootNode){
		 
		int width = 0;
		String name = rootNode.getNode().getMethodName();
		if (name.length()>2) width = 5*name.length();
	    TextInBox root = new TextInBox(rootNode.getNode().getMethodName(), 40 + width, 30);
	    DefaultTreeForTreeLayout<TextInBox> treeLayout = new DefaultTreeForTreeLayout<TextInBox>(root);
	    
	    painTree(rootNode,root,treeLayout); 
	    
	    return treeLayout;
	    
	 }
 
	 private void painTree(TreeNode treeNode,TextInBox tree, DefaultTreeForTreeLayout<TextInBox> treeLayout ){
		 List<TreeNode> listcalledMethods= treeNode.getCalledMethods();
		 Iterator<TreeNode> iterator=listcalledMethods.iterator();
		 TreeNode child;  
	   
		 while(iterator.hasNext()){
			 child=iterator.next();
			 int width = 0;
			 String name = child.getNode().getMethodName();
			 if (name.length()>2) width = 5*name.length();
			 TextInBox n1 = new TextInBox(name,30+width,30);
			 treeLayout.addChild(tree, n1);
			 if(child.getCalledMethods().size()!=0){
				 painTree(child,n1,treeLayout);
			 }
		 }
	 } 

	 private void showInDialog(JComponent panel) {
		  JDialog dialog = new JDialog();
		  Container contentPane = dialog.getContentPane();
		  ((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
		    10, 10, 10, 10));
		  contentPane.add(panel);
		  dialog.pack();
		  dialog.setLocationRelativeTo(null);
		  dialog.setVisible(true);
	}
}