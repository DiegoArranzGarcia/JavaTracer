package com.inspector.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.inspector.controller.InspectorController;
import com.inspector.objectinspector.controller.ObjectInspectorController;
import com.inspector.objectinspector.view.ObjectInspectorView;
import com.inspector.treeinspector.controller.TreeInspectorController;
import com.inspector.treeinspector.view.TreeInspectorView;


@SuppressWarnings("serial")
public class InpectorView extends JFrame implements ComponentListener,ActionListener{
	
	private static String WINDOW_TITLE = "Inspector";
	
	private static double DIVIDER_SPLIT = 0.75;
	private static double PERCENTAGE = 0.75;
	
	private InspectorController controller;
	
	private JSplitPane split;
	private JMenuItem[] items;
	
	public InpectorView(TreeInspectorController treeInspector, ObjectInspectorController objectInspector) {
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setTitle(WINDOW_TITLE);
		Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)(d.width*PERCENTAGE),(int)(d.height*PERCENTAGE));
		setLocationRelativeTo(null);
		addComponentListener(this);
				
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_1);
		
		TreeInspectorView treeView = treeInspector.getView();
		scrollPane_1.setViewportView(treeView);
		splitPane.setDividerLocation((int)(getWidth()*DIVIDER_SPLIT));
		
		ObjectInspectorView objectview = objectInspector.getView();
		scrollPane.setViewportView(objectview);
		
		createMenu();
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
		//split.setDividerLocation(DIVIDER_SPLIT);
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
