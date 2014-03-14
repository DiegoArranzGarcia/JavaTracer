package com.tracer.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.general.model.FileUtilities;
import com.general.resources.ImageLoader;
import com.inspector.controller.InspectorController;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
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
	
	private static final int WINDOW_WIDTH = 1050;
	private static final int WINDOW_HEIGHT_NO_CONSOLE = 300;
	private static final int WINDOW_HEIGHT_CONSOLE = 600;
	
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
	private JScrollPane scrollPane;
		
	public TracerView(JComponent console) {
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		setTitle("Java Tracer");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT_NO_CONSOLE);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(imageLoader.getApplicationIcon().getImage());
		
		labelPath = new JLabel(LABEL_PATH);
		labelPath.setBackground(Color.white);
		
		path = new TextField();
		path.setEditable(false);
		path.setBackground(Color.white);	
		
		helpPath = new JLabel();
		helpPath.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpPath.setToolTipText(HELP_PATH_TOOLTIP);
		
		addArgument = new JButton(ADD_ARGUMENTS);
		addArgument.addActionListener(this);
		
		labelNameClass = new JLabel(LABEL_NAME_CLASS);
		labelNameClass.setBackground(Color.white); 
				
		nameClass = new JComboBox<String>();
		nameClass.setBackground(Color.white); 
		nameClass.setEditable(false);
		nameClass.setEnabled(false); 
		nameClass.addActionListener(this);
		
		helpNameClass = new JLabel();
		helpNameClass.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpNameClass.setToolTipText(LABEL_NAME_CLASS);
			
		labelXml = new JLabel(NAME_OF_THE_TRACE_PROFILE_FILE);
		
		nameXml = new TextField();
		nameXml.setEnabled(false); 
			 
		helpXmlFile = new JLabel();
		helpXmlFile.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpXmlFile.setToolTipText(XML_FILE_TOOLTIP);
		
		exit =new JButton(EXIT);
		exit.setBackground(Color.white); 
		exit.addActionListener(this);
		
		trace = new JButton(TRACE);
		trace.setLayout(new GridLayout(1,1));
		trace.setBackground(Color.white);  
		trace.setEnabled(false); 
		trace.addActionListener(this);
	
		profile = new JButton(PROFILE);
		profile.setLayout(new GridLayout(1,1));
		profile.setBackground(Color.white);  
		profile.setEnabled(false); 
		profile.addActionListener(this);
		
		examine = new JButton("Examine"); 
		examine.setLayout(new GridLayout(1,1));
		examine.setBackground(Color.LIGHT_GRAY); 
		examine.addActionListener(this);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("310px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("120px"),
				ColumnSpec.decode("50px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("50px"),
				ColumnSpec.decode("130px"),
				ColumnSpec.decode("20px"),
				ColumnSpec.decode("25px"),
				ColumnSpec.decode("35px"),
				ColumnSpec.decode("194px"),},
			new RowSpec[] {
				RowSpec.decode("26px"),
				RowSpec.decode("34px"),
				RowSpec.decode("30px"),
				RowSpec.decode("30px"),
				RowSpec.decode("30px"),
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("30px"),
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("40px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		getContentPane().add(path, "3, 3, 5, 1, fill, fill");
		getContentPane().add(trace, "5, 9, fill, fill");
		getContentPane().add(examine, "1, 3, right, fill");
		getContentPane().add(exit, "7, 9, fill, fill");
		getContentPane().add(labelPath, "1, 3, center, fill");
		getContentPane().add(nameClass, "3, 5, 5, 1, fill, fill");
		getContentPane().add(labelNameClass, "1, 5, center, fill");
		getContentPane().add(helpPath, "9, 3, fill, center");
		getContentPane().add(helpNameClass, "9, 5, fill, top");
		getContentPane().add(labelXml, "1, 7, center, fill");
		getContentPane().add(nameXml, "3, 7, 5, 1, fill, fill");
		getContentPane().add(helpXmlFile, "9, 7, fill, top");
		getContentPane().add(addArgument, "11, 5, left, center");
		getContentPane().add(profile, "3, 9, fill, fill");
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, "1, 1, 11, 1, fill, top");
		
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
		
		scrollPane = new JScrollPane();
		scrollPane.setVisible(false);
		getContentPane().add(scrollPane, "1, 13, 11, 1, fill, fill");
		scrollPane.setViewportView(console);
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
	
	public void showConsole(){
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT_CONSOLE);
		scrollPane.setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public void hideConsole(){
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT_NO_CONSOLE);
		scrollPane.setVisible(false);
		setLocationRelativeTo(null);
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
		presenter = controller;
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