package com.tracer.console.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.alee.laf.button.WebToggleButton;
import com.general.resources.ImageLoader;
import com.tracer.console.presenter.ConsolePresenter;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.border.EmptyBorder;
import java.awt.Rectangle;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ConsoleView extends JPanel implements KeyListener{
	
	private static final String CLEAR_CONSOLE = "Clear console";
	
	private ConsolePresenter presenter; 
	private ImageLoader imageLoader;
	
	private WebToggleButton btnClearConsole;
	private WebToggleButton btnMinimizeConsole;
	private JEditorPane consoleText;
	private Document document;
	
	private SimpleAttributeSet error;
	private SimpleAttributeSet output;
	private SimpleAttributeSet input;
	private JLabel lblNewLabel;
	private JPanel panel_1;
	
	public ConsoleView(){
		setRequestFocusEnabled(false);
		imageLoader = ImageLoader.getInstance();
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		consoleText = new JEditorPane();
		scrollPane.setViewportView(consoleText);
		createSytles();
		consoleText.addKeyListener(this);
		document = consoleText.getDocument();
		
		JPanel panel = new JPanel();
		panel.setBorder(UIManager.getBorder("MenuBar.border"));
		panel.setBackground(Color.WHITE);
		panel.setBounds(new Rectangle(1, 1, 1, 1));
		scrollPane.setColumnHeaderView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("Console");
		lblNewLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel, BorderLayout.WEST);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel.add(panel_1, BorderLayout.EAST);
		
		btnMinimizeConsole = new WebToggleButton();
		panel_1.add(btnMinimizeConsole);
		btnMinimizeConsole.setRolloverDecoratedOnly(true);
		btnMinimizeConsole.setToolTipText("Clear console");
		btnMinimizeConsole.setHorizontalAlignment(SwingConstants.RIGHT);
		btnMinimizeConsole.setIcon(new ImageIcon(imageLoader.getMinimizeIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		btnMinimizeConsole.setDrawFocus(false);
		
		btnClearConsole = new WebToggleButton();
		panel_1.add(btnClearConsole);
		btnClearConsole.setRolloverDecoratedOnly(true);
		btnClearConsole.setHorizontalAlignment(SwingConstants.RIGHT);
		btnClearConsole.setToolTipText(CLEAR_CONSOLE);
		btnClearConsole.setIcon(new ImageIcon(imageLoader.getDeleteIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		btnClearConsole.setDrawFocus(false);
		
	}

	private void createSytles() {
		error = new SimpleAttributeSet();
		StyleConstants.setForeground(error, Color.RED);
		output = new SimpleAttributeSet();
		input = new SimpleAttributeSet();
	}

	public void writeOutput(String string) {
		try {
			document.insertString(document.getLength(),string,output);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void writeError(String string) {
		try {
			document.insertString(document.getLength(),string,error);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER){
			String[] lines = consoleText.getText().split("\\n");
			presenter.input(lines[lines.length-1]);
		} else if (!(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT)){
			//TODO : AvPag/RePag
			setForeground(Color.ORANGE);
			consoleText.setCaretPosition(document.getLength());
		}
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
	
	public void setPresenter(ConsolePresenter presenter){
		this.presenter = presenter;
	}
	
}
