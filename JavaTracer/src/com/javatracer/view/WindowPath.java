package com.javatracer.view;

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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.javatracer.controller.TracerController;

@SuppressWarnings("serial")
public class WindowPath extends JFrame {

	private JButton tracer,examine,cancel,helpPath,helpNameClass,helpXmlFile;
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

	
	public WindowPath(TracerController controller) {
		this.controller = controller;
		initialice();	
	} 
	
	public void initialice(){
		
		setTitle("Java Tracer");
		setSize(850, 300);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = new ImageIcon(getClass().getResource("j4.jpe")).getImage();
		setIconImage(icon);
	
		
		setVisible(true); 	

		
		labelPath = new JLabel("Select the directory ");
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
		helpPath.setIcon(new ImageIcon(getClass().getResource("image6.jpe")));
		helpPath.setToolTipText("You should choose the directory where are all files .class"); 
		
		labelNameClass = new JLabel("Insert the name of Main class");
		labelNameClass.setBounds(new Rectangle(540,30));
		labelNameClass.setLocation(FIRST_COL, SECOND_ROW); 
		labelNameClass.setBackground(Color.white); 
		labelNameClass.setFont(new Font("Comic Sans MS",Font.ROMAN_BASELINE, LABELS_SIZE));
				
		nameClass = new JComboBox();
		nameClass.setBounds(new Rectangle(450,30));
		nameClass.setLocation(SECOND_COL, SECOND_ROW); 
		nameClass.setBackground(Color.white); 
		nameClass.setEditable(false);
		nameClass.setEnabled(false); 
		
		helpNameClass = new JButton();
		helpNameClass.setBounds(new Rectangle(25,25));
		helpNameClass.setLocation(THIRD_COL, SECOND_ROW); 
		helpNameClass.setIcon(new ImageIcon(getClass().getResource("image6.jpe")));
		helpNameClass.setToolTipText("Name of main class, if your main class there is in a package you should insert the name of the package");
			
		labelXml = new JLabel("Insert xml name");
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
		helpXmlFile.setIcon(new ImageIcon(getClass().getResource("image6.jpe")));
		helpXmlFile.setToolTipText("Filename without extension");
		
		cancel =new JButton("Cancel");
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
				try {
					
					contentPane=getContentPane();
					InitLoading();
					String file =path.getText();
					String name= (String) nameClass.getSelectedItem();
					String IsPuntoClass="";
					
						if(name.length() > 5)
							IsPuntoClass = name.substring(name.length()-5, name.length());
					
						if (IsPuntoClass.equals("class")||IsPuntoClass.equals("Class")) name=name.substring(0, name.length()-6);
						
					
								if(file!=null && file!="" && !file.equals("") && name!=null && name!="" && !name.equals("")){
				
									
									String[] args=ProcessPath(file,name);
									String nameXlm = nameXml.getText();
									if (nameXlm.contains(".xml") || nameXlm.contains(".XML")){
										errorNameXml();
									} else {
											if (nameXlm.equals("")) nameXlm ="default";
											controller.startTrace(args,nameXlm); 	
									}
									
					
								}else JOptionPane.showMessageDialog(new JFrame(), "White text area");
						
					FinishedLoading();
			
				} catch (Exception e1) {
					
					e1.printStackTrace();
					
				}
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
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//return directory file
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {		
					 
 					String sf=chooser.getSelectedFile().toString();
 					nameClass.setEnabled(true);
 					nameXml.setEnabled(true); 
					path.setText(sf);
					controller.loadClassFromPath(sf);		
				} else chooser.cancelSelection();
				
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
		this.repaint();
	}
	

	public void showErrorMain() {
		JOptionPane.showMessageDialog(new JFrame(),"This class must contain the method: public" 
	       +"static void main(String[] args)");
	}

	public void showErrorLoadClass() {
		JOptionPane.showMessageDialog(new JFrame(),"was not found or loaded main class");
	}

	public void finishedTrace() {
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(), "JavaTracer has finished. You can see the trace at the file created in the same directory that you "
				+ "	launched ");
	}
	
	
	public void errorNameXml(){
		JOptionPane.setDefaultLocale(new Locale("en"));
		JOptionPane.showMessageDialog(new JFrame(), "The file name is invalid, should not lead extension "
				+ "	launched ");
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
		tracer.setEnabled(true);
	}

	private String[] ProcessPath(String file, String name) {
		
		
		boolean equals=false;
		String[] args = new String[2];
		
		String Path_file=controller.giveMePathController(name);
		file=Path_file.substring(0, Path_file.lastIndexOf("\\"));
		
			Path_file=file.replaceAll("\\\\", ".");
			
				while(!equals){
		    	
					if(name.contains(Path_file.substring(Path_file.lastIndexOf(".")+1,Path_file.length())))
							file=file.substring(0, file.lastIndexOf("\\"));
					else 
						equals=true;
	
					Path_file=Path_file.substring(0, Path_file.lastIndexOf("."));
	
					}
			
				
	
	
		
	  args[0]=file;
	  args[1]=name;
		
	  return args;
	}
	
	
	
	
}