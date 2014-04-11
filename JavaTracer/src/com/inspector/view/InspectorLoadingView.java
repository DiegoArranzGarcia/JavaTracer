package com.inspector.view;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.alee.laf.progressbar.WebProgressBar;
import com.general.settings.model.Settings;

@SuppressWarnings("serial")
public class InspectorLoadingView extends JDialog{
	private WebProgressBar progressBar;
	private JLabel infoLabel;
	private Settings config;
	
	public InspectorLoadingView(InspectorView view) {
		super(view,true);
		
		this.config = Settings.getInstance();
		getContentPane().setLayout(null);
		
		progressBar = new WebProgressBar(0,100);
		progressBar.setBounds(40, 72, 544, 31);
		progressBar.setHighlightWhite(Color.BLUE);
		progressBar.setHighlightDarkWhite(Color.BLUE);
		progressBar.setProgressTopColor(Color.CYAN);
		progressBar.setValue(0);
		progressBar.setProgressBottomColor(Color.BLUE);
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		
		progressBar.setVisible(!config.isUnlimitedNodes());
		
		getContentPane().add(progressBar);
		
		infoLabel = new JLabel("Opening ...");
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setBounds(204, 30, 223, 16);
		getContentPane().add(infoLabel);
		setTitle("Please wait");
		
		setSize(650,170);
		setResizable(false);
		setLocationRelativeTo(null);
		
	}

	public void setPercentage(int value) {
		progressBar.setValue(value);
	}

	public void updateInfo(int numNodes) {
		
		if (config.isUnlimitedNodes()){
			infoLabel.setText("Loaded " + numNodes + " nodes"); 
		} else {
			infoLabel.setText("Loading... " + numNodes + "/" + config.getNumNodes()); 
		}
		
	}

	public void opening() {
		infoLabel.setText("Opening...");
	}

}
