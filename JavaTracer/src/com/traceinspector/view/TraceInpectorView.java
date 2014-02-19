package com.traceinspector.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.traceinspector.controller.TraceInspectorController;
import com.traceinspector.objectinspector.view.ObjectInspectorView;
import com.traceinspector.treeinspectorview.TreeInspectorView;


@SuppressWarnings("serial")
public class TraceInpectorView extends JFrame implements ComponentListener,ActionListener{
	
	private static double DIVIDER_SPLIT = 0.75;
	
	private TraceInspectorController controller;
	
	private JSplitPane split;
	private JMenuItem[] items;
	
	public TraceInpectorView(TraceInspectorController controller) {
		
		this.controller = controller;
		
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TraceInspector");
		addComponentListener(this);
				
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(new JPanel());
		split.setRightComponent(new JPanel());
		split.setDividerLocation(DIVIDER_SPLIT);
		split.setEnabled(false);
		setContentPane(split);
		
		createMenu(controller);
	}	
	
	public void loadTreeInspector(TreeInspectorView treeInspectorView){
		
		split.setLeftComponent(treeInspectorView);
		split.setDividerLocation(DIVIDER_SPLIT);
	}
	
	public void loadObjectInspector(ObjectInspectorView objectInspectorView){

		split.setRightComponent(objectInspectorView);
		split.setDividerLocation(DIVIDER_SPLIT);
		
	}
	
	private void createMenu(TraceInspectorController controller) {
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

	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
		split.setDividerLocation(DIVIDER_SPLIT);
	}

	public void componentShown(ComponentEvent e) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == items[0]){
			controller.clickedOpen();
		}else if(e.getSource() == items[1])
			System.exit(0);
	}

}
