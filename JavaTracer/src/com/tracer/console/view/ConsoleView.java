package com.tracer.console.view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.tracer.console.presenter.ConsolePresenter;

@SuppressWarnings("serial")
public class ConsoleView extends JTextPane implements KeyListener{
	
	private ConsolePresenter presenter; 
	private StyledDocument doc;
	
	private SimpleAttributeSet error;
	private SimpleAttributeSet output;
	private SimpleAttributeSet input;
	
	public ConsoleView(){
		doc = getStyledDocument();
		createSytles();
		addKeyListener(this);
	}

	private void createSytles() {
		error = new SimpleAttributeSet();
		StyleConstants.setForeground(error, Color.RED);
		output = new SimpleAttributeSet();
		input = new SimpleAttributeSet();
	}

	public void writeOutput(String string) {
		try {
			doc.insertString(doc.getLength(),string,output);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void writeError(String string) {
		try {
			doc.insertString(doc.getLength(),string,error);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER){
			String[] lines = getText().split("\\n");
			presenter.input(lines[lines.length-1]);
		} else if (!(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT)){
			//TODO : AvPag/RePag
			setForeground(Color.ORANGE);
			setCaretPosition(doc.getLength());
		}
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
	
	public void setPresenter(ConsolePresenter presenter){
		this.presenter = presenter;
	}
	
}
