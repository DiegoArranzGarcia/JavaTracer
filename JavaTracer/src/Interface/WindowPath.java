package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import Tracer.Trace;

public class WindowPath extends JFrame {

	private JButton tracer,examine,cancel;
	private TextField path;
	private JFileChooser chooser;
	private JLabel labelPath;
	
	public WindowPath() {
		initialice();
	} 
	
	public void initialice(){

		//Inspector i= new Inspector();
		
		setTitle("Java Tracer");
		setSize(700, 300);  
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); 		
		
		labelPath = new JLabel("Select the Main class");
		labelPath.setBounds(new Rectangle(540,30));
		labelPath.setLocation(20, 15); 
		labelPath.setBackground(Color.white); 
		
		path = new TextField();
		path.setBounds(new Rectangle(540,30));
		path.setLocation(20, 60); 
		path.setBackground(Color.white); 
		
		cancel =new JButton("Cancel");
		cancel.setBounds(new Rectangle(100,40));
		cancel.setLocation(350, 150); 
		cancel.setBackground(Color.white); 
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); 
				
			}
		});
		
		tracer = new JButton("Trace");
		tracer.setLayout(new GridLayout(1,1)); 
		tracer.setBounds(new Rectangle(100,40));
		tracer.setLocation(200, 150); 
		tracer.setBackground(Color.white); 
		tracer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					
				String file =path.getText();
				
				if(file!=null && file!="" && !file.equals("")){
					
					String[] args= processPath(file);
					Trace trace=new Trace(args); 
			  
						if(!trace.getMistake()){
							JOptionPane.setDefaultLocale(new Locale("en"));
							JOptionPane.showMessageDialog(new JFrame(), "JavaTracer has finished. You can see the trace at the file created in the same directory that you "
									+ "	launched ");
						}
					
				 }else JOptionPane.showMessageDialog(new JFrame(), " White text area");
			
				} catch (Exception e1) {
					
					e1.printStackTrace();
					
				}
			}
		});
		
		examine = new JButton("Examine"); 
		examine.setLayout(new GridLayout(1,1)); 
		examine.setBounds(new Rectangle(100,30));
		examine.setLocation(585, 60); 
		examine.setBackground(Color.LIGHT_GRAY); 
		examine.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				//Title window
				chooser.setDialogTitle("Java Tracer");
			
				//return directory file
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
 					String sf=chooser.getSelectedFile().toString();
					path.setText(sf);
					 		
				
				} else chooser.cancelSelection();
				
			}
		});
		
		
		setLayout(null); 
		add(path);
		add(tracer);
		add(examine);
		add(cancel);
		add(labelPath);
		this.repaint();

	}
	
	private String[] processPath(String path) {

		if (path.contains(".class")) path = path.replaceAll(".class", "");
		System.out.println(path);
		String filePath= path.substring(0,path.indexOf("bin")+3);
		String nameClass = path.substring(filePath.indexOf("bin")+4).replaceAll("\\\\", ".");
		String[] args={filePath,nameClass};
		return args;
	}
	
	
}

