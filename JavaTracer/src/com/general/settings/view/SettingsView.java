/**
 * 
 */
package com.general.settings.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.general.resources.ImageLoader;
import com.general.settings.presenter.SettingsPresenterInterface;

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
	private JButton addButton,deleteButton,deleteAllButton,saveButton,cancelButton,upButton,downButton;
	private JList<String> excludesList;
	
	
	
	/*
	 * Inspector view
	 */
	private JTextField levelsField,nodesField;
	private JLabel levelsLabel, nodesLabel;
	private JButton deleteAllButtonInspector;
	private JPanel _panel;
	private JPanel _panel_1;
	private JCheckBox excludedThis;
	private JPanel _panel_2;
	private JPanel _panel_3;
	@SuppressWarnings("rawtypes")
    private JComboBox _comboBox;
	private JButton _button;
	private JCheckBox unlimited;
	
	
	public SettingsView() {
		setResizable(false);
		
		initGeneralView(); 
		initTracerSettings();
		initInspectorView();	
		
	}


	private void initGeneralView() {
    	setTitle(WINDOWS_TITLE);
		setMinimumSize(new Dimension(600, 330));
		setSize(new Dimension(636, 515));
		setLocationRelativeTo(null);	
		imageLoader = ImageLoader.getInstance();
		getContentPane().setLayout(null);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 607, 429);
		getContentPane().add(tabbedPane);
		
		saveButton = new JButton(SAVE);
		saveButton.setBounds(402, 450, 90, 25);
		getContentPane().add(saveButton);
		
		cancelButton = new JButton(CANCEL);
		cancelButton.setBounds(502, 450, 90, 25);
		getContentPane().add(cancelButton);
		cancelButton.addActionListener(this);
		saveButton.addActionListener(this);
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



	@SuppressWarnings("rawtypes")
    private void initInspectorView() {
			
			tracerSettings = new Panel();
			tracerSettings.setBackground(Color.WHITE);
			tabbedPane.addTab("Tracer", null, tracerSettings, null);
			tracerSettings.addMouseListener(this);
			tracerSettings.setLayout(null);
			tracerSettings.setSize(690, 330);
			tracerSettings.setLayout(null);
			
			excludes = new JTextField();
			excludes.setBounds(20, 140, 385, 30);
			tracerSettings.add(excludes);
			excludes.setColumns(10);
			excludes.getDocument().addDocumentListener(this);
			
			addButton = new JButton(ADD);
			addButton.setBounds(459, 148, 115, 25);
			tracerSettings.add(addButton);
			addButton.addActionListener(this);
			
			deleteButton = new JButton(DELETE);
			deleteButton.setLocation(459, 188);
			deleteButton.setSize(115, 25);
			tracerSettings.add(deleteButton);
			deleteButton.addActionListener(this);
			
			deleteAllButton = new JButton(DELETE_ALL);
			deleteAllButton.setBounds(459, 232, 115, 25);
			tracerSettings.add(deleteAllButton);
			deleteAllButton.addActionListener(this);
			
			downButton = new JButton("");
			downButton.setBounds(362, 321, 43, 25);
			downButton.setIcon(imageLoader.getArrowDownIcon());
			downButton.setEnabled(false);
			tracerSettings.add(downButton);
			downButton.addActionListener(this);
			
			upButton = new JButton("");
			upButton.setBounds(362, 185, 43, 25);
			upButton.setIcon(imageLoader.getArrowUpIcon());
			upButton.setEnabled(false);
			upButton.addActionListener(this);
			tracerSettings.add(upButton);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(20, 181, 321, 165);
			scrollPane.setBackground(Color.WHITE);
			tracerSettings.add(scrollPane);
			
			excludesList = new JList<String>();
			excludesList.setToolTipText("");
			scrollPane.setViewportView(excludesList);
			excludesList.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			excludesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			excludesList.setModel(model);
			
			_panel = new JPanel();
			_panel.setOpaque(false);
			_panel.setBorder(new CompoundBorder(null, new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null)));
			_panel.setBackground(new Color(255, 255, 255));
			_panel.setBounds(10, 11, 578, 75);
			tracerSettings.add(_panel);
			_panel.setLayout(null);
			
			JComboBox comboBox = new JComboBox();
			comboBox.setOpaque(false);
			comboBox.setEditable(true);
			comboBox.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			comboBox.setBackground(Color.WHITE);
			comboBox.setBounds(10, 30, 230, 23);
			_panel.add(comboBox);
			
			JButton btnSaveConfiguration = new JButton("Save");
			btnSaveConfiguration.setBounds(270, 29, 124, 25);
			_panel.add(btnSaveConfiguration);
			
			_panel_1 = new JPanel();
			_panel_1.setBorder(new TitledBorder(null, "Excluded", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			_panel_1.setBackground(new Color(255, 255, 255));
			_panel_1.setBounds(10, 108, 578, 282);
			tracerSettings.add(_panel_1);
			_panel_1.setLayout(null);
			
			excludedThis = new JCheckBox("Excluded this");
			excludedThis.setToolTipText("It is recommended checking this box, in the case of very large traces");
			excludedThis.setBounds(17, 252, 137, 23);
			_panel_1.add(excludedThis);
			excludesList.addMouseListener(this);
			excludesList.addKeyListener(this); 
			inspectorSettings= new Panel();
			inspectorSettings.setBackground(Color.WHITE);
			tabbedPane.addTab("Display tree", null, inspectorSettings, null);
			inspectorSettings.addMouseListener(this);
			inspectorSettings.setLayout(null);
			inspectorSettings.setSize(660, 330);
			
			levelsLabel = new JLabel("Number of tree levels");
			levelsLabel.setBounds(28, 249, 156, 14);
			inspectorSettings.add(levelsLabel);
			
			nodesLabel = new JLabel("Number of tree nodes");
			nodesLabel.setBounds(28, 181, 156, 14);
			inspectorSettings.add(nodesLabel);
			
			levelsField = new JTextField();
			levelsField.setBounds(205, 176, 50, 25);
			inspectorSettings.add(levelsField);
			levelsField.setColumns(10);
			
			nodesField = new JTextField();
			nodesField.setColumns(10);
			nodesField.setBounds(205, 244, 50, 25);
			inspectorSettings.add(nodesField);
			
			deleteAllButtonInspector = new JButton("Delete All");
			deleteAllButtonInspector.setBounds(446, 177, 115, 23);
			deleteAllButtonInspector.addActionListener(this); 
			inspectorSettings.add(deleteAllButtonInspector);
			
			unlimited = new JCheckBox("Unlimited");
			unlimited.setBackground(Color.WHITE);
			unlimited.setBounds(295, 177, 97, 23);
			inspectorSettings.add(unlimited);
			
			_panel_2 = new JPanel();
			_panel_2.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Tree ", TitledBorder.LEADING, TitledBorder.TOP, null, null), null));
			_panel_2.setBackground(Color.WHITE);
			_panel_2.setBounds(10, 108, 578, 282);
			inspectorSettings.add(_panel_2);
			
			_panel_3 = new JPanel();
			_panel_3.setLayout(null);
			_panel_3.setOpaque(false);
			_panel_3.setBorder(new CompoundBorder(null, new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null)));
			_panel_3.setBackground(Color.WHITE);
			_panel_3.setBounds(10, 22, 578, 75);
			inspectorSettings.add(_panel_3);
			
			_comboBox = new JComboBox();
			_comboBox.setOpaque(false);
			_comboBox.setEditable(true);
			_comboBox.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			_comboBox.setBackground(Color.WHITE);
			_comboBox.setBounds(10, 30, 230, 23);
			_panel_3.add(_comboBox);
			
			_button = new JButton("Save");
			_button.setBounds(270, 29, 91, 25);
			_panel_3.add(_button);
			
		    
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
			
			if(source == saveButton) {
				clickedOnSave();
			}
			
			if (source == cancelButton){
				clickedOnCancel();
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

 	private void clickedOnSave() {
		presenter.save();
	}
	
	private void clickedOnCancel() {
		presenter.cancelAction();
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
    
    public void loadExcludedThis(boolean excludedThis) {
    	this.excludedThis.setSelected(excludedThis);
    }
    
    public void loadUnlimited(boolean unlimited) {
    	this.unlimited.setSelected(unlimited);
    }

    public String[] getExcludes() {
    	int size=excludesList.getModel().getSize();
		String[] excludes = new  String[size];
    	for (int i=0;i<size;i++) {
    		excludes[i] = excludesList.getModel().getElementAt(i).toString();
    	}
	    return excludes;
    }

    public boolean getExcludedThis() {
    	return excludedThis.isSelected();
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

    public boolean getUnlimited() {
    	return unlimited.isSelected();
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
