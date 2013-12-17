package com.javatracer.view;

import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JScrollPane;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import com.javatracer.controller.TreeController;
import com.javatracer.model.info.VariableInfo;
import com.javatracer.view.tree.DefaultTreeLayout;
import com.javatracer.view.tree.TextInBox;
import com.javatracer.view.tree.TextInBoxExt;
import com.javatracer.view.tree.TextInBoxNodeExtentProvider;
import com.traceinspector.datamodel.TreeNode;

@SuppressWarnings("serial")
public class TreeInspector extends JScrollPane {
	
	private static final int DEFAULT_WIDTH_BOX = 80;
	private static final int DEFAULT_HEIGHT_BOX = 60;
	private static final int WIDTH_BY_LETTER = 8;

	TreeController controller;
	
	static TreeLayout<TextInBox> treeLayout;
	
	static DefaultTreeLayout<TextInBox> tree;
	static TextInBoxNodeExtentProvider nodeExtentProvider;
	static DefaultConfiguration<TextInBox> configuration;
	
	static TreePanel panel;
	
	public TreeInspector(TreeNode root,TreeController controller) {
		super(createTreePanel(root, controller));
		
		this.controller = controller;
		
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
	}
 
	 private static TreePanel createTreePanel(TreeNode root,TreeController controller) {
		tree = createTree(root);
		//setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		configuration = new DefaultConfiguration<TextInBox>(gapBetweenLevels, gapBetweenNodes);

		// create the NodeExtentProvider for TextInBox nodes
		nodeExtentProvider = new TextInBoxNodeExtentProvider();	
			
		//create the layout
		treeLayout = new TreeLayout<TextInBox>(tree,nodeExtentProvider,configuration);

		// Create a panel that draws the nodes and edges and show the panel
		panel = new TreePanel(treeLayout);
		panel.addMouseListener(controller);
		return panel;
	}

	private static DefaultTreeLayout<TextInBox> createTree(TreeNode rootNode){
		 
		int width = 0;
		String name = giveMeTextInBox(rootNode);
		long id = rootNode.getNode().getId();
		if (name.length()>2) width = WIDTH_BY_LETTER*name.length();
		
	    TextInBoxExt root = new TextInBoxExt(id,name, DEFAULT_WIDTH_BOX + width,DEFAULT_HEIGHT_BOX);
	    DefaultTreeLayout<TextInBox> treeLayout = new DefaultTreeLayout<TextInBox>(root);
	    
	    paintTree(rootNode,root,treeLayout); 
	    
	    return treeLayout;
	    
	 }
 
	 private static void paintTree(TreeNode treeNode,TextInBoxExt tree, DefaultTreeLayout<TextInBox> treeLayout ){
		 List<TreeNode> listcalledMethods= treeNode.getCalledMethods();
		 Iterator<TreeNode> iterator=listcalledMethods.iterator();
		 TreeNode child;  
	   
		 while(iterator.hasNext()){
			 
			 child=iterator.next();
			 int width = 0;
			 long id = child.getNode().getId();
			 String name = giveMeTextInBox(child);
			 if (name.length()>2) width = WIDTH_BY_LETTER*name.length();
			 
			 TextInBoxExt n1 = new TextInBoxExt(id,name,DEFAULT_WIDTH_BOX+width,DEFAULT_HEIGHT_BOX);
			 
			 treeLayout.addChild(tree,n1);
			 
			 if(child.getCalledMethods().size()!=0){
				 paintTree(child,n1,treeLayout);
			 }
			 
		 }
	 } 

	 private static String giveMeTextInBox(TreeNode node) {
			
		 List<VariableInfo> var=node.getNode().getVariables();
		 int i=0;
		 
		 String name = node.getNode().getMethodName() + " (";
		 
		 while(i<var.size()){
			 
			name= name + var.get(i).CompleteArgumentString();
	    	i++;
		 }		 
		 
		 if(i==0)
			 name = name.substring(0, name.length()) +" )";
		 else 
			 name = name.substring(0, name.length()-2) +" )";
		 
		 return name;
	}

	public TextInBoxExt clickedOnTree(int x, int y) {
		
		TextInBoxExt textInBoxExt = null;
		
		boolean clickedOnNode = false;
		Set<Entry<TextInBox,Double>> nodes = treeLayout.getNodeBounds().entrySet();
		Iterator<Entry<TextInBox,Double>> iterator = nodes.iterator();
		
		while (iterator.hasNext() && !clickedOnNode){
			Entry<TextInBox,Double> entry = iterator.next();
			Double rec = entry.getValue();
			if (rec.contains(x,y)) {
				textInBoxExt = (TextInBoxExt)entry.getKey();
				clickedOnNode = true;
			}
		}
		
		return textInBoxExt;
	}

	public void expandTreeNode(TextInBoxExt textInBoxExt,TreeNode node) {
				   
		updateNode(textInBoxExt,node);
		repaintTree();
		 
	}
	
	public void repaintTree() {
		
		treeLayout = new TreeLayout<TextInBox>(tree,nodeExtentProvider,configuration);
		
		panel.setTree(treeLayout);
		panel.repaint();
		
	}

	private void updateNode(TextInBoxExt textInBoxExt, TreeNode node) {
		
		Iterator<TreeNode> iterator = node.getCalledMethods().iterator();	
		
		 while(iterator.hasNext()){
			 TreeNode child=iterator.next();
			 int width = 0;
			 long id = child.getNode().getId();
			 String name = giveMeTextInBox(child);
			 if (name.length()>2) width = WIDTH_BY_LETTER*name.length();
			 TextInBoxExt n1 = new TextInBoxExt(id,name,DEFAULT_WIDTH_BOX+width,DEFAULT_HEIGHT_BOX);
			 tree.addChild(textInBoxExt, n1);
		 }
		
	}

	public void foldNode(TextInBoxExt textInBoxExt) {
		tree.removeChilds(textInBoxExt);
		repaintTree();
	}

	





}