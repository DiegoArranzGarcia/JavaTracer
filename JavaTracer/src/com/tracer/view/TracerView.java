package com.tracer.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.general.imageresources.ImageLoader;
import com.tracer.arguments.view.ArgumentsView;
import com.tracer.controller.TracerController;

@SuppressWarnings("serial")
public class TracerView extends JFrame {
	
	private static int FIRST_ROW = 60;
	private static int SECOND_ROW = 120;
	private static int THIRD_ROW = 170;
	private static int FIRST_COL= 35;
	private static int SECOND_COL = 320;
	private static int THIRD_COL = 790;
	private static int EXAMINE_COL = 220;
	private static int CANCEL_TRACER_ROW = 220;
	private static int CANCEL_COL = 480;
	private static int TRACER_COL = 300;
	private static int LABELS_SIZE = 14;
	private static int DISTANCE_BUTTONS = 150;
	
	private static String LABEL_PATH = "Select a directory";
	private static String HELP_PATH_TOOLTIP = "Select a directory where all .class are located.";
	private static String LABEL_NAME_CLASS = "Select a class";
	private static String LABEL_XML = "Name of the trace file";
	private static String XML_FILE_TOOLTIP = "Write the name of the file without any extensions";
	
	private JButton tracer,examine,back,helpPath,helpNameClass,helpXmlFile,addArgument,profiling;
	private TextField path,nameXml;
	private JComboBox<String> nameClass;
	private JFileChooser chooser;
	private JLabel labelPath,labelNameClass,labelXml;
	private TracerController controller;
	private Container contentPane;
		
