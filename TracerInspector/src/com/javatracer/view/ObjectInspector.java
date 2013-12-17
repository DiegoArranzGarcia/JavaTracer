package com.javatracer.view;

import javax.swing.JTable;

public class ObjectInspector extends JTable{

	public ObjectInspector(int heightWindow) {
   	  super(heightWindow/25,2);
      setRowHeight(25);	
	}

}
