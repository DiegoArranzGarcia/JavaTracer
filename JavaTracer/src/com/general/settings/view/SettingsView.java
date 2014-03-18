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

import com.general.resources.ImageLoader;
import com.general.settings.presenter.SettingsPresenterInterface;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class SettingsView extends JFrame implements ActionListener,MouseListener,DocumentListener, SettingsViewInterface,KeyListener{
	

	private static String WINDOWS_TITLE = "Settings";
	
	private static String ADD = "Add";
	private static String DELETE = "Delete";
	private static String DELETE_ALL = "Delete All";
	private static String SAVE = "Save";
	private static String CANCEL = "Cancel";
	
	private static String ERROR = "Error";
	private static String ERROR_EMPTY_DELETE = "Should select at least one";
	private static String ERROR_EMPTY_ADD = "Should write at least one";
	private static String ERROR_NUMBER = "Should be a number the entered value";
	private static String ERROR_EMPTY_NUM_LEVELS = "You should enter the number of levels";
	private static String ERROR_EMPTY_NUM_NODES = "You should enter the number of nodes";
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
		setResizable(false);
		
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
		
    }



	private void initInspectorView() {
			
			tracerSettings = new Panel();
			tabbedPane.addTab("Tracer", null, tracerSettings, null);
			tracerSettings.addMouseListener(this);
			tracerSettings.setLayout(null);
			tracerSettings.setSize(690, 330);
			tracerSettings.setLayout(null);
			
			excludes = new JTextField();
			excludes.setBounds(39, 46, 390, 22);
			tracerSettings.add(excludes);
			excludes.setColumns(10);
			excludes.getDocument().addDocumentListener(this);
			
			JLabel argumentLabel = new JLabel("Excludes");
			argumentLabel.setBounds(39, 15, 49, 16);
			tracerSettings.add(argumentLabel);
			
			addButton = new JButton(ADD);
			addButton.setBounds(522, 101, 115, 25);
			tracerSettings.add(addButton);
			addButton.addActionListener(this);
			
			deleteButton = new JButton(DELETE);
			deleteButton.setLocation(522, 141);
			deleteButton.setSize(115, 25);
			tracerSettings.add(deleteButton);
			deleteButton.addActionListener(this);
			
			deleteAllButton = new JButton(DELETE_ALL);
			deleteAllButton.setBounds(522, 185, 115, 25);
			tracerSettings.add(deleteAllButton);
			deleteAllButton.addActionListener(this);
			
			cancelButtonTracer = new JButton(CANCEL);
			cancelButtonTracer.setBounds(243, 291, 71, 25);
			tracerSettings.add(cancelButtonTracer);
			cancelButtonTracer.addActionListener(this);
			
			saveButtonTracer = new JButton(SAVE);
			saveButtonTracer.setBounds(110, 291, 61, 25);
			tracerSettings.add(saveButtonTracer);
			saveButtonTracer.addActionListener(this);
			
			downButton = new JButton("");
			downButton.setBounds(366, 242, 63, 25);
			downButton.setIcon(imageLoader.getArrowDownIcon());
			downButton.setEnabled(false);
			tracerSettings.add(downButton);
			downButton.addActionListener(this);
			
			upButton = new JButton("");
			upButton.setBounds(366, 97, 63, 33);
			upButton.setIcon(imageLoader.getArrowUpIcon());
			upButton.setEnabled(false);
			upButton.addActionListener(this);
			tracerSettings.add(upButton);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(39, 97, 321, 170);
			scrollPane.setBackground(Color.WHITE);
			tracerSettings.add(scrollPane);
			
			excludesList = new JList<String>();		
			excludesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			excludesList.setBorder(new LineBorder(new Color(0, 0, 0)));
			scrollPane.setViewportView(excludesList);
			excludesList.setModel(model);
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
			saveButtonInspector.setBounds(61, 230, 87, 23);
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
			excludesList.addMouseListener(this);
			excludesList.addKeyListener(this); 
			
		    
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
	
	/*
	 * Tracer methods
	 */
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
    
    /*
     * Inspector methods
     */
    private static boolean isNumeric(String cadena){
    	try {
    		Integer.parseInt(cadena);
    		return true;
    	} catch (NumberFormatException nfe){
    		return false;
    	}
    }
    public int getNumLevels() {
    	String numLevels = levelsField.getText();
    	boolean isNumber = isNumeric(numLevels);
    	int nLevels = -1;
    	if (numLevels.equals(""))
    		JOptionPane.showMessageDialog(null,ERROR_EMPTY_NUM_LEVELS,ERROR, JOptionPane.ERROR_MESSAGE);
    	else if (isNumber)
    		nLevels =  Integer.parseInt(numLevels);
    	else JOptionPane.showMessageDialog(null,ERROR_NUMBER,ERROR, JOptionPane.ERROR_MESSAGE);
    	
    	return nLevels;
    }
    
    public int getNumNodes() {
    	String numNodes = nodesField.getText();
    	boolean isNumber = isNumeric(numNodes);
    	int nNodes = -1;
    		
    	if (numNodes.equals(""))
    		JOptionPane.showMessageDialog(null,ERROR_EMPTY_NUM_NODES,ERROR, JOptionPane.ERROR_MESSAGE);
    	else if (isNumber)
    		nNodes =  Integer.parseInt(numNodes);
    	else JOptionPane.showMessageDialog(null,ERROR_NUMBER,ERROR, JOptionPane.ERROR_MESSAGE);
    	
    	return nNodes;
    }


    public void loadNumLevels(int numLevels) {
    	levelsField.setText("");
    	String nLevels = Integer.toString(numLevels);
    	levelsField.setText(nLevels);    
    }


    public void loadNumNodes(int numNodes) {
	  nodesField.setText("");
	  String nNodes = Integer.toString(numNodes);
	  nodesField.setText(nNodes);
	    
    }


    public void keyPressed(KeyEvent e) {
    	if (e.getSource().equals(excludesList)) {
    		int index = -1;
    		 if (e.getKeyCode() == KeyEvent.VK_UP) {
    			  index = excludesList.getSelectedIndex()-1;
    		 }else if (e.getKeyCode() == KeyEvent.VK_DOWN){
    			  index = excludesList.getSelectedIndex()+1;
    		 }
    		refreshButtons(index);  
    		
    	}
	    
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
    
}
