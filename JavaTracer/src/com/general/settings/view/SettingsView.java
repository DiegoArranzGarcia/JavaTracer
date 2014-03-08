/**
 * 
 */
package com.general.settings.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.general.imageresources.ImageLoader;
import com.general.settings.presenter.SettingsPresenterInterface;

@SuppressWarnings("serial")
public class SettingsView extends JFrame implements ActionListener,MouseListener,DocumentListener, SettingsViewInterface{
	

	private static String WINDOWS_TITLE = "Settings";
	
	private static String ADD = "Add";
	private static String DELETE = "Delete";
	private static String DELETE_ALL = "Delete All";
	private static String SAVE = "Save";
	private static String CANCEL = "Cancel";
	
	private static String ERROR = "Error";
	private static String ERROR_EMPTY_DELETE = "Should select at least one";
	private static String ERROR_EMPTY_ADD = "Should write at least one";
	
	/*
	 * General view
	 */
	private ImageLoader imageLoader;
	private JTabbedPane tabbedPane;
	private Panel tracerSettings,inspectorSettings;
	private SettingsPresenterInterface presenter;
	
	/*
	 * Tracer view
	 */
	private JTextField excludes;
	private DefaultListModel<String> model;
	private JButton addButton,deleteButton,deleteAllButton,saveButtonTracer,cancelButtonTracer,upButton,downButton;
	private JList<String> excludesList;
	
	
	/*
	 * Inspector view
	 */
	private JTextField levelsField,nodesField;
	private JLabel levelsLabel, nodesLabel;
	private JButton saveButtonInspector,cancelButtonInspector,deleteAllButtonInspector;
	
	public SettingsView() {
		
		initGeneralView(); 
		initTracerSettings();
		initInspectorView();	
		
	}


	private void initGeneralView() {
    	setTitle(WINDOWS_TITLE);
		setMinimumSize(new Dimension(660, 330));
		setSize(new Dimension(692, 398));
		setLocationRelativeTo(null);	
		imageLoader = ImageLoader.getInstance();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    	    // System.exit(0);
	    		presenter.closeWindow();
	    	   }
		});
    }
	
