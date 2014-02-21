package com.javatracer.view;

import java.awt.BorderLayout;

import javax.swing.*;

@SuppressWarnings("serial")
public class LoadingMain extends JWindow {

	  BorderLayout borderLayout1 = new BorderLayout();
	  JLabel imageLabel = new JLabel();
	  ImageIcon imageIcon;
	  //private static String PATH_IMAGE_lOADING = "../../../resource/LoadingSmall.gif";

  public LoadingMain() {
    InitScreen();
    }

  private void InitScreen() {

  	String curDir = System.getProperty("user.dir");
  	String dirImage=curDir+"\\src\\resource\\LoadingSmall.gif";
  	imageIcon =new ImageIcon(dirImage);

    setLocationRelativeTo(null);
    
   
  }

  public void draWindow() {
	   
	  	imageLabel.setIcon(imageIcon);
	    this.getContentPane().setLayout(borderLayout1);
	    this.getContentPane().add(imageLabel, BorderLayout.CENTER);
	    this.pack();
	    
	  }
  
  
 
  public void addScreen(){
	  draWindow();
	  repaint();
	  validate();
	  setVisible(false);
  }
  
}

