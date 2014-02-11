package com.traceinspector.viewlogic;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.javatracer.model.variables.data.ArrayData;
import com.javatracer.model.variables.data.IgnoredData;
import com.javatracer.model.variables.data.InfoVisitor;
import com.javatracer.model.variables.data.NullData;
import com.javatracer.model.variables.data.ObjectData;
import com.javatracer.model.variables.data.StringData;
import com.traceinspector.objectinspectorview.ObjectInspectorView;

public class ObjectInspectorVisitor implements InfoVisitor{
	
	private ObjectInspectorView view;
	
	private boolean expandable;
	private String mainInfo;
	
	public ObjectInspectorVisitor(ObjectInspectorView view){
		this.view = view;
		this.mainInfo = "";
		this.expandable = false;
	}

	@Override
	public void visit(ArrayData info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringData info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NullData info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ObjectData info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IgnoredData ignoredClass) {
		// TODO Auto-generated method stub
		
	}

	

}
