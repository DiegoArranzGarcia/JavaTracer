package com.traceinspector.model;

import com.javatracer.view.TreeInspector;
import com.traceinspector.datamodel.TreeNode;

public class LaunchTraceInspector {

	public static void main(String[] args){
		
		TreeGenerator treeGen = new TreeGenerator();
		TreeNode tree = treeGen.loadFromFile("output.xml");
		TreeInspector view = new TreeInspector(tree);
	}
	
	
}
