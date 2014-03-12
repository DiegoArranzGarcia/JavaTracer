package com.tracer.console.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.tracer.console.presenter.ConsolePresenter;

@SuppressWarnings("serial")
public class ConsoleView extends JFrame implements KeyListener{
	
	private JTextArea textArea;
	private ConsolePresenter presenter; 
	
	public ConsoleView(){
		textArea = new JTextArea();
		add(textArea);
		setSize(600,600);
		setLocationRelativeTo(null);
		textArea.addKeyListener(this);
	}

	public void write(String string) {
		textArea.append(string);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			String[] lines = textArea.getText().split("\\n");
			presenter.input(lines[lines.length-1]);
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public void setPresenter(ConsolePresenter presenter){
		this.presenter = presenter;
	}
	
}
