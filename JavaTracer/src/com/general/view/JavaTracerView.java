package com.general.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import com.general.presenter.JavaTracerPresenter;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JavaTracerView extends JFrame implements ActionListener{

	private JavaTracerPresenter presenter; 
	
	private JButton traceProfileButton;
	private JButton inspectButton;
	private JButton exitButton;
	
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
		sl_panel.putConstraint(SpringLayout.NORTH, traceProfileButton, 91, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, traceProfileButton, 143, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, traceProfileButton, -406, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, traceProfileButton, 338, SpringLayout.WEST, panel);
		panel.add(traceProfileButton);
		traceProfileButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		traceProfileButton.addActionListener(this);
		
		inspectButton = new JButton("Inspect trace");
		sl_panel.putConstraint(SpringLayout.NORTH, inspectButton, 90, SpringLayout.SOUTH, traceProfileButton);
		sl_panel.putConstraint(SpringLayout.WEST, inspectButton, 0, SpringLayout.WEST, traceProfileButton);
		sl_panel.putConstraint(SpringLayout.SOUTH, inspectButton, 146, SpringLayout.SOUTH, traceProfileButton);
		sl_panel.putConstraint(SpringLayout.EAST, inspectButton, 0, SpringLayout.EAST, traceProfileButton);
		panel.add(inspectButton);
		inspectButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		inspectButton.addActionListener(this);
		
		exitButton = new JButton("Exit");
		sl_panel.putConstraint(SpringLayout.NORTH, exitButton, 80, SpringLayout.SOUTH, inspectButton);
		sl_panel.putConstraint(SpringLayout.WEST, exitButton, 0, SpringLayout.WEST, traceProfileButton);
		sl_panel.putConstraint(SpringLayout.SOUTH, exitButton, -124, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, exitButton, 0, SpringLayout.EAST, traceProfileButton);
		panel.add(exitButton);
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		exitButton.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(traceProfileButton))
			presenter.clickedOnTraceProfile();
		else if (source.equals(inspectButton))
			presenter.clickedOnInspectTrace();
		else if (source.equals(exitButton))
			presenter.clickedOnExit();
	}
	
	public void setController(JavaTracerPresenter javaTracerPresenter) {
		this.presenter = javaTracerPresenter;
	}
}
