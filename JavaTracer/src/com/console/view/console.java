package com.console.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.console.keys.CharKey;
import com.console.keys.StrokeInformer;






public class console extends JFrame {

	
	
    private JTextArea area;
    private JScrollPane scroll;
    private StrokeInformer aStrokeInformer; // Object to which strokes are informed

 public console(){
	
	setVisible(true);
	setSize(400, 400);
	setLocationRelativeTo(null);
	getContentPane().setLayout(new BorderLayout(0, 0));
	
	
	area= new JTextArea();
	area.setBackground(Color.WHITE);
	area.setForeground(Color.BLACK);
	area.setCaretColor(Color.red);
	area.setEditable(false);
	
	scroll = new JScrollPane( area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	add(scroll);
	
	aStrokeInformer = new StrokeInformer();
	addKeyListener(aStrokeInformer);
	repaint();validate();
	
 	}

 public void write(String message){
	 area.append(message);
	 repaint();
	 validate();
	 
	 
 }
 
 
 public String read(){
	 
	
	
	  String ret = "";
      CharKey read = new CharKey(CharKey.NONE);
      while (true) {
          while (read.code == CharKey.NONE) {
              read = inkey();
          }
          if (read.isMetaKey()) {
              read.code = CharKey.NONE;
              continue;
          }
          if (read.code == CharKey.ENTER) {
              return ret;
          }
          if(read.code==CharKey.SPACE){
        	  write(" ");
          }
          
          if (read.code == CharKey.BACKSPACE) {
              if (ret.equals("")) {
                  read.code = CharKey.NONE;
                  continue;
              }
              
              if (ret.length() > 1) {
                  ret = ret.substring(0, ret.length() - 1);
                  
              } else {
                  ret = "";
              }
              
              String s=area.getText().substring(0, area.getText().length()-1);
              area.setText(s);
          
          } else {
              if (ret.length() >= 9999) {
                  read.code = CharKey.NONE;
                  continue;
              }
            
              
              String nuevo = read.toString();
              write(nuevo);
              ret += nuevo;
          		}
          
          read.code = CharKey.NONE;
   }

 }
 
 public void clearScreen(){
	 
	 area.setText("");
	 
 }
 
 
 public void waitKey(int keyCode) {
     CharKey x = new CharKey(CharKey.NONE);
     while (x.code != keyCode) {
         x = inkey();
     }
 }
 
 public synchronized CharKey inkey() {
     aStrokeInformer.informKey(Thread.currentThread());
     try {
         this.wait();
     } catch (InterruptedException ie) {
     }
     CharKey ret = new CharKey(aStrokeInformer.getInkeyBuffer());
     return ret;
 }
 
}
