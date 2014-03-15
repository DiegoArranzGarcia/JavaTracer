package com.tracer.console.view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


@SuppressWarnings("serial")
public class ConsoleTextPane extends JTextPane implements KeyListener{
	
	private SimpleAttributeSet error;
	private SimpleAttributeSet output;
	private SimpleAttributeSet input;
	private ConsoleView view;
	
	private StyledDocument document;
	private List<Integer> caretPositions;
	
	public ConsoleTextPane(ConsoleView view){
		setBorder(new EmptyBorder(0, 5, 0, 0));
		addKeyListener(this);
		createSytles();
		document = getStyledDocument();
		caretPositions = new ArrayList<Integer>();
		caretPositions.add(0);
		this.view = view;
	}
		
	private void createSytles() {
		error = new SimpleAttributeSet();
		StyleConstants.setForeground(error, Color.RED);
		StyleConstants.setFontFamily(error, "Consolas");
		StyleConstants.setFontSize(error, 14);
		output = new SimpleAttributeSet();
		StyleConstants.setForeground(output, Color.BLACK);
		StyleConstants.setFontFamily(output, "Consolas");
		StyleConstants.setFontSize(output, 14);
		input = new SimpleAttributeSet();
		StyleConstants.setForeground(input,new Color(0x88A85));
		StyleConstants.setFontFamily(input, "Consolas");
		StyleConstants.setFontSize(input, 14);
	}

	public void writeOutput(String string) {
		try {
			caretPositions.add(document.getLength());
			document.insertString(document.getLength(),string,output);
			caretPositions.add(document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void writeError(String string) {
		try {
			caretPositions.add(document.getLength());
			document.insertString(document.getLength(),string,error);
			caretPositions.add(document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER){
			caretPositions.add(document.getLength());
			String getText = getTextFromConsole();
			caretPositions.clear();
			caretPositions.add(document.getLength());
			view.input(getText);
		} else if (keyCode == KeyEvent.VK_BACK_SPACE){ //Key <-
			delete();
		} else if (keyCode == KeyEvent.VK_DELETE){ //Key "Supr"
			supr();
		} else if (!specialKey(keyCode)){ 
			insertInput(e.getKeyChar());
		}
		
	}
	
	private String getTextFromConsole() {
		String fullText = "";
		for (int i=0;i<caretPositions.size();i+=2){
			try {
				fullText += getText(caretPositions.get(i),caretPositions.get(i+1)-caretPositions.get(i));
			} catch (BadLocationException e){}	
		}
		
		return fullText;
	}
	
	private boolean specialKey(int keyCode) {
		return (keyCode == KeyEvent.VK_F1) || (keyCode == KeyEvent.VK_F2) || (keyCode == KeyEvent.VK_F3) || (keyCode == KeyEvent.VK_F4) || (keyCode == KeyEvent.VK_F5) || (keyCode == KeyEvent.VK_F6)
				|| (keyCode == KeyEvent.VK_F7) || (keyCode == KeyEvent.VK_F8) || (keyCode == KeyEvent.VK_F9) || (keyCode == KeyEvent.VK_F10) || (keyCode == KeyEvent.VK_F11) || (keyCode == KeyEvent.VK_F12)
				|| (keyCode == KeyEvent.VK_F13) || (keyCode == KeyEvent.VK_F14) || (keyCode == KeyEvent.VK_F15) || (keyCode == KeyEvent.VK_F16) || (keyCode == KeyEvent.VK_F17) || (keyCode == KeyEvent.VK_F18)
				|| (keyCode == KeyEvent.VK_F19) || (keyCode == KeyEvent.VK_F20) || (keyCode == KeyEvent.VK_F21) || (keyCode == KeyEvent.VK_F22) || (keyCode == KeyEvent.VK_F23) || (keyCode == KeyEvent.VK_F24)
				|| (keyCode == KeyEvent.VK_CONTROL) || (keyCode == KeyEvent.VK_ALT) || (keyCode == KeyEvent.VK_TAB) || (keyCode == KeyEvent.VK_SHIFT) || (keyCode == KeyEvent.VK_ESCAPE)
				|| (keyCode == KeyEvent.VK_INSERT) || (keyCode == KeyEvent.VK_PAUSE) || (keyCode == KeyEvent.VK_PRINTSCREEN) || (keyCode == KeyEvent.VK_F) || (keyCode == KeyEvent.VK_ALT_GRAPH)
				|| (keyCode == KeyEvent.VK_NUM_LOCK) || (keyCode == KeyEvent.VK_END) || (keyCode == KeyEvent.VK_BEGIN) || (keyCode == KeyEvent.VK_PAGE_UP) || (keyCode == KeyEvent.VK_PAGE_DOWN)
				|| (keyCode == KeyEvent.VK_WINDOWS) || (keyCode == KeyEvent.VK_CAPS_LOCK) || (keyCode == KeyEvent.VK_CONTEXT_MENU) || (keyCode == KeyEvent.VK_UP) || (keyCode == KeyEvent.VK_DOWN)
				|| (keyCode == KeyEvent.VK_RIGHT) || (keyCode == KeyEvent.VK_LEFT) || (keyCode == 0);
	}

	private void delete() {
		int caretPosition = getCaretPosition();
		if (getLastCaretPosition()<caretPosition)
			getActionMap().get("delete-previous").setEnabled(true);
		else {
			getActionMap().get("delete-previous").setEnabled(false);
		}
	}

	private int getLastCaretPosition() {
		return caretPositions.get(caretPositions.size()-1);
	}

	private void supr() {
		int caretPosition = getCaretPosition();
		if (getLastCaretPosition()<caretPosition){
			getActionMap().get("delete-next").setEnabled(true);
		} else {
			getActionMap().get("delete-next").setEnabled(false);
		}
	}

	private void insertInput(char keyChar) {
		setCaretPosition(document.getLength());
		document.setParagraphAttributes(document.getLength(),1,input,false);
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void clear() {
		setText("");
		caretPositions.clear();
		caretPositions.add(0);
	}



}
