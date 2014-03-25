package com.tracer.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alee.extended.filechooser.FilesSelectionListener;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.general.model.FileUtilities;
import com.general.resources.ImageLoader;
import com.general.view.WebFileChooserField;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.tracer.controller.TracerController;

@SuppressWarnings("serial")
public class TracerView extends JFrame implements ActionListener, FilesSelectionListener{
	
	private static final String ADD_ARGUMENTS = "Add arguments";
	private static final String ABOUT = "About";
	private static final String HELP = "Help";
	private static final String SETTINGS = "Settings";
	private static final String EXIT = "Exit";
	private static final String LOAD_TRACE = "Load Trace";
	private static final String LOAD_PROFILE = "Load Profile";
	private static final String PROFILE = "Profile";
	private static final String FILE = "File";
	private static final String TRACE = "Trace";
	
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT_NO_CONSOLE = 285;
	private static final int WINDOW_HEIGHT_CONSOLE = 600;
	
	private static String LABEL_PATH = "Select a directory";
	private static String HELP_PATH_TOOLTIP = "Select a directory where all .class are located.";
	private static String LABEL_NAME_CLASS = "Select a class";
	
	private TracerController presenter;
	
	private WebButton trace,exit,addArgument,profile;
	private WebComboBox nameClass;
	private WebFileChooserField chooser;
	private WebLabel labelPath,labelNameClass;
	private WebLabel helpNameClass,helpPath;
	private JComponent console;
	
	private WebMenuItem mntmLoadProfile;
	private WebMenuItem mntmLoadTrace;
	private WebMenuItem mntmExit;
	private WebMenuItem mntmTrace;
	private WebMenuItem mntmProfile;
	private WebMenuItem mntmSettings;
	private WebMenuItem mntmHelp;
	private WebMenuItem mntmAbout;
	private String nameXml;
	
