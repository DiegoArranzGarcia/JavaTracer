package com.tracer.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.general.model.FileUtilities;
import com.general.resources.ImageLoader;
import com.inspector.controller.InspectorController;
import com.tracer.controller.TracerController;

@SuppressWarnings("serial")
public class TracerView extends JFrame implements ActionListener{
	
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
	
	private static int FIRST_ROW = 60;
	private static int SECOND_ROW = 120;
	private static int THIRD_ROW = 170;
	private static int FIRST_COL= 35;
	private static int SECOND_COL = 320;
	private static int THIRD_COL = 790;
	private static int EXAMINE_COL = 220;
	private static int CANCEL_TRACER_ROW = 220;
	private static int TRACER_COL = 340;
	private static int DISTANCE_BUTTONS = 150;
	
	private static String LABEL_PATH = "Select a directory";
	private static String HELP_PATH_TOOLTIP = "Select a directory where all .class are located.";
	private static String LABEL_NAME_CLASS = "Select a class";
	private static String XML_FILE_TOOLTIP = "Write the name of the file without any extensions";
	
	private JButton trace,examine,exit,addArgument,profile;
	private TextField path,nameXml;
	private JComboBox<String> nameClass;
	private JFileChooser chooser;
	private JLabel labelPath,labelNameClass,labelXml;
	private JLabel helpXmlFile,helpNameClass,helpPath;
	private TracerController presenter;
	private InspectorController inspectorController;
	private JMenuItem mntmLoadProfile;
	private JMenuItem mntmLoadTrace;
	private JMenuItem mntmExit;
	private JMenuItem mntmTrace;
	private JMenuItem mntmProfile;
	private JMenuItem mntmSettings;
	private JMenuItem mntmHelp;
	private JMenuItem mntmAbout;
		
