package com.javatracer.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.general.imageresources.ImageLoader;
import com.javatracer.controller.TracerController;

@SuppressWarnings("serial")
public class JavaTracerView extends JFrame {

	private JButton tracer,examine,cancel,helpPath,helpNameClass,helpXmlFile,addArgument;
	private TextField path,nameXml;
	private JComboBox<String> nameClass;
	private JFileChooser chooser;
	private JLabel labelPath,labelNameClass,labelXml;
	private TracerController controller;
	private LoadingMain loading;
	private Container contentPane;
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
	
	public JavaTracerView(TracerController controller) {
		this.controller = controller;
		initialice();	
	} 
	
	public void initialice(){
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		setTitle("Java Tracer");
		setSize(1050, 300);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = imageLoader.getApplicationIcon().getImage();
		setIconImage(icon);
		
		labelPath = new JLabel(new Message(1).getMessage());
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
		helpPath.setToolTipText(new Message(2).getMessage());
		
		addArgument = new JButton("Add arguments");
		addArgument.setBounds(new Rectangle(140,25));
		addArgument.setLocation(850, SECOND_ROW+2); 
		addArgument.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JListWindow jListWindow = new JListWindow();
				jListWindow.setVisible(true); 
			}
		});
		
		labelNameClass = new JLabel(new Message(3).getMessage());
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
		helpNameClass.setToolTipText(new Message(4).getMessage());
			
		labelXml = new JLabel(new Message(5).getMessage());
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
		helpXmlFile.setToolTipText(new Message(6).getMessage());
		
		cancel =new JButton("Exit");
		cancel.setBounds(new Rectangle(100,40));
		cancel.setLocation(CANCEL_COL, CANCEL_TRACER_ROW); 
		cancel.setBackground(Color.white); 
		cancel.setFont(new Font("Comic Sans MS",Font.BOLD, 15)); 
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); 
				
			}
		});
		
		tracer = new JButton("Trace");
		tracer.setLayout(new GridLayout(1,1)); 
		tracer.setBounds(new Rectangle(100,40));
		tracer.setLocation(TRACER_COL, CANCEL_TRACER_ROW); 
		tracer.setBackground(Color.white);  
		tracer.setFont(new Font("Comic Sans MS",Font.BOLD, 15)); 
		tracer.setEnabled(false); 
		tracer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
					controller.clickedOnTrace();
					contentPane=getContentPane();
					InitLoading();
					
						
					FinishedLoading();
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
		
		
		setLayout(null); 
		add(path);
		add(tracer);
		add(examine);
		add(cancel);
		add(labelPath);
		add(nameClass);
		add(labelNameClass);
		add(helpPath);
		add(helpNameClass);
		add(labelXml);
		add(nameXml);
		add(helpXmlFile);
		add(addArgument);
	
		setVisible(true); 	;
	}
	

	public void showErrorMain() {
		JOptionPane.showMessageDialog(new JFrame(),new Message(10).getMessage());
	}

	public void showErrorLoadClass() {
		JOptionPane.showMessageDialog(new JFrame(),new Message(11).getMessage());
	}

	public void finishedTrace() {
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(), new Message(12).getMessage());
	}
	
	
	public void errorNameXml(){
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(), new Message(13).getMessage());
	}


	public void InitLoading(){
		loading=new LoadingMain();
		loading.addScreen();
		setContentPane(loading.getContentPane());
		setSize(400, 300);
	}

	public void FinishedLoading(){
		setContentPane(contentPane);
		setSize(850, 300);
  	}


	public void loadClasses(List<String> classes) {
	
		nameClass.removeAllItems();
		
		for (int i=0;i<classes.size();i++){
			nameClass.addItem(classes.get(i));
		}
		
		if (nameClass.getItemCount()>0){
			tracer.setEnabled(true);
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
	
	
}