	public TracerView(JComponent console) {
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		setTitle("Java Tracer");
		setSize(876,285);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(imageLoader.getApplicationIcon().getImage());
		
		this.console = console;
		
		chooser = new WebFileChooserField();
		chooser.getLastComponent().setBounds(427, 2, 21, 26);
		chooser.getFirstComponent().setBounds(10, 2, 422, 26);
		chooser.getChooseButton().setBounds(427, 2, 21, 26);
		chooser.setBounds(175, 60, 450, 30);
		chooser.setTitle("Select a directory or jar file");
		String currentDirectory = System.getProperty("user.dir");
		chooser.setCurrentDirectory(currentDirectory);
		FileNameExtensionFilter jarFilter = new FileNameExtensionFilter("Jar Files","jar");
		chooser.setFileFilter(jarFilter);
		chooser.setMultiSelectionEnabled(false);
		chooser.setShowFileShortName(false);	
		chooser.addSelectedFilesListener(this);
		
		helpPath = new WebLabel();
		helpPath.setBounds(667, 60, 24, 24);
		helpPath.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpPath.setToolTipText(HELP_PATH_TOOLTIP);
		
		addArgument = new WebButton(ADD_ARGUMENTS);
		addArgument.setBounds(716, 115, 120, 24);
		addArgument.addActionListener(this);
				
		nameClass = new WebComboBox();
		nameClass.setBounds(175, 115, 450, 30);
		nameClass.setBackground(Color.white); 
		nameClass.setEditable(false);
		nameClass.setEnabled(false); 
		nameClass.addActionListener(this);
		
		helpNameClass = new WebLabel();
		helpNameClass.setBounds(667, 115, 24, 24);
		helpNameClass.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpNameClass.setToolTipText(LABEL_NAME_CLASS);
			 
		exit =new WebButton(EXIT);
		exit.setBounds(497, 182, 120, 30);
		exit.setBackground(Color.white); 
		exit.addActionListener(this);
		
		trace = new WebButton(TRACE);
		trace.setBounds(335, 182, 120, 30);
		trace.setLayout(new GridLayout(1,1));
		trace.setBackground(Color.white);  
		trace.setEnabled(false); 
		trace.addActionListener(this);
	
		profile = new WebButton(PROFILE);
		profile.setBounds(170, 182, 120, 30);
		profile.setLayout(new GridLayout(1,1));
		profile.setBackground(Color.white);  
		profile.setEnabled(false); 
		profile.addActionListener(this);
		getContentPane().setLayout(null);
		
		labelPath = new WebLabel(LABEL_PATH);
		labelPath.setBounds(23, 60, 130, 30);
		labelPath.setBackground(Color.white);
		getContentPane().add(labelPath);
		getContentPane().add(chooser);
		chooser.setLayout(null);
		
		labelNameClass = new WebLabel(LABEL_NAME_CLASS);
		labelNameClass.setBounds(23, 115, 100, 30);
		labelNameClass.setBackground(Color.white); 
		getContentPane().add(labelNameClass);
		
		
		getContentPane().add(trace);
		getContentPane().add(exit);
		getContentPane().add(nameClass);
		getContentPane().add(helpPath);
		getContentPane().add(helpNameClass);
		getContentPane().add(addArgument);
		getContentPane().add(profile);
		getContentPane().add(console, "3, 11, 11, 2, fill, fill");
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 994, 26);
		getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu(FILE);
		mnFile.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnFile);
		
		mntmTrace = new WebMenuItem(TRACE);
		mnFile.add(mntmTrace);
		mntmTrace.addActionListener(this);
		
		mntmProfile = new WebMenuItem(PROFILE);
		mnFile.add(mntmProfile);
		mntmProfile.addActionListener(this);
		
		mntmLoadProfile = new WebMenuItem(LOAD_PROFILE);
		mnFile.add(mntmLoadProfile);
		mntmLoadProfile.addActionListener(this);
		
		mntmLoadTrace = new WebMenuItem(LOAD_TRACE);
		mnFile.add(mntmLoadTrace);
		mntmLoadTrace.addActionListener(this);
		
		mntmExit = new WebMenuItem(EXIT);
		mnFile.add(mntmExit);
		mntmExit.addActionListener(this);
		
		mntmSettings = new WebMenuItem(SETTINGS);
		menuBar.add(mntmSettings);
		mntmSettings.addActionListener(this);
		
		mntmHelp = new WebMenuItem(HELP);
		menuBar.add(mntmHelp);
		mntmHelp.addActionListener(this);
		
		mntmAbout = new WebMenuItem(ABOUT);
		menuBar.add(mntmAbout);	
		
		mntmAbout.addActionListener(this);

		setEnableProfileAndTracer(false);
	}
		
	public void finishedTrace(String xmlName) {
		JOptionPane.setDefaultLocale(new Locale("en"));
		
		String xmlPath = FileUtilities.CURRENT_DIR + FileUtilities.SEPARATOR + xmlName;	
		
		int selected = JOptionPane.showOptionDialog(null, Message.FINISHED, "", JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Ok", "Cancel"}, "Ok");
		
		if(selected==0) {
			presenter.open(xmlPath);
		}
		
	}
	
	public void showConsole(){
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT_CONSOLE);
		console.setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public void hideConsole(){
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT_NO_CONSOLE);
		console.setVisible(false);
		setLocationRelativeTo(null);
	}

	public void errorNameXml(){
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(),Message.ERROR_EXTENSION);
	}

	@SuppressWarnings("unchecked")
    public void loadClasses(List<String> classes) {
	
		nameClass.removeAllItems();
		
		for (int i=0;i<classes.size();i++){
			nameClass.addItem(classes.get(i));
		}
		
		if (nameClass.getItemCount()>0){
			trace.setEnabled(true);
			profile.setEnabled(true); 
			
		}
		
	}
	
	public String getPath() {
		String path = "";
		if (!chooser.getSelectedFiles().isEmpty()){
			path = chooser.getSelectedFiles().get(0).toString();
		}		
		return path;
	}

	public String getMainClass() {
		return (String)nameClass.getSelectedItem();
	}

	public String getNameXml(boolean trace) {
		String nameXml = (String) nameClass.getSelectedItem();
		
		if(nameXml.contains(".")){
			String aux = nameXml.substring(nameXml.lastIndexOf(".")+1, nameXml.length());
		
			if (aux.equals("jar")||aux.equals("class"))
				nameXml = nameXml.substring(0,nameXml.lastIndexOf("."));
		
			if (nameXml.contains("."))
				nameXml = nameXml.substring(nameXml.lastIndexOf(".")+1, nameXml.length());
		}
			
		if(trace)
			nameXml += "_trace";
		else
			nameXml += "_profile";
		 
		
		if (existFileXml(nameXml + FileUtilities.EXTENSION_XML)){
			int i=1;	
			while(existFileXml(nameXml + "_" + i + FileUtilities.EXTENSION_XML)){
				i=i+1;
			}
			nameXml += "_" + Integer.toString(i);
		}
		
		nameXml += FileUtilities.EXTENSION_XML;
		
		return nameXml;
	}

	public void enableMainClassCombo() {
		nameClass.setEnabled(true);		
	}

	public void setController(TracerController controller){
		presenter = controller;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source.equals(addArgument)){
			clickedEditOnArguments();
		} else if (source.equals(exit)){
			clickedOnExit();
		} else if (source.equals(trace)){
			clickedOnTrace();			
		} else if (source.equals(profile)){
			clickedOnProfile();
		} else if (source.equals(mntmExit)){
			clickedOnExit();
		} else if (source.equals(mntmLoadProfile)){
			setEnableProfileAndTracer(false);
			clickedOnLoadProfile();
		} else if (source.equals(mntmLoadTrace)){
			clickedOnLoadTrace();
		} else if (source.equals(mntmProfile)){
			setEnableProfileAndTracer(false);
			clickedOnProfile();
		} else if (source.equals(mntmTrace)){
			setEnableProfileAndTracer(false);
			clickedOnTrace();
		} else if (source.equals(nameClass)){
			dataChangedComboBox();
		} else if (source.equals(mntmSettings)){
			clickedOnSettings();
		} else if (source.equals(mntmHelp)){
			clickedOnHelp();
		} else if (source.equals(mntmAbout)){
			clickedOnAbout();
		}
	}

	private void clickedOnAbout() {
		presenter.clickedOnAbout();
	}

	private void clickedOnHelp() {
		//TODO : SHOW HELP
	}
	
	private void clickedOnSettings() {
		presenter.clickOnSettings();
	}

	private void dataChangedComboBox() {
		if (nameClass.getItemAt(nameClass.getSelectedIndex()) != null){
			setEnableProfileAndTracer(true);
		} else {
			setEnableProfileAndTracer(false);
		}
	}

	private void clickedOnLoadProfile() {
		presenter.clickedOnLoadProfile();
	}

	private void clickedOnLoadTrace() {
		presenter.clickedOnLoadTrace();
	}

	private void clickedOnProfile() {
			presenter.clickedOnProfile();
	}

	private void clickedOnTrace(){
		presenter.clickedOnTrace();
	}

	private void clickedOnExit(){
		presenter.clickedExit();		
	}

	private void clickedEditOnArguments(){
		presenter.editArguments();
	}

	public void selectionChanged(List<File> arg0) {
		
		if (!arg0.isEmpty()){
			String file_selected = arg0.get(0).toString();
			presenter.selectedPath(file_selected);
		} else {
			nameClass.setEnabled(false);
			nameClass.removeAllItems();
		}
	}

	public void setEnableProfileAndTracer(boolean enable) {
		mntmProfile.setEnabled(enable);
		mntmTrace.setEnabled(enable);
		trace.setEnabled(enable);
		profile.setEnabled(enable);
	}

	public void setEnableTracer(boolean enable) {
		mntmTrace.setEnabled(enable);
		trace.setEnabled(enable);
	}

	public void setEnableProfile(boolean enable) {
		mntmProfile.setEnabled(enable);
		profile.setEnabled(enable);
	}
	
	private boolean existFileXml(String name){
		int i=0;
	 	boolean found=false;
	 	File files = new File(FileUtilities.CURRENT_DIR);
	 	String[] classes=files.list();
		
	 	while(i<classes.length && !found){
	 			
	 		if(classes[i].equals(name)) 
	 			found=true;
	 		
	 		i++;
	 	} 		
	 	
	 	return found;	 		
	}

}