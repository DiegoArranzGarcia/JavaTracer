package com.javatracer.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
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
import javax.swing.JOptionPane;

import com.javatracer.controller.TracerController;

@SuppressWarnings("serial")
public class WindowPath extends JFrame {

	private JButton tracer,examine,cancel,helpPath,helpNameClass;
	private TextField path;
	private JComboBox<String> nameClass;
	private JFileChooser chooser;
	private JLabel labelPath,labelNameClass;
	private TracerController controller;
	private LoadingMain loading;
	private Container contentPane;
	
	public WindowPath(TracerController controller) {
		this.controller = controller;
		initialice();
	} 
	
	public void initialice(){

		//Inspector i= new Inspector();
		
		setTitle("Java Tracer");
		setSize(850, 300);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); 		
		
		labelPath = new JLabel("Select the directory ");
		labelPath.setBounds(new Rectangle(540,30));
		labelPath.setLocation(20, 60); 
		labelPath.setBackground(Color.white); 
		
		labelNameClass = new JLabel("Insert the name of Main class");
		labelNameClass.setBounds(new Rectangle(540,30));
		labelNameClass.setLocation(20, 120); 
		labelNameClass.setBackground(Color.white); 
		
		path = new TextField();
		path.setBounds(new Rectangle(450,30));
		path.setLocation(250, 60); 
		path.setBackground(Color.white); 
		
		nameClass = new JComboBox();
		nameClass.setBounds(new Rectangle(450,30));
		nameClass.setLocation(250, 120); 
		nameClass.setBackground(Color.white); 
		
		helpPath = new JButton();
		helpPath.setBounds(new Rectangle(25,25));
		helpPath.setLocation(210, 62); 
		String curDir = System.getProperty("user.dir");
		String dirImage=curDir+"\\src\\resource\\image6.jpe";
		//helpPath.setIcon(new ImageIcon(dirImage));
		helpPath.setIcon(new ImageIcon(getClass().getResource("image6.jpe")));
		helpPath.setToolTipText("You should choose the directory where are all files .class"); 
		
		helpNameClass = new JButton();
		helpNameClass.setBounds(new Rectangle(25,25));
		helpNameClass.setLocation(210, 120); 
		helpNameClass.setIcon(new ImageIcon(getClass().getResource("image6.jpe")));
		//helpNameClass.setIcon(new ImageIcon(dirImage));
		helpNameClass.setToolTipText("Name of main class, if your main class there is in a package you should insert the name of the package");
			 
		cancel =new JButton("Cancel");
		cancel.setBounds(new Rectangle(100,40));
		cancel.setLocation(450, 190); 
		cancel.setBackground(Color.white); 
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); 
				
			}
		});
		
		tracer = new JButton("Trace");
		tracer.setLayout(new GridLayout(1,1)); 
		tracer.setBounds(new Rectangle(100,40));
		tracer.setLocation(300, 190); 
		tracer.setBackground(Color.white);  
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
									
								
									controller.startTrace(args); 
					
								}else JOptionPane.showMessageDialog(new JFrame(), "White text area");
						
					FinishedLoading();
			
				} catch (Exception e1) {
					
					e1.printStackTrace();
					
				}
			}

			
		});
		
		examine = new JButton("Examine"); 
		examine.setLayout(new GridLayout(1,1)); 
		examine.setBounds(new Rectangle(100,30));
		examine.setLocation(730, 60); 
		examine.setBackground(Color.LIGHT_GRAY); 
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