package com.general.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import com.general.presenter.JavaTracerPresenter;
import com.general.settings.view.SettingsView;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JavaTracerView extends JFrame implements ActionListener{

	private JavaTracerPresenter presenter; 
	
	private JButton traceProfileButton;
	private JButton inspectButton;
	private JButton exitButton;
	private JButton viewProfile;
	private JButton settingsButton;
	
	/**
	 * Create the frame.
	 */
	public JavaTracerView() {
		setTitle("JavaTracer");
		setBounds(100, 100, 500, 600);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JPanel panel = new JPanel();
		setContentPane(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		traceProfileButton = new JButton("Trace / Profile");
		sl_panel.putConstraint(SpringLayout.NORTH, traceProfileButton, 53, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, traceProfileButton, 143, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, traceProfileButton, -444, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, traceProfileButton, -146, SpringLayout.EAST, panel);
		panel.add(traceProfileButton);
		traceProfileButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		traceProfileButton.addActionListener(this);
		
		inspectButton = new JButton("Inspect trace");
		sl_panel.putConstraint(SpringLayout.WEST, inspectButton, 143, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, inspectButton, -246, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, inspectButton, -146, SpringLayout.EAST, panel);
		panel.add(inspectButton);
		inspectButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		inspectButton.addActionListener(this);
		
		exitButton = new JButton("Exit");
		sl_panel.putConstraint(SpringLayout.NORTH, exitButton, 468, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, exitButton, 143, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, exitButton, -29, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, exitButton, -146, SpringLayout.EAST, panel);
		panel.add(exitButton);
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		exitButton.addActionListener(this);
		
		viewProfile = new JButton("View Profile");
		sl_panel.putConstraint(SpringLayout.NORTH, inspectButton, 38, SpringLayout.SOUTH, viewProfile);
		sl_panel.putConstraint(SpringLayout.NORTH, viewProfile, 48, SpringLayout.SOUTH, traceProfileButton);
		sl_panel.putConstraint(SpringLayout.WEST, viewProfile, 0, SpringLayout.WEST, traceProfileButton);
		sl_panel.putConstraint(SpringLayout.SOUTH, viewProfile, -340, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, viewProfile, -146, SpringLayout.EAST, panel);
		viewProfile.addActionListener(this);
		viewProfile.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel.add(viewProfile);
		
		settingsButton = new JButton("Settings");
		sl_panel.putConstraint(SpringLayout.NORTH, settingsButton, 42, SpringLayout.SOUTH, inspectButton);
		sl_panel.putConstraint(SpringLayout.WEST, settingsButton, 0, SpringLayout.WEST, traceProfileButton);
		sl_panel.putConstraint(SpringLayout.SOUTH, settingsButton, -54, SpringLayout.NORTH, exitButton);
		sl_panel.putConstraint(SpringLayout.EAST, settingsButton, -146, SpringLayout.EAST, panel);
		settingsButton.addActionListener(this);
		settingsButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel.add(settingsButton);
		
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(traceProfileButton))
			presenter.clickedOnTraceProfile();
		else if (source.equals(inspectButton))
			presenter.clickedOnInspectTrace();
		else if (source.equals(exitButton))
			presenter.clickedOnExit();
		else if (source.equals(viewProfile)){
			presenter.clickedOnViewProfile();
		}else if (source.equals(settingsButton)) {
			presenter.clickedOnSettings();
		}
	}
	
	public void setController(JavaTracerPresenter javaTracerPresenter) {
		this.presenter = javaTracerPresenter;
	}
}
