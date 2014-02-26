package com.inspector.view;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import com.inspector.controller.InspectorController;
import com.inspector.objectinspector.view.ObjectInspectorView;
import com.inspector.treeinspectorview.TreeInspectorView;


@SuppressWarnings("serial")
public class InpectorView extends JFrame implements ComponentListener,ActionListener{
	
	private static String WINDOW_TITLE = "Inspector";
	
	private static double DIVIDER_SPLIT = 0.75;
	private static double PERCENTAGE = 0.75;
	
	private InspectorController controller;
	
	private JSplitPane split;
	private JMenuItem[] items;
	
	public InpectorView() {
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setTitle(WINDOW_TITLE);
		Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)(d.width*PERCENTAGE),(int)(d.height*PERCENTAGE));
		setLocationRelativeTo(null);
		addComponentListener(this);
				
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(new JPanel());
		split.setRightComponent(new JPanel());
		split.setDividerLocation(DIVIDER_SPLIT);
		split.setEnabled(false);
		setContentPane(split);
		
		createMenu();
	}	
	
	public void loadTreeInspector(TreeInspectorView treeInspectorView){
		
		split.setLeftComponent(treeInspectorView);
		split.setDividerLocation(DIVIDER_SPLIT);
	}
	
	public void loadObjectInspector(ObjectInspectorView objectInspectorView){

		split.setRightComponent(objectInspectorView);
		split.setDividerLocation(DIVIDER_SPLIT);
		
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		items = new JMenuItem[2];
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem menuOpen = new JMenuItem("Open");
		JMenuItem menuExit = new JMenuItem("Exit");
		menuFile.add(menuOpen,0);
		menuFile.add(menuExit,1);
		items[0] = menuOpen;
		items[1] = menuExit;
		
		setJMenuBar(menuBar);
		
		menuOpen.addActionListener(this);
		menuExit.addActionListener(this); 
	}

	public void componentHidden(ComponentEvent e) {
		controller.back();
	}

	public void componentMoved(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
		split.setDividerLocation(DIVIDER_SPLIT);
	}

	public void componentShown(ComponentEvent e) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == items[0]){
			controller.clickedOpen();
		}else if(e.getSource() == items[1])
			controller.back();
	}

	public void setController(InspectorController controller){
		this.controller = controller;
	}
}
