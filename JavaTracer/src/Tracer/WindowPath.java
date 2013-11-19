package Tracer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WindowPath extends JFrame {

        private JButton debug,examine,cancel;
        private TextField path;
        private JPanel panel;
        private JFileChooser chooser;
        private JLabel labelPath;
        
        public WindowPath() {
                initialice();
                } 
        public void initialice(){
                
                panel = new JPanel();
                
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
                
                debug = new JButton("Trace");
                debug.setLayout(new GridLayout(1,1)); 
                debug.setBounds(new Rectangle(100,40));
                debug.setLocation(200, 150); 
                debug.setBackground(Color.white); 
                debug.addActionListener(new ActionListener() {
                        
                        public void actionPerformed(ActionEvent e) {
                                String s=chooser.getSelectedFile().toString();
                                //String file = s.substring(s.lastIndexOf('/') + 1, s.lastIndexOf('.'));
                                String file = s.substring(0, s.lastIndexOf('.'));
                                String[] args= processPath(chooser.getSelectedFile().toString(),file);
                                new Trace(args);
                                
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
                        
                                //Si seleccionamos algún archivo retornaremos su directorio
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
                
                
                setLayout(new BorderLayout());
                panel.setLayout(null);
                add(panel);
                panel.add(path);
                panel.add(debug);
                panel.add(examine);
                panel.add(cancel);
                panel.add(labelPath); 
                

        }
        
        private String[] processPath(String path,String nameClass) {

                String filePath= path.substring(0,path.indexOf("bin")+4);
                String nameClassA = nameClass.substring(nameClass.indexOf("bin")+4).replaceAll("\\\\", ".");
                String[] args={filePath,nameClassA};
                return args;
        }
        
        
}