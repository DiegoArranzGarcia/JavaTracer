package Tracer;

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

public class WindowPath extends JFrame {

	private JButton tracer,examine,cancel;
	private TextField path;
	private JFileChooser chooser;
	private JLabel labelPath;
	
	public WindowPath() {
		initialice();
	} 
	
	public void initialice(){
		
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
	
				
				String s=chooser.getSelectedFile().toString();
				//String file = s.substring(s.lastIndexOf('/') + 1, s.lastIndexOf('.'));
				String file = s.substring(0, s.lastIndexOf('.'));
				String[] args= processPath(chooser.getSelectedFile().toString(),file);
				new Trace(args); 
				
		      JOptionPane.setDefaultLocale(new Locale("en"));
               JOptionPane.showMessageDialog(new JFrame(), "JavaTracer has finished. You can see the trace at the file created in the same directory that you "
               		+ "	lanuched "); 
			
				} catch (Exception e1) {
					
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
				//Titulo que llevara la ventana
				chooser.setDialogTitle("Titulo");
			
				//Si seleccionamos alg�n archivo retornaremos su directorio
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String sf=chooser.getSelectedFile().toString();				
					String s=chooser.getSelectedFile().getName();
					
					String file = sf.substring(0, sf.lastIndexOf('.'));
					String[] args= processPath(chooser.getSelectedFile().toString(),file);
					path.setText(args[0]+args[1]);
					new Trace(args);
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
		

	}
	
	private String[] processPath(String path,String nameClass) {

		String filePath= path.substring(0,path.indexOf("bin")+4);
		String nameClassA = nameClass.substring(nameClass.indexOf("bin")+4).replaceAll("\\\\", ".");
		String[] args={filePath,nameClassA};
		return args;
	}
	
	
}

