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
import com.tracer.console.view.ConsoleView;
import com.tracer.presenter.TracerPresenterInterface;

@SuppressWarnings("serial")
public class TracerView extends JFrame implements ActionListener, FilesSelectionListener,TracerViewInterface{

	private static final String ADD_ARGUMENTS = "Add arguments";
	private static final String ABOUT = "About";
	private static final String SETTINGS = "Settings";
	private static final String EXIT = "Exit";
	private static final String LOAD_TRACE = "Load Trace";
	private static final String LOAD_PROFILE = "Load Profile";
	private static final String PROFILE = "Profile";
	private static final String FILE = "File";
	private static final String TRACE = "Trace";

	private static final int DEFAULT_WINDOW_WIDTH = 800;
	private static final int DEFAULT_WINDOW_HEIGHT = 500;

	private static String LABEL_PATH = "Select a directory";
	private static String HELP_PATH_TOOLTIP = "Select a directory where all .class or .jar are located.";
	private static String LABEL_NAME_CLASS = "Select a class / jar";

	private TracerPresenterInterface presenter;

	private WebButton trace,btnArguments,profile;
	private WebComboBox nameClass;
	private WebFileChooserField chooser;
	private WebLabel labelPath,labelNameClass;
	private WebLabel helpNameClass,helpPath;

	private WebMenuItem mntmLoadProfile;
	private WebMenuItem mntmLoadTrace;
	private WebMenuItem mntmExit;
	private WebMenuItem mntmTrace;
	private WebMenuItem mntmProfile;
	private WebMenuItem mntmSettings;
	private WebMenuItem mntmAbout;
	
	private ConsoleView console;

	public TracerView(ConsoleView console) {

		this.console = console;
		ImageLoader imageLoader = ImageLoader.getInstance();

		setTitle("Java Tracer");
		setSize(DEFAULT_WINDOW_WIDTH,DEFAULT_WINDOW_HEIGHT); 
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(imageLoader.getApplicationIcon().getImage());

		chooser = new WebFileChooserField(this);
		chooser.setBounds(245, 60, 450, 30);

		chooser.setTitle("Select a directory");
		chooser.setCurrentDirectory(FileUtilities.CURRENT_DIR);
		FileNameExtensionFilter Filter = new FileNameExtensionFilter("Only Directories","Folders");
		chooser.setFileFilter(Filter);
		chooser.setMultiSelectionEnabled(false);
		chooser.setShowFileShortName(false);	
		chooser.addSelectedFilesListener(this);

		helpPath = new WebLabel();
		helpPath.setBounds(707, 62, 24, 24);
		helpPath.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpPath.setToolTipText(HELP_PATH_TOOLTIP);

		btnArguments = new WebButton(ADD_ARGUMENTS);
		btnArguments.setText("Arguments");
		btnArguments.setBounds(556, 151, 141, 28);
		btnArguments.addActionListener(this);

		nameClass = new WebComboBox();
		nameClass.setBounds(245, 105, 450, 30);
		nameClass.setBackground(Color.white); 
		nameClass.setEditable(false);
		nameClass.setEnabled(false); 
		nameClass.addActionListener(this);

		helpNameClass = new WebLabel();
		helpNameClass.setBounds(707, 111, 24, 24);
		helpNameClass.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpNameClass.setToolTipText(LABEL_NAME_CLASS);

		trace = new WebButton(TRACE);
		trace.setBounds(400, 150, 120, 30);
		trace.setLayout(new GridLayout(1,1));
		trace.setBackground(Color.white);  
		trace.setEnabled(false); 
		trace.addActionListener(this);

		profile = new WebButton(PROFILE);
		profile.setBounds(245, 150, 120, 30);
		profile.setLayout(new GridLayout(1,1));
		profile.setBackground(Color.white);  
		profile.setEnabled(false); 
		profile.addActionListener(this);



		labelPath = new WebLabel(LABEL_PATH);
		labelPath.setBounds(72, 62, 168, 30);
		labelPath.setBackground(Color.white);
		getContentPane().setLayout(null);


		labelNameClass = new WebLabel(LABEL_NAME_CLASS);
		labelNameClass.setBounds(72, 105, 168, 30);
		labelNameClass.setBackground(Color.white); 

		console.setBounds(10,200,DEFAULT_WINDOW_WIDTH-30,DEFAULT_WINDOW_HEIGHT-250);

		getContentPane().add(labelNameClass);
		getContentPane().add(labelPath);
		getContentPane().add(chooser);
		getContentPane().add(trace);
		getContentPane().add(nameClass);
		getContentPane().add(helpPath);
		getContentPane().add(helpNameClass);
		getContentPane().add(btnArguments);
		getContentPane().add(profile);
		getContentPane().add(console);

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
			int selected = JOptionPane.showOptionDialog(null,nameXml + " " + Message.ERROR_EXIST, "", JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Ok", "No"}, "Ok");
			if (selected == 0) {
				nameXml += FileUtilities.EXTENSION_XML;
			} else if(selected == 1) {
				int i=1;	
				while(existFileXml(nameXml + "(" + i +")"+ FileUtilities.EXTENSION_XML)){
					i=i+1;
				}
				nameXml += "(" + Integer.toString(i)+")" +FileUtilities.EXTENSION_XML;
			}
			
		}else nameXml += FileUtilities.EXTENSION_XML;

		return nameXml;
	}

	public void enableMainClassCombo() {
		nameClass.setEnabled(true);		
	}

	public void setController(TracerPresenterInterface presenter){
		this.presenter = presenter;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source.equals(btnArguments)){
			clickedEditOnArguments();
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
			clickedOnTrace();
		} else if (source.equals(nameClass)){
			dataChangedComboBox();
		} else if (source.equals(mntmSettings)){
			clickedOnSettings();
		} else if (source.equals(mntmAbout)){
			clickedOnAbout();
		}
	}

	private void clickedOnAbout() {
		presenter.clickedOnAbout();
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
		setEnableProfileAndTracer(false);
		presenter.clickedOnProfile();
	}

	private void clickedOnTrace(){
		setEnableProfileAndTracer(false);
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
			
		if(arg0.get(0).isFile()&& FileUtilities.getExtension(arg0.get(0)).equals("jar")){
			if(presenter.isExecutableJar(arg0.get(0))){
				String file_selected = arg0.get(0).toString();
				presenter.selectedPath(file_selected);
				
			}else{
				nameClass.removeAllItems();
				setEnableProfileAndTracer(false);
				JOptionPane.showMessageDialog(null,Message.JARNOTEXECUTABLE);
				}
			
		}else{
			String file_selected = arg0.get(0).toString();
			presenter.selectedPath(file_selected);
			
		}
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
		console.setPlayEnabled(enable);
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

	public void consoleMinimize() {
		setSize(DEFAULT_WINDOW_WIDTH,DEFAULT_WINDOW_HEIGHT-(console.getDefaultHeight() - console.getCurrentHeight()));
	}

	public void consoleMaximize() {
		setSize(DEFAULT_WINDOW_WIDTH,DEFAULT_WINDOW_HEIGHT);
	}
	
	public JFrame getView() {
		return this;
	}
}