	public TracerView() {
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		setTitle("Java Tracer");
		setSize(1050, 300);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(imageLoader.getApplicationIcon().getImage());
		
		labelPath = new JLabel(LABEL_PATH);
		labelPath.setBounds(new Rectangle(540,30));
		labelPath.setLocation(FIRST_COL, FIRST_ROW); 
		labelPath.setBackground(Color.white);
		
		path = new TextField();
		path.setBounds(new Rectangle(450,30));
		path.setLocation(SECOND_COL, FIRST_ROW); 
		path.setEditable(false);
		path.setBackground(Color.white);	
		
		helpPath = new JLabel();
		helpPath.setBounds(new Rectangle(25,25));
		helpPath.setLocation(THIRD_COL, FIRST_ROW+2); 
		helpPath.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpPath.setToolTipText(HELP_PATH_TOOLTIP);
		
		addArgument = new JButton(ADD_ARGUMENTS);
		addArgument.setBounds(new Rectangle(140,25));
		addArgument.setLocation(850, SECOND_ROW+2); 
		addArgument.addActionListener(this);
		
		labelNameClass = new JLabel(LABEL_NAME_CLASS);
		labelNameClass.setBounds(new Rectangle(540,30));
		labelNameClass.setLocation(FIRST_COL, SECOND_ROW); 
		labelNameClass.setBackground(Color.white); 
				
		nameClass = new JComboBox<String>();
		nameClass.setBounds(new Rectangle(450,30));
		nameClass.setLocation(SECOND_COL, SECOND_ROW); 
		nameClass.setBackground(Color.white); 
		nameClass.setEditable(false);
		nameClass.setEnabled(false); 
		nameClass.addActionListener(this);
		
		helpNameClass = new JLabel();
		helpNameClass.setBounds(new Rectangle(25,25));
		helpNameClass.setLocation(THIRD_COL, SECOND_ROW); 
		helpNameClass.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpNameClass.setToolTipText(LABEL_NAME_CLASS);
			
		labelXml = new JLabel(NAME_OF_THE_TRACE_PROFILE_FILE);
		labelXml.setBounds(new Rectangle(200,30)); 
		labelXml.setLocation(FIRST_COL, THIRD_ROW); 
		
		nameXml = new TextField();
		nameXml.setBounds(new Rectangle(450,30));
		nameXml.setLocation(SECOND_COL,THIRD_ROW);
		nameXml.setEnabled(false); 
			 
		helpXmlFile = new JLabel();
		helpXmlFile.setBounds(new Rectangle(25,25));
		helpXmlFile.setLocation(THIRD_COL, THIRD_ROW); 
		helpXmlFile.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpXmlFile.setToolTipText(XML_FILE_TOOLTIP);
		
		exit =new JButton(EXIT);
		exit.setBounds(new Rectangle(690, 206, 100, 40));
		exit.setLocation(TRACER_COL+2*DISTANCE_BUTTONS, CANCEL_TRACER_ROW); 
		exit.setBackground(Color.white); 
		exit.addActionListener(this);
		
		trace = new JButton(TRACE);
		trace.setLayout(new GridLayout(1,1)); 
		trace.setBounds(new Rectangle(100,40));
		trace.setLocation(TRACER_COL+DISTANCE_BUTTONS, CANCEL_TRACER_ROW); 
		trace.setBackground(Color.white);  
		trace.setEnabled(false); 
		trace.addActionListener(this);
	
		profile = new JButton(PROFILE);
		profile.setLayout(new GridLayout(1,1)); 
		profile.setBounds(new Rectangle(100,40));
		profile.setLocation(TRACER_COL, CANCEL_TRACER_ROW); 
		profile.setBackground(Color.white);  
		profile.setEnabled(false); 
		profile.addActionListener(this);
		
		examine = new JButton("Examine"); 
		examine.setLayout(new GridLayout(1,1)); 
		examine.setBounds(new Rectangle(90,30));
		examine.setLocation(EXAMINE_COL, FIRST_ROW); 
		examine.setBackground(Color.LIGHT_GRAY); 
		examine.addActionListener(this);
		
		getContentPane().setLayout(null); 
		getContentPane().add(path);
		getContentPane().add(trace);
		getContentPane().add(examine);
		getContentPane().add(exit);
		getContentPane().add(labelPath);
		getContentPane().add(nameClass);
		getContentPane().add(labelNameClass);
		getContentPane().add(helpPath);
		getContentPane().add(helpNameClass);
		getContentPane().add(labelXml);
		getContentPane().add(nameXml);
		getContentPane().add(helpXmlFile);
		getContentPane().add(addArgument);
		getContentPane().add(profile);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1044, 26);
		getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu(FILE);
		mnFile.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnFile);
		
		mntmTrace = new JMenuItem(TRACE);
		mnFile.add(mntmTrace);
		mntmTrace.addActionListener(this);
		
		mntmProfile = new JMenuItem(PROFILE);
		mnFile.add(mntmProfile);
		mntmProfile.addActionListener(this);
		
		mntmLoadProfile = new JMenuItem(LOAD_PROFILE);
		mnFile.add(mntmLoadProfile);
		mntmLoadProfile.addActionListener(this);
		
		mntmLoadTrace = new JMenuItem(LOAD_TRACE);
		mnFile.add(mntmLoadTrace);
		mntmLoadTrace.addActionListener(this);
		
		mntmExit = new JMenuItem(EXIT);
		mnFile.add(mntmExit);
		mntmExit.addActionListener(this);
		
		mntmSettings = new JMenuItem(SETTINGS);
		menuBar.add(mntmSettings);
		mntmSettings.addActionListener(this);
		
		mntmHelp = new JMenuItem(HELP);
		menuBar.add(mntmHelp);
		mntmHelp.addActionListener(this);
		
		mntmAbout = new JMenuItem(ABOUT);
		menuBar.add(mntmAbout);
		mntmAbout.addActionListener(this);
		
		inspectorController = new InspectorController();
		setEnableProfileAndTracer(false);
	}
	
	public void finishedTrace() {
		JOptionPane.setDefaultLocale(new Locale("en"));
		String curDir = System.getProperty("user.dir");
		String s= File.separator;
		String xmlPath = curDir+s+getNameXml()+FileUtilities.EXTENSION_XML;
		
		int selected = JOptionPane.showOptionDialog(null, Message.FINISHED, "", JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Ok", "Cancel"}, "Ok");
		
		if(selected==0) {
			inspectorController.open();
			inspectorController.setFromTracer(true);
			inspectorController.showTree(xmlPath);
		}
		
	}
	
	
	public void errorNameXml(){
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(),Message.ERROR_EXTENSION);
	}

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
		return path.getText();
	}

	public String getMainClass() {
		return (String)nameClass.getSelectedItem();
	}

	public String getNameXml() {
		String nameXml = this. nameXml.getText();
		if (nameXml.equals(""))
			nameXml = "default";
		return nameXml;
	}

	public void enableMainClassCombo() {
		nameClass.setEnabled(true);		
	}

	public void setController(TracerController controller){
		this.presenter = controller;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source.equals(examine)){
			clickedOnExamine();
		} else if (source.equals(addArgument)){
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
			clickedOnLoadProfile();
		} else if (source.equals(mntmLoadTrace)){
			clickedOnLoadTracer();
		} else if (source.equals(mntmProfile)){
			clickedOnProfile();
		} else if (source.equals(mntmTrace)){
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

	private void clickedOnExamine(){
		chooser = new JFileChooser();
		
		//Title window
		chooser.setDialogTitle("Select directoty or a jar file");
		String currentDirectory = System.getProperty("user.dir");
		chooser.setCurrentDirectory(new File(currentDirectory));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		FileNameExtensionFilter filtroXml = new FileNameExtensionFilter("Jar Files","jar");
		chooser.setFileFilter(filtroXml);
		//return directory file
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {		
				String file_selected= chooser.getSelectedFile().toString();
				path.setText(file_selected);
			presenter.selectedPath(file_selected);
		} else {
			chooser.cancelSelection();
		}
	}

	public void setEnableProfileAndTracer(boolean enable) {
		mntmProfile.setEnabled(enable);
		mntmTrace.setEnabled(enable);
		trace.setEnabled(enable);
		profile.setEnabled(enable);
	}

}