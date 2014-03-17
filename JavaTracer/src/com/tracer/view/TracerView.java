package com.tracer.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alee.extended.filechooser.FilesSelectionListener;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.text.WebTextField;
import com.general.model.FileUtilities;
import com.general.resources.ImageLoader;
import com.general.view.WebFileChooserField;
import com.inspector.controller.InspectorController;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import com.tracer.controller.TracerController;

@SuppressWarnings("serial")
public class TracerView extends JFrame implements ActionListener, FilesSelectionListener{
	
	private static final String ADD_ARGUMENTS = "Add arguments";
	private static final String NAME_OF_THE_TRACE_PROFILE_FILE = "Name of the trace/profile file";
	private static final String ABOUT = "About";
	private static final String HELP = "Help";
	private static final String SETTINGS = "Settings";
	private static final String EXIT = "Exit";
	private static final String LOAD_TRACE = "Load Trace";
	private static final String LOAD_PROFILE = "Load Profile";
	private static final String PROFILE = "Profile";
	private static final String FILE = "File";
	private static final String TRACE = "Trace";
	private static final String XML = "xml";
	
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT_NO_CONSOLE = 285;
	private static final int WINDOW_HEIGHT_CONSOLE = 600;
	
	private static String LABEL_PATH = "Select a directory";
	private static String HELP_PATH_TOOLTIP = "Select a directory where all .class are located.";
	private static String LABEL_NAME_CLASS = "Select a class";
	private static String XML_FILE_TOOLTIP = "Write the name of the file without any extensions";
	
	private TracerController presenter;
	private InspectorController inspectorController;
	
	private WebButton trace,exit,addArgument,profile;
	private WebTextField nameXml;
	private WebComboBox nameClass;
	private WebFileChooserField chooser;
	private WebLabel labelPath,labelNameClass,labelXml;
	private WebLabel helpXmlFile,helpNameClass,helpPath;
	private JComponent console;
	
	private WebMenuItem mntmLoadProfile;
	private WebMenuItem mntmLoadTrace;
	private WebMenuItem mntmExit;
	private WebMenuItem mntmTrace;
	private WebMenuItem mntmProfile;
	private WebMenuItem mntmSettings;
	private WebMenuItem mntmHelp;
	private WebMenuItem mntmAbout;
	
	private String  onlyXmlName;
	private boolean haveExtension;
	public TracerView(JComponent console) {
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		setTitle("Java Tracer");
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT_NO_CONSOLE);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(imageLoader.getApplicationIcon().getImage());
		
		this.console = console;
		
		chooser = new WebFileChooserField();
		chooser.setTitle("Select a directory or jar file");
		String currentDirectory = System.getProperty("user.dir");
		chooser.setCurrentDirectory(currentDirectory);
		FileNameExtensionFilter jarFilter = new FileNameExtensionFilter("Jar Files","jar");
		chooser.setFileFilter(jarFilter);
		chooser.setMultiSelectionEnabled(false);
		chooser.setShowFileShortName(false);	
		chooser.addSelectedFilesListener(this);
		
		helpPath = new WebLabel();
		helpPath.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpPath.setToolTipText(HELP_PATH_TOOLTIP);
		
		addArgument = new WebButton(ADD_ARGUMENTS);
		addArgument.addActionListener(this);
				
		nameClass = new WebComboBox();
		nameClass.setBackground(Color.white); 
		nameClass.setEditable(false);
		nameClass.setEnabled(false); 
		nameClass.addActionListener(this);
		
		helpNameClass = new WebLabel();
		helpNameClass.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpNameClass.setToolTipText(LABEL_NAME_CLASS);
		
		nameXml = new WebTextField();
		nameXml.setEnabled(false); 
			 
		helpXmlFile = new WebLabel();
		helpXmlFile.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpXmlFile.setToolTipText(XML_FILE_TOOLTIP);
		
		exit =new WebButton(EXIT);
		exit.setBackground(Color.white); 
		exit.addActionListener(this);
		
		trace = new WebButton(TRACE);
		trace.setLayout(new GridLayout(1,1));
		trace.setBackground(Color.white);  
		trace.setEnabled(false); 
		trace.addActionListener(this);
	
		profile = new WebButton(PROFILE);
		profile.setLayout(new GridLayout(1,1));
		profile.setBackground(Color.white);  
		profile.setEnabled(false); 
		profile.addActionListener(this);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("5px"),
				ColumnSpec.decode("5dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("10dlu"),
				ColumnSpec.decode("120px"),
				ColumnSpec.decode("50px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("50px"),
				ColumnSpec.decode("130px"),
				ColumnSpec.decode("20px"),
				ColumnSpec.decode("25px"),
				ColumnSpec.decode("35px"),
				ColumnSpec.decode("120px"),
				ColumnSpec.decode("10px"),},
			new RowSpec[] {
				RowSpec.decode("26px"),
				RowSpec.decode("34px"),
				RowSpec.decode("30px"),
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("30px"),
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("30px"),
				RowSpec.decode("6dlu"),
				RowSpec.decode("20dlu"),
				RowSpec.decode("40px"),
				RowSpec.decode("20dlu:grow"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("10px"),}));
		