	public TracerView() {
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		setTitle("Java Tracer");
		setSize(1050, 300);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = imageLoader.getApplicationIcon().getImage();
		setIconImage(icon);
		
		labelPath = new JLabel(LABEL_PATH);
		labelPath.setBounds(new Rectangle(540,30));
		labelPath.setLocation(FIRST_COL, FIRST_ROW); 
		labelPath.setBackground(Color.white); 
		labelPath.setFont(new Font("Comic Sans MS",Font.ROMAN_BASELINE, LABELS_SIZE)); 
		
		path = new TextField();
		path.setBounds(new Rectangle(450,30));
		path.setLocation(SECOND_COL, FIRST_ROW); 
		path.setEditable(false);
		path.setBackground(Color.white);	
		
		helpPath = new JButton();
		helpPath.setBounds(new Rectangle(25,25));
		helpPath.setLocation(THIRD_COL, FIRST_ROW+2); 
		helpPath.setIcon(imageLoader.getHelpIcon());
		helpPath.setToolTipText(HELP_PATH_TOOLTIP);
		
		addArgument = new JButton("Add arguments");
		addArgument.setBounds(new Rectangle(140,25));
		addArgument.setLocation(850, SECOND_ROW+2); 
		addArgument.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				controller.clickedEditArguments();
			}
		});
		
		labelNameClass = new JLabel(LABEL_NAME_CLASS);
		labelNameClass.setBounds(new Rectangle(540,30));
		labelNameClass.setLocation(FIRST_COL, SECOND_ROW); 
		labelNameClass.setBackground(Color.white); 
		labelNameClass.setFont(new Font("Comic Sans MS",Font.ROMAN_BASELINE, LABELS_SIZE));
				
		nameClass = new JComboBox<String>();
		nameClass.setBounds(new Rectangle(450,30));
		nameClass.setLocation(SECOND_COL, SECOND_ROW); 
		nameClass.setBackground(Color.white); 
		nameClass.setEditable(false);
		nameClass.setEnabled(false); 
		
		helpNameClass = new JButton();
		helpNameClass.setBounds(new Rectangle(25,25));
		helpNameClass.setLocation(THIRD_COL, SECOND_ROW); 
		helpNameClass.setIcon(imageLoader.getHelpIcon());
		helpNameClass.setToolTipText(LABEL_NAME_CLASS);
			
		labelXml = new JLabel(LABEL_XML);
		labelXml.setBounds(new Rectangle(200,30)); 
		labelXml.setLocation(FIRST_COL, THIRD_ROW); 
		labelXml.setFont(new Font("Comic Sans MS",Font.ROMAN_BASELINE, LABELS_SIZE));
		
		nameXml = new TextField();
		nameXml.setBounds(new Rectangle(450,30));
		nameXml.setLocation(SECOND_COL,THIRD_ROW);
		nameXml.setEnabled(false); 
			 
		helpXmlFile = new JButton();
		helpXmlFile.setBounds(new Rectangle(25,25));
		helpXmlFile.setLocation(THIRD_COL, THIRD_ROW); 
		helpXmlFile.setIcon(imageLoader.getHelpIcon());
		helpXmlFile.setToolTipText(XML_FILE_TOOLTIP);
		
		back =new JButton("Back");
		back.setBounds(new Rectangle(690, 206, 100, 40));
		back.setLocation(TRACER_COL+2*DISTANCE_BUTTONS, CANCEL_TRACER_ROW); 
		back.setBackground(Color.white); 
		back.setFont(new Font("Comic Sans MS",Font.BOLD, 15)); 
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clickedBack();			
			}
		});
	
		
		tracer = new JButton("Trace");
		tracer.setLayout(new GridLayout(1,1)); 
		tracer.setBounds(new Rectangle(100,40));
		tracer.setLocation(TRACER_COL+DISTANCE_BUTTONS, CANCEL_TRACER_ROW); 
		tracer.setBackground(Color.white);  
		tracer.setFont(new Font("Comic Sans MS",Font.BOLD, 15)); 
		tracer.setEnabled(false); 
		tracer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
					controller.clickedOnTrace();
					contentPane=getContentPane();
					//InitLoading();
					//FinishedLoading();
			}
			
		});
	
		profiling = new JButton("Profile");
		profiling.setLayout(new GridLayout(1,1)); 
		profiling.setBounds(new Rectangle(100,40));
		profiling.setLocation(TRACER_COL, CANCEL_TRACER_ROW); 
		profiling.setBackground(Color.white);  
		profiling.setFont(new Font("Comic Sans MS",Font.BOLD, 15)); 
		profiling.setEnabled(false); 
		profiling.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clickedOnProfiling();
				contentPane=getContentPane();
				
			}
		});
		
		examine = new JButton("Examine"); 
		examine.setLayout(new GridLayout(1,1)); 
		examine.setBounds(new Rectangle(90,30));
		examine.setLocation(EXAMINE_COL, FIRST_ROW); 
		examine.setBackground(Color.LIGHT_GRAY); 
		examine.setFont(new Font("Comic Sans MS",Font.BOLD, 12)); 
		examine.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				//Title window
				chooser.setDialogTitle("Java Tracer");
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				FileNameExtensionFilter filtroXml = new FileNameExtensionFilter("Jar Files","jar");
				chooser.setFileFilter(filtroXml);
				//return directory file
				
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {		
 					String file_selected= chooser.getSelectedFile().toString();
 					path.setText(file_selected);
					controller.selectedPath(file_selected);
				} else {
					chooser.cancelSelection();
				}
			}
		});
		
		
		getContentPane().setLayout(null); 
		getContentPane().add(path);
		//getContentPane().add(profilingCheckBox);
		getContentPane().add(tracer);
		getContentPane().add(examine);
		getContentPane().add(back);
		getContentPane().add(labelPath);
		getContentPane().add(nameClass);
		getContentPane().add(labelNameClass);
		getContentPane().add(helpPath);
		getContentPane().add(helpNameClass);
		getContentPane().add(labelXml);
		getContentPane().add(nameXml);
		getContentPane().add(helpXmlFile);
		getContentPane().add(addArgument);
		getContentPane().add(profiling);
	
	}
	
	public void finishedTrace() {
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(),Message.FINISHED);
	}
	
	
	public void errorNameXml(){
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(),Message.ERROR_EXTENSION);
	}
/*
	public void InitLoading(){
		loading=new LoadingMain();
		loading.addScreen();
		setContentPane(loading.getContentPane());
		setSize(400, 300);
	}

	public void FinishedLoading(){
		setContentPane(contentPane);
		setSize(850, 300);
  	}*/


	public void loadClasses(List<String> classes) {
	
		nameClass.removeAllItems();
		
		for (int i=0;i<classes.size();i++){
			nameClass.addItem(classes.get(i));
		}
		
		if (nameClass.getItemCount()>0){
			tracer.setEnabled(true);
			profiling.setEnabled(true); 
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
		return nameXml.getText();
	}

	public void enableMainClassCombo() {
		nameClass.setEnabled(true);		
	}

	public void setController(TracerController controller){
		this.controller = controller;
	}
}