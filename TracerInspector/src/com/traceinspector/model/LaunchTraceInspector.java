package com.traceinspector.model;

import com.traceinspector.datamodel.TreeNode;

public class LaunchTraceInspector {

	public static void main(String[] args){
		
		TreeGenerator treeGen = new TreeGenerator();
		TreeNode tree = treeGen.loadFromFile("output.xml");
	
		printTree(tree,0);
	}
	
	public static void printTree(TreeNode tree,int nivel){
		
		
		String tab = "";
		for (int i = 0;i<nivel;i++) tab += "\t";
	//	System.out.println(tab+nivel);
	//	System.out.println(tab+"Hijos:");
		
		for (int i=0;i<tree.getCalledMethods().size();i++)
			printTree(tree.getCalledMethods().get(i),nivel+1);
		
	}
	
}
