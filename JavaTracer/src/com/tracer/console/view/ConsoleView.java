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
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.alee.laf.button.WebToggleButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.general.resources.ImageLoader;
import com.tracer.console.presenter.ConsolePresenter;

@SuppressWarnings("serial")
public class ConsoleView extends WebPanel implements ActionListener{
	
	private static final String CONSOLE_TEXT = "Console";
	private static final String CLEAR_CONSOLE = "Clear console";
	private enum Status{LAUNCHING,TRACING,PROFILING,FINISHED};
	
	private ConsolePresenter presenter; 
	private ImageLoader imageLoader;
	private Status status;
	private double lastSize;
	private int height;
	
	private WebToggleButton btnClearConsole;
	private WebToggleButton btnMinimizeConsole;
		
	private ConsoleTextPane console;
	private JLabel lblConsoleStatus;
	private JPanel header;
	private WebToggleButton btnPlay;
	private WebToggleButton btnStop;
	private WebToggleButton btnMaximizeConsole;
	private WebScrollPane scrollPane;
	
	public ConsoleView(){
		
		lastSize = 0;
		status = Status.LAUNCHING;
		setDrawFocus(false);
		
		imageLoader = ImageLoader.getInstance();
		setLayout(new BorderLayout(0, 0));
				
		console = new ConsoleTextPane(this);
		console.setEditable(false);
		
		JPanel panel = new JPanel();
		panel.setBorder(UIManager.getBorder("MenuBar.border"));
		panel.setBackground(Color.WHITE);
		panel.setBounds(new Rectangle(1, 1, 1, 1));
		panel.setLayout(new BorderLayout(0, 0));
		
		lblConsoleStatus = new JLabel(CONSOLE_TEXT);
		lblConsoleStatus.setBorder(new EmptyBorder(0, 5, 0, 0));
		lblConsoleStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblConsoleStatus.setHorizontalTextPosition(SwingConstants.CENTER);
		lblConsoleStatus.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblConsoleStatus, BorderLayout.WEST);
		
		header = new JPanel();
		header.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) header.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel.add(header, BorderLayout.EAST);
		
		btnStop = new WebToggleButton();
		btnStop.setVisible(false);
		btnStop.setToolTipText("Clear console");
		btnStop.setRolloverDecoratedOnly(true);
		btnStop.setHorizontalAlignment(SwingConstants.RIGHT);
		btnStop.setDrawFocus(false);
		btnStop.setIcon(new ImageIcon(imageLoader.getStopIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		header.add(btnStop);
		
		btnPlay = new WebToggleButton();
		btnPlay.setEnabled(false);
		btnPlay.setToolTipText("Clear console");
		btnPlay.setRolloverDecoratedOnly(true);
		btnPlay.setHorizontalAlignment(SwingConstants.RIGHT);
		btnPlay.setIcon(new ImageIcon(imageLoader.getPlayIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		btnPlay.setDrawFocus(false);
		header.add(btnPlay);
		
		btnMinimizeConsole = new WebToggleButton();
		header.add(btnMinimizeConsole);
		btnMinimizeConsole.setRolloverDecoratedOnly(true);
		btnMinimizeConsole.setToolTipText("Clear console");
		btnMinimizeConsole.setHorizontalAlignment(SwingConstants.RIGHT);
		btnMinimizeConsole.setIcon(new ImageIcon(imageLoader.getMinimizeIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		btnMinimizeConsole.setDrawFocus(false);
		btnMinimizeConsole.addActionListener(this);
		
		btnMaximizeConsole = new WebToggleButton();
		btnMaximizeConsole.setVisible(false);
		btnMaximizeConsole.setToolTipText("Clear console");
		btnMaximizeConsole.setRolloverDecoratedOnly(true);
		btnMaximizeConsole.setHorizontalAlignment(SwingConstants.RIGHT);
		btnMaximizeConsole.setIcon(new ImageIcon(imageLoader.getMaximizeIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		btnMaximizeConsole.setDrawFocus(false);
		header.add(btnMaximizeConsole);
		
		btnClearConsole = new WebToggleButton();
		header.add(btnClearConsole);
		btnClearConsole.setRolloverDecoratedOnly(true);
		btnClearConsole.setHorizontalAlignment(SwingConstants.RIGHT);
		btnClearConsole.setToolTipText(CLEAR_CONSOLE);
		btnClearConsole.setIcon(new ImageIcon(imageLoader.getDeleteIcon().getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
		btnClearConsole.setDrawFocus(false);
		btnClearConsole.addActionListener(this);
		
		scrollPane = new WebScrollPane(console);
		scrollPane.setColumnHeaderView(panel);
		scrollPane.setDrawFocus(false);
		add(scrollPane);
	}
	
	public void setPresenter(ConsolePresenter presenter){
		this.presenter = presenter;
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source.equals(btnClearConsole)){
			clear();
		} else if (source.equals(btnMinimizeConsole)){
			minimize();
		} else if (source.equals(btnMaximizeConsole)){
			maximize();
		} else if (source.equals(btnPlay)){
			play();
		} else if (source.equals(btnStop)){
			stop();
		}
		
	}

	private void stop() {
	}

	private void play() {
	}

	private void maximize() {
		scrollPane.setSize(scrollPane.getWidth(),header.getHeight()+4);
		btnMaximizeConsole.setVisible(true);
		btnMinimizeConsole.setVisible(true);
		presenter.minimize();
	}

	private void minimize() {
		height = header.getHeight()+4;
		scrollPane.setSize(scrollPane.getWidth(),height);
		btnMaximizeConsole.setVisible(true);
		btnMinimizeConsole.setVisible(true);
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

	public int defaultHeight() {
		
		return 0;
	}
	
}
