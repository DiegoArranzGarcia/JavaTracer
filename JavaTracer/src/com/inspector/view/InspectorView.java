package com.inspector.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import com.general.resources.ImageLoader;
import com.inspector.controller.InspectorController;
import com.inspector.objectinspector.presenter.ObjectInspectorPresenter;
import com.inspector.objectinspector.view.ObjectInspectorViewInterface;
import com.inspector.treeinspector.presenter.TreeInspectorPresenter;
import com.inspector.treeinspector.view.TreeInspectorView;
import com.inspector.treeinspector.view.TreeInspectorViewInterface;


@SuppressWarnings("serial")
public class InspectorView extends JFrame implements ComponentListener,ActionListener{
	
	private static final String TITLE = "Inspector";

	private static String WINDOW_TITLE = TITLE;
	
	private static double DIVIDER_SPLIT = 0.75;
	private static double PERCENTAGE = 0.75;
	
	private InspectorController controller;

	private JMenuItem menuOpen;
	private JMenuItem menuExit;
	private JMenuItem mntmSettings;
	private JMenuItem mntmHelp;
	private JMenuItem mntmAbout;
	
	public InspectorView(TreeInspectorPresenter treeInspector, ObjectInspectorPresenter objectInspector) {
		
		setIconImage(ImageLoader.getInstance().getApplicationIcon().getImage());
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
		
		TreeInspectorViewInterface treeView = treeInspector.getView();
		scrollPane_1.setViewportView(treeView.getView());
		splitPane.setDividerLocation((int)(getWidth()*DIVIDER_SPLIT));
		
		ObjectInspectorViewInterface objectview = objectInspector.getView();
		scrollPane.setViewportView(objectview.getView());
		
		createMenu();
	}	
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		menuOpen = new JMenuItem("Load Trace");
		menuOpen.addActionListener(this);
		menuExit = new JMenuItem("Exit");
		menuExit.addActionListener(this);
		
		setJMenuBar(menuBar);
		
		mntmSettings = new JMenuItem("Settings");
		menuBar.add(mntmSettings);
		mntmSettings.addActionListener(this);
		
		mntmHelp = new JMenuItem("Help");
		menuBar.add(mntmHelp);
		mntmHelp.addActionListener(this);
		
		mntmAbout = new JMenuItem("About");
		menuBar.add(mntmAbout);
		mntmAbout.addActionListener(this);

		menuFile.add(menuOpen);
		menuFile.add(menuExit);
	}

	public void setTitle(String title){
		super.setTitle(TITLE + " - " + title);
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
		Object source = e.getSource();
		if (source.equals(menuOpen)){
			controller.clickedOnLoadTrace(this);
		} else if(source.equals(menuExit)){
			controller.back();
		} else if(source.equals(mntmSettings)){
			controller.clickedOnSettings();
		} else if(source.equals(mntmHelp)){
			controller.clickedOnHelp();
		} else if(source.equals(mntmAbout)){
			controller.clickedOnAbout();
		}
	}

	public void setController(InspectorController controller){
		this.controller = controller;
	}

}