		labelPath = new WebLabel(LABEL_PATH);
		labelPath.setBackground(Color.white);
		getContentPane().add(labelPath, "3, 3, left, fill");
		getContentPane().add(chooser, "5, 3, 5, 1, fill, fill");
		
		labelNameClass = new WebLabel(LABEL_NAME_CLASS);
		labelNameClass.setBackground(Color.white); 
		getContentPane().add(labelNameClass, "3, 5, left, fill");
		
		labelXml = new WebLabel(NAME_OF_THE_TRACE_PROFILE_FILE);
		getContentPane().add(labelXml, "3, 7, left, fill");
		getContentPane().add(trace, "7, 9, fill, fill");
		getContentPane().add(exit, "9, 9, fill, fill");
		getContentPane().add(nameClass, "5, 5, 5, 1, fill, fill");
		getContentPane().add(helpPath, "11, 3, center, center");
		getContentPane().add(helpNameClass, "11, 5, center, center");
		getContentPane().add(nameXml, "5, 7, 5, 1, fill, fill");
		getContentPane().add(helpXmlFile, "11, 7, center, center");
		getContentPane().add(addArgument, "13, 5, center, center");
		getContentPane().add(profile, "5, 9, fill, fill");
		getContentPane().add(console, "3, 11, 11, 2, fill, fill");
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, "1, 1, 14, 1, fill, top");
		
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
		
		inspectorController = new InspectorController();
		setEnableProfileAndTracer(false);
	}
		
	public void finishedTrace() {
		JOptionPane.setDefaultLocale(new Locale("en"));
		String curDir = System.getProperty("user.dir");
		String s= File.separator;
		String xmlName = getNameXml();
		
		String xmlPath = curDir+s+xmlName+FileUtilities.EXTENSION_XML;	
		
		int selected = JOptionPane.showOptionDialog(null, Message.FINISHED, "", JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Ok", "Cancel"}, "Ok");
		
		if(selected==0) {
			inspectorController.open();
			inspectorController.setFromTracer(true);
			inspectorController.showTree(xmlPath);
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
			nameXml.setEnabled(true);
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

	public String getNameXml() {
		String nameXml = "";
		if (!haveExtension) {
			nameXml = this. nameXml.getText();
			if (nameXml.equals(""))
				nameXml = "default";
		} else nameXml = onlyXmlName;
		
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
			String xmlName = nameXml.getText();
			boolean isXmlExtension = FileUtilities.isExtension(xmlName, XML);
			if (isXmlExtension) {
				haveExtension = true;
				onlyXmlName = FileUtilities.getOnlyName(xmlName);
			}else haveExtension=false;
			
			if(existFileXml(getNameXml()+FileUtilities.EXTENSION_XML)){									
				 int seleccion = JOptionPane.showOptionDialog( null,getNameXml() +" already exists,"+" are you sure you want to overwrite the current file?",
						 					"Overwrite current file",	JOptionPane.YES_NO_CANCEL_OPTION,    JOptionPane.QUESTION_MESSAGE, null, null, null);				
				 if(seleccion==0) {
					 setEnableProfileAndTracer(false);
					 clickedOnTrace();
				 }				 											    
			}else {
				 setEnableProfileAndTracer(false);
				 clickedOnTrace();
			}
			
		} else if (source.equals(profile)){
			
		
			String xmlName = nameXml.getText()+"Profiler";
			boolean isXmlExtension = FileUtilities.isExtension(xmlName, XML);
			if (isXmlExtension) {
				haveExtension = true;
				onlyXmlName = FileUtilities.getOnlyName(xmlName);
			}else haveExtension=false;
			
			if(existFileXml(nameXml+FileUtilities.EXTENSION_XML)){									
				 int seleccion = JOptionPane.showOptionDialog( null,xmlName +" already exists,"+" are you sure you want to overwrite the current file?",
						 					"Overwrite current file",	JOptionPane.YES_NO_CANCEL_OPTION,    JOptionPane.QUESTION_MESSAGE, null, null, null);				
				 if(seleccion==0) {
					 setEnableProfileAndTracer(false);
					 clickedOnProfile();
				 }				 											    
			}else {
				 
				setEnableProfileAndTracer(false);
				profile.setEnabled(false); 
				clickedOnProfile();
					}
		
		} else if (source.equals(mntmExit)){
			clickedOnExit();
		} else if (source.equals(mntmLoadProfile)){
			setEnableProfileAndTracer(false);
			clickedOnLoadProfile();
		} else if (source.equals(mntmLoadTrace)){
			clickedOnLoadTracer();
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
		AboutDialog aboutDialog = new AboutDialog();
		aboutDialog.setVisible(true);
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
		presenter.clickedOnProfile();
	}

	private void clickedOnLoadTracer() {
		presenter.clickedOnTrace();
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
	 	File files = new File("./");
	 	String[] classes=files.list();
		
	 	while(i<classes.length && !found){
	 			
	 	if(classes[i].equals(name)) found=true;
	 		i++;
	 		
	 	} 		
	 	return found;	 		
	}

}