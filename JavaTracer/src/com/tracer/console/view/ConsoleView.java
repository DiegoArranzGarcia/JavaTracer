package com.tracer.console.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.alee.laf.button.WebToggleButton;
import com.general.resources.ImageLoader;
import com.tracer.console.presenter.ConsolePresenter;

@SuppressWarnings("serial")
public class ConsoleView extends JPanel implements ActionListener{
	
	private static final String CONSOLE_TEXT = "Console";
	private static final String CLEAR_CONSOLE = "Clear console";
	private enum Status{LAUNCHING,TRACING,PROFILING,FINISHED};
	
	private ConsolePresenter presenter; 
	private ImageLoader imageLoader;
	private Status status;
	private double lastSize;
	
	private WebToggleButton btnClearConsole;
	private WebToggleButton btnMinimizeConsole;
		
	private ConsoleTextPane console;
	private JLabel lblConsoleStatus;
	private JPanel panel_1;
	
	public ConsoleView(){
		
		lastSize = 0;
		status = Status.LAUNCHING;
		
		setRequestFocusEnabled(false);
		imageLoader = ImageLoader.getInstance();
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		console = new ConsoleTextPane(this);
		console.setEditable(false);
		scrollPane.setViewportView(console);
		
		JPanel panel = new JPanel();
		panel.setBorder(UIManager.getBorder("MenuBar.border"));
		panel.setBackground(Color.WHITE);
		panel.setBounds(new Rectangle(1, 1, 1, 1));
		scrollPane.setColumnHeaderView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblConsoleStatus = new JLabel(CONSOLE_TEXT);
		lblConsoleStatus.setBorder(new EmptyBorder(0, 5, 0, 0));
		lblConsoleStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblConsoleStatus.setHorizontalTextPosition(SwingConstants.CENTER);
		lblConsoleStatus.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblConsoleStatus, BorderLayout.WEST);
		
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
		btnMinimizeConsole.addActionListener(this);
		
		btnClearConsole = new WebToggleButton();
		panel_1.add(btnClearConsole);
		btnClearConsole.setRolloverDecoratedOnly(true);
		btnClearConsole.setHorizontalAlignment(SwingConstants.RIGHT);
		btnClearConsole.setToolTipText(CLEAR_CONSOLE);
		btnClearConsole.setIcon(new ImageIcon(imageLoader.getDeleteIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		btnClearConsole.setDrawFocus(false);
		btnClearConsole.addActionListener(this);
	}
	
	public void setPresenter(ConsolePresenter presenter){
		this.presenter = presenter;
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source.equals(btnClearConsole)){
			clear();
		} else {
			minimize();
		}
		
	}

	private void minimize() {
		
	}

	public void clear() {
		console.clear();
	}

	public void writeError(String string) {
		console.writeError(string);
	}

	public void writeOutput(String string) {
		console.writeOutput(string);
	}

	public void input(String text) {
		presenter.input(text);
	}

	public void setEditable(boolean editable) {
		console.setEditable(editable);
	}

	public void launching() {
		status = Status.LAUNCHING;
		updateLabel();
	}

	public void finished() {
		status = Status.FINISHED;
		updateLabel();
	}

	public void tracing() {
		status = Status.TRACING;
		updateLabel();
	}

	public void profiling() {
		status = Status.PROFILING; 
		updateLabel();
	}

	public void updateSize(String lastFileName, double size) {
		lastSize = size;
		updateLabel();
	}
	
	private void updateLabel(){
		String text = CONSOLE_TEXT;
		switch (status) {
			case FINISHED:
				text += " ( Finished ) ";
				break;
			case LAUNCHING:
				text += " ( Launching ) ";
				break;
			case PROFILING:
				text += " ( Profiling ) ";
				break;
			case TRACING:
				text += " ( Tracing ) ";
				break;
			default:
				break;
		}
		
		text += getTextSize();
		lblConsoleStatus.setText(text);
	}

	private String getTextSize() {
		
		double bytes = lastSize;
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);
		double gigabytes = (megabytes / 1024);
		
		String text = "";
		DecimalFormat df = new DecimalFormat("0.00");
	  	    
		if (gigabytes >= 1){
			text = df.format(gigabytes) + " GB";
		} else if (megabytes >= 1){
			text = df.format(megabytes) + " MB";
		} else if (kilobytes >= 1){
			text = df.format(kilobytes) + " KB";
		} else {
			text = df.format(bytes) + " B";
		}
		
		return text;
	}
}
