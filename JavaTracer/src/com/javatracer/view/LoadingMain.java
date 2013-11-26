package com.javatracer.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class LoadingMain extends JWindow {

	BorderLayout borderLayout1 = new BorderLayout();
	  JLabel imageLabel = new JLabel();
	  JPanel southPanel = new JPanel();
	  FlowLayout southPanelFlowLayout = new FlowLayout();
	  ImageIcon imageIcon;
    

  public LoadingMain() {
    InitScreen();
	}

  private void InitScreen() {
    
	String curDir = System.getProperty("user.dir");
	String dirImage=curDir+"\\src\\resource\\Loading.gif";
	  
	imageIcon = new ImageIcon(dirImage);
    setLocationRelativeTo(null);
   
  }

  public void draWindow() {
	    imageLabel.setIcon(imageIcon);
	    this.getContentPane().setLayout(borderLayout1);
	    southPanel.setLayout(southPanelFlowLayout);
	    southPanel.setBackground(Color.BLACK);
	    this.getContentPane().add(imageLabel, BorderLayout.CENTER);
	    this.getContentPane().add(southPanel, BorderLayout.SOUTH);
	    this.pack();
	  }
  
  
 
  public void addScreen()
  {
	  draWindow();
	  repaint();
	  validate();
	  setVisible(true);
  }
  
  
  public void removeScreen()
  {
	  
	  setVisible(false);
  }
  
  
}