private void initTracerSettings() {
    	
    	model = new DefaultListModel<String>();
		
		tracerSettings = new Panel();
		tabbedPane.addTab("Tracer", null, tracerSettings, null);
		tracerSettings.addMouseListener(this);
		tracerSettings.setLayout(null);
		tracerSettings.setSize(690, 330); 
		
		SpringLayout sl_contentPane = new SpringLayout();
		tracerSettings.setLayout(sl_contentPane);
		
		excludes = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, excludes, 39, SpringLayout.WEST, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.EAST, excludes, -244, SpringLayout.EAST, tracerSettings);
		tracerSettings.add(excludes);
		excludes.setColumns(10);
		excludes.getDocument().addDocumentListener(this);
		
		JLabel argumentLabel = new JLabel("Excludes");
		sl_contentPane.putConstraint(SpringLayout.NORTH, excludes, 20, SpringLayout.SOUTH, argumentLabel);
		sl_contentPane.putConstraint(SpringLayout.NORTH, argumentLabel, 20, SpringLayout.NORTH, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.WEST, argumentLabel, 39, SpringLayout.WEST, tracerSettings);
		tracerSettings.add(argumentLabel);
		
		addButton = new JButton(ADD);
		sl_contentPane.putConstraint(SpringLayout.NORTH, addButton, 107, SpringLayout.NORTH, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.EAST, addButton, -31, SpringLayout.EAST, tracerSettings);
		tracerSettings.add(addButton);
		addButton.addActionListener(this);
		
		deleteButton = new JButton(DELETE);
		sl_contentPane.putConstraint(SpringLayout.NORTH, deleteButton, 17, SpringLayout.SOUTH, addButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, deleteButton, 0, SpringLayout.WEST, addButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, deleteButton, -31, SpringLayout.EAST, tracerSettings);
		tracerSettings.add(deleteButton);
		deleteButton.addActionListener(this);
		
		deleteAllButton = new JButton(DELETE_ALL);
		sl_contentPane.putConstraint(SpringLayout.NORTH, deleteAllButton, 189, SpringLayout.NORTH, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, deleteButton, -15, SpringLayout.NORTH, deleteAllButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, deleteAllButton, 0, SpringLayout.WEST, addButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, deleteAllButton, -31, SpringLayout.EAST, tracerSettings);
		tracerSettings.add(deleteAllButton);
		deleteAllButton.addActionListener(this);
		
		cancelButtonTracer = new JButton(CANCEL);
		tracerSettings.add(cancelButtonTracer);
		cancelButtonTracer.addActionListener(this);
		
		saveButtonTracer = new JButton(SAVE);
		sl_contentPane.putConstraint(SpringLayout.NORTH, cancelButtonTracer, 0, SpringLayout.NORTH, saveButtonTracer);
		sl_contentPane.putConstraint(SpringLayout.WEST, cancelButtonTracer, 92, SpringLayout.EAST, saveButtonTracer);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, saveButtonTracer, -10, SpringLayout.SOUTH, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.WEST, saveButtonTracer, 97, SpringLayout.WEST, tracerSettings);
		tracerSettings.add(saveButtonTracer);
		saveButtonTracer.addActionListener(this);
		
		downButton = new JButton("");
		sl_contentPane.putConstraint(SpringLayout.WEST, downButton, 367, SpringLayout.WEST, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, downButton, -59, SpringLayout.SOUTH, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.EAST, downButton, -94, SpringLayout.WEST, deleteAllButton);
		downButton.setIcon(imageLoader.getArrowDownIcon());
		downButton.setEnabled(false);
		tracerSettings.add(downButton);
		downButton.addActionListener(this);
		
		upButton = new JButton("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, downButton, 114, SpringLayout.SOUTH, upButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, addButton, 98, SpringLayout.EAST, upButton);
		sl_contentPane.putConstraint(SpringLayout.NORTH, upButton, 31, SpringLayout.SOUTH, excludes);
		sl_contentPane.putConstraint(SpringLayout.WEST, upButton, 367, SpringLayout.WEST, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, upButton, -198, SpringLayout.SOUTH, tracerSettings);
		sl_contentPane.putConstraint(SpringLayout.EAST, upButton, 0, SpringLayout.EAST, excludes);
		upButton.setIcon(imageLoader.getArrowUpIcon());
		upButton.setEnabled(false);
		upButton.addActionListener(this);
		tracerSettings.add(upButton);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, addButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, excludes);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, downButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPane, -6, SpringLayout.WEST, downButton);
		scrollPane.setBackground(Color.WHITE);
		tracerSettings.add(scrollPane);
		
		excludesList = new JList<String>();
		excludesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		excludesList.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setViewportView(excludesList);
		excludesList.setModel(model);
		excludesList.addMouseListener(this);
	    
    }
	
	 private void initInspectorView() {
		    inspectorSettings= new Panel();
			tabbedPane.addTab("Inspector", null, inspectorSettings, null);
			inspectorSettings.addMouseListener(this);
			inspectorSettings.setLayout(null);
			inspectorSettings.setSize(660, 330);
			
			levelsLabel = new JLabel("Number of levels");
			levelsLabel.setBounds(49, 54, 124, 14);
			inspectorSettings.add(levelsLabel);
			
			nodesLabel = new JLabel("Number of nodes");
			nodesLabel.setBounds(49, 98, 124, 14);
			inspectorSettings.add(nodesLabel);
			
			levelsField = new JTextField();
			levelsField.setBounds(205, 51, 70, 20);
			inspectorSettings.add(levelsField);
			levelsField.setColumns(10);
			
			nodesField = new JTextField();
			nodesField.setColumns(10);
			nodesField.setBounds(205, 95, 70, 20);
			inspectorSettings.add(nodesField);
			
			saveButtonInspector = new JButton("Save");
			saveButtonInspector.setBounds(61, 230, 57, 23);
			saveButtonInspector.addActionListener(this); 
			inspectorSettings.add(saveButtonInspector);
			
			cancelButtonInspector = new JButton("Cancel");
			cancelButtonInspector.setBounds(210, 230, 65, 23);
			cancelButtonInspector.addActionListener(this); 
			inspectorSettings.add(cancelButtonInspector);
			
			deleteAllButtonInspector = new JButton("Delete All");
			deleteAllButtonInspector.setBounds(415, 50, 115, 23);
			deleteAllButtonInspector.addActionListener(this); 
			inspectorSettings.add(deleteAllButtonInspector);
		    
	    }
	 /*
	  * Tracer/Inspector
	  */
	 public void setPresenter(SettingsPresenterInterface presenter) {
		    this.presenter = presenter;	    
	 }
	 
	 /*
	  * General events
	  */
	 public void mouseEntered(MouseEvent e) {}
	 public void mouseExited(MouseEvent e) {}
	 public void mousePressed(MouseEvent e) {}
	 public void mouseReleased(MouseEvent e) {}

	 public void actionPerformed(ActionEvent event) {
			Object source = event.getSource();
			if (source == addButton){
				if (!excludes.getText().equals("")) {
					String text = excludes.getText();
					addExclude(text);
				}else {
					JOptionPane.showMessageDialog(null,ERROR_EMPTY_ADD,ERROR, JOptionPane.ERROR_MESSAGE);	
				}			
			}		
			
			if (source == deleteButton)	{
				deleteExclude(excludesList.getSelectedIndex());
			}
			
			if (source == deleteAllButton)	{
				deleteAllExcludes();
			}
			
			if(source == saveButtonTracer) {
				clickedOnSaveTracer();
			}
			
			if (source == cancelButtonTracer){
				clickedOnCancelTracer();
			}
			
			if (source == saveButtonInspector) {
				clickedOnSaveInspector();
			}
			
			if (source == cancelButtonInspector) {
				clickedOnCancelInspector();
			}
			
			if (source == upButton){
				clickedOnUpButton();
			}
			
			if (source == deleteAllButtonInspector) {
				clickedOnDeleteAllInspector();
			}
			if (source == downButton){
				clickedOnDownButton();
			}
	}
	 
   

	/*
	 *  Tracer Events
	 */

 	private void clickedOnSaveTracer() {
		presenter.saveActionTracer();
	}
	
	private void clickedOnCancelTracer() {
		presenter.cancelActionTracer();
	}
	
	private void clickedOnDownButton() {
		int index = excludesList.getSelectedIndex();
		String argument1 = model.get(index);
		String argument2 = model.get(index+1);
		model.set(index,argument2);
		model.set(index+1,argument1);
		excludesList.setSelectedIndex(index+1);
		refreshButtons(index+1);
	}

	private void clickedOnUpButton() {
		int index = excludesList.getSelectedIndex();
		String argument1 = model.get(index);
		String argument2 = model.get(index-1);
		model.set(index,argument2);
		model.set(index-1,argument1);		
		excludesList.setSelectedIndex(index-1);
		refreshButtons(index-1);
	}

	
	private void updateText() {
    	int index = excludesList.getSelectedIndex();
		if (index != -1)
			model.set(index, excludes.getText());
	}

	private void addExclude(String exclude_text) { 
		model.addElement(exclude_text);
		excludesList.setModel(model);
		excludes.setText("");
	}
	
	private void deleteExclude(int index) {
		if (index>=0) {
			model.removeElementAt(index);	
			excludes.setText(""); 
		}else{
			JOptionPane.showMessageDialog(null,ERROR_EMPTY_DELETE,ERROR, JOptionPane.ERROR_MESSAGE);
		}
		refreshButtons(-1);
	}
	
	private void deleteAllExcludes() {
		model.clear();
		excludes.setText(""); 
		refreshButtons(-1);
	}
	
	// Listener methods
	
	public void changedUpdate(DocumentEvent e) {}
	
	public void insertUpdate(DocumentEvent e) {
		updateText();
	}
	
	public void removeUpdate(DocumentEvent e) {
		updateText();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(excludesList)) {
    		int index = excludesList.getSelectedIndex();
    		if (index != -1)
    			excludes.setText(model.getElementAt(index));
    		refreshButtons(index);
		} else if (e.getSource().equals(tracerSettings)){
			excludesList.clearSelection();
			excludes.setText("");
			refreshButtons(-1);
		}
	}
	
	private void refreshButtons(int index) {
		if (index != -1){
			if (index == 0)
				upButton.setEnabled(false);
			else 
				upButton.setEnabled(true);
			
			if (index == model.getSize()-1)
				downButton.setEnabled(false);
			else 
				downButton.setEnabled(true);
		} else {
			downButton.setEnabled(false);
			upButton.setEnabled(false);
		}
	}

    public void loadExcludes(String[] args) {
    	deleteAllExcludes();
		for (int i=0;i<args.length;i++){
			addExclude(args[i]);
		}    
    }

    public String[] getExcludes() {
    	int size=excludesList.getModel().getSize();
		String[] excludes = new  String[size];
    	for (int i=0;i<size;i++) {
    		excludes[i] = excludesList.getModel().getElementAt(i).toString();
    	}
	    return excludes;
    }


    /*
     * Inspector events
     */
    private void clickedOnCancelInspector() {
    	presenter.cancelActionInspector();
    }
    
    private void clickedOnSaveInspector() {
		presenter.saveActionInspector();
	}

    private void clickedOnDeleteAllInspector() {
	    nodesField.setText("");
	    levelsField.setText(""); 
	    
    }
}
