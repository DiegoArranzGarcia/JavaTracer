package com.tracer.console.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	private static final String CLEAR_CONSOLE = "Clear console";
	
	private ConsolePresenter presenter; 
	private ImageLoader imageLoader;
	
	private WebToggleButton btnClearConsole;
	private WebToggleButton btnMinimizeConsole;
		
	private ConsoleTextPane console;
	private JLabel lblNewLabel;
	private JPanel panel_1;
	
	public ConsoleView(){
		setRequestFocusEnabled(false);
		imageLoader = ImageLoader.getInstance();
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		console = new ConsoleTextPane(this);
		scrollPane.setViewportView(console);
		
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
	
	
}
