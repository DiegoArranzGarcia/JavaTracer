/**
 * 
 */
package com.general.settings.view;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.alee.laf.combobox.WebComboBox;
import com.general.model.configuration.JavaTracerConfiguration;
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
	private JButton restoreDefaults,saveButton,cancelButton;
	private WebComboBox configuration; 

	/*
	 * Tracer view
	 */
	private JTextField excludes;
	private DefaultListModel<String> model;
	private JButton addButton,deleteButton,deleteAllButton,upButton,downButton;
	private JList<String> excludesList;



	/*
	 * Inspector view
	 */
	private JTextField levelsField,nodesField;
	private JLabel levelsLabel, nodesLabel;
	private JPanel _panel;
	private JPanel _panel_1;
	private JCheckBox excludedThis,excludedLibraries,excludeJavaDataStructures;

	private JPanel _panel_2;
	private JCheckBox unlimitedLevels,unlimitedNodes;
	private JScrollPane _scrollPane_1;

	public SettingsView() {
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);

		initGeneralView(); 
		initTracerSettings();
		initInspectorView();	

	}


	private void initGeneralView() {
		setTitle(WINDOWS_TITLE);
		setMinimumSize(new Dimension(600, 330));
		setSize(new Dimension(636, 530));
		setLocationRelativeTo(null);	
		imageLoader = ImageLoader.getInstance();
		getContentPane().setLayout(null);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBounds(10, 103, 607, 336);
		getContentPane().add(tabbedPane);

		saveButton = new JButton(SAVE);
		saveButton.setBounds(165, 450, 115, 32);
		getContentPane().add(saveButton);

		cancelButton = new JButton(CANCEL);
		cancelButton.setBounds(476, 450, 115, 32);
		getContentPane().add(cancelButton);

		_panel = new JPanel();
		_panel.setBounds(10, 13, 608, 75);
		getContentPane().add(_panel);
		_panel.setOpaque(false);
		_panel.setBorder(new CompoundBorder(null, new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null)));
		_panel.setBackground(Color.WHITE);
		_panel.setLayout(null);

		configuration = new WebComboBox();
		configuration.setOpaque(false);
		configuration.setEditable(true);
		configuration.setBackground(Color.WHITE);
		configuration.setBounds(10, 30, 404, 34);
		configuration.addActionListener(this); 
		_panel.add(configuration);

		restoreDefaults = new JButton("Restore Defaults");
		restoreDefaults.addActionListener(this);
		restoreDefaults.setBounds(292, 450, 172, 32);
		restoreDefaults.addActionListener(this); 
		getContentPane().add(restoreDefaults);

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

	private void initInspectorView() {

		tracerSettings = new Panel();
		tracerSettings.setBackground(Color.WHITE);
		tabbedPane.addTab("Tracer", null, tracerSettings, null);
		tracerSettings.addMouseListener(this);
		tracerSettings.setLayout(null);
		tracerSettings.setSize(690, 330);
		tracerSettings.setLayout(null);

		_panel_1 = new JPanel();
		_panel_1.setBorder(new TitledBorder(null, "Excluded", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_panel_1.setBackground(new Color(255, 255, 255));
		_panel_1.setBounds(12, 13, 578, 284);
		tracerSettings.add(_panel_1);
		_panel_1.setLayout(null);

		excludedThis = new JCheckBox("Exclude this");
		excludedThis.setBackground(Color.WHITE);
		excludedThis.setToolTipText("It is recommended checking this box, in the case of very large traces");
		excludedThis.setBounds(17, 224, 135, 23);
		_panel_1.add(excludedThis);

		excludes = new JTextField();
		excludes.setBounds(17, 27, 385, 30);
		_panel_1.add(excludes);
		excludes.setColumns(10);

		addButton = new JButton(ADD);
		addButton.setBounds(451, 70, 115, 30);
		_panel_1.add(addButton);

		deleteButton = new JButton(DELETE);
		deleteButton.setBounds(451, 109, 115, 30);
		_panel_1.add(deleteButton);

		deleteAllButton = new JButton(DELETE_ALL);
		deleteAllButton.setBounds(451, 145, 115, 30);
		_panel_1.add(deleteAllButton);

		downButton = new JButton("");
		downButton.setBounds(359, 190, 43, 25);
		_panel_1.add(downButton);
		downButton.setIcon(imageLoader.getArrowDownIcon());
		downButton.setEnabled(false);

		upButton = new JButton("");
		upButton.setBounds(359, 70, 43, 25);
		_panel_1.add(upButton);
		upButton.setIcon(imageLoader.getArrowUpIcon());
		upButton.setEnabled(false);

		excludeJavaDataStructures = new JCheckBox("Exclude java data structures");
		excludeJavaDataStructures.setToolTipText("It is recommended checking this box, in the case of very large traces");
		excludeJavaDataStructures.setBackground(Color.WHITE);
		excludeJavaDataStructures.setBounds(17, 250, 249, 23);
		_panel_1.add(excludeJavaDataStructures);
		
		_scrollPane_1 = new JScrollPane();
		_scrollPane_1.setBounds(17, 71, 330, 146);
		_panel_1.add(_scrollPane_1);
		
		excludesList = new JList<String>();
		_scrollPane_1.setViewportView(excludesList);
		excludesList.setBackground(Color.WHITE);
		excludesList.setToolTipText("");
		
		excludesList.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		excludesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		excludesList.setModel(model);
		
		excludedLibraries = new JCheckBox("Exclude libraries");
		excludedLibraries.setSelected(true);
		excludedLibraries.setBackground(Color.WHITE);
		excludedLibraries.setBounds(200, 224, 147, 23);
		_panel_1.add(excludedLibraries);
		
		
		excludesList.addMouseListener(this);
		excludesList.addKeyListener(this); 
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		deleteAllButton.addActionListener(this);
		deleteButton.addActionListener(this);
		addButton.addActionListener(this);
		excludes.getDocument().addDocumentListener(this);
		inspectorSettings= new Panel();
		inspectorSettings.setBackground(Color.WHITE);
		tabbedPane.addTab("Display tree", null, inspectorSettings, null);
		inspectorSettings.addMouseListener(this);
		inspectorSettings.setLayout(null);
		inspectorSettings.setSize(660, 330);

		_panel_2 = new JPanel();
		_panel_2.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Tree ", TitledBorder.LEADING, TitledBorder.TOP, null, null), null));
		_panel_2.setBackground(Color.WHITE);
		_panel_2.setBounds(12, 13, 578, 289);
		inspectorSettings.add(_panel_2);
		_panel_2.setLayout(null);

		nodesLabel = new JLabel("Number of tree nodes");
		nodesLabel.setBounds(25, 47, 156, 16);
		_panel_2.add(nodesLabel);

		levelsLabel = new JLabel("Number of tree levels");
		levelsLabel.setBounds(25, 99, 156, 14);
		_panel_2.add(levelsLabel);

		levelsField = new JTextField();
		levelsField.setBounds(193, 94, 77, 25);
		_panel_2.add(levelsField);
		levelsField.setColumns(10);

		nodesField = new JTextField();
		nodesField.setBounds(193, 43, 77, 25);
		_panel_2.add(nodesField);
		nodesField.setColumns(10);

		unlimitedLevels = new JCheckBox("Unlimited");
		unlimitedLevels.setBounds(291, 95, 97, 23);
		_panel_2.add(unlimitedLevels);
		unlimitedLevels.setBackground(Color.WHITE);

		unlimitedNodes = new JCheckBox("Unlimited");
		unlimitedNodes.setBackground(Color.WHITE);
		unlimitedNodes.setBounds(291, 43, 97, 23);
		_panel_2.add(unlimitedNodes);


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
			clickedOnAdd();
		}		
		
		if (source == configuration) {
			System.out.println("cambia");
			changeConfiguration();
		}
		
		if (source == restoreDefaults) {
			clickedOnRestoreDefaults();
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
		if (source == downButton){
			clickedOnDownButton();
		}
	}

   
    private void changeConfiguration() {
    	presenter.loadConfiguration(configuration.getSelectedItem().toString());
	    
    }


	private void clickedOnRestoreDefaults() {
    	restoreTracerConfiguration();
    	restoreDisplayTreeConfiguration();
	    
    }

    
    private void restoreDisplayTreeConfiguration() {
	   loadNumLevels(JavaTracerConfiguration.DEFAULT_NUM_LEVELS);
	   loadNumNodes(JavaTracerConfiguration.DEFAULT_NUM_NODES);
	   loadUnlimitedLevels(false);
	   loadUnlimitedNodes(false); 
    }


	private void restoreTracerConfiguration() {
    	deleteAllExcludes();
    	restoreExcluded();
    	restoreExcludedThis();
    	restoreExcludedLibraries();
    	restoreExcludedDataStructure();	    
    }


	private void restoreExcludedDataStructure() {
		loadExcludedDataStructure(false);    
    }


	private void restoreExcludedThis() {
		loadExcludedThis(false);  
    }
	
	private void restoreExcludedLibraries() {
		loadExcludedLibraries(true);
    }
	

	private void restoreExcluded() {
	    JavaTracerConfiguration configuration = JavaTracerConfiguration.getInstance();
	    List<String> defaultExcluded = configuration.getDefaultExcluded();
	    for (int i=0;i<defaultExcluded.size();i++) {
	    	addOnlyExclude(defaultExcluded.get(i));
	    }
	    
    }


	private void clickedOnAdd() {
		if (!excludes.getText().equals("")) {
			String text = excludes.getText();
			addExclude(text);
		}else {
			JOptionPane.showMessageDialog(null,ERROR_EMPTY_ADD,ERROR, JOptionPane.ERROR_MESSAGE);	
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

	public void addOnlyExclude(String exclude) {
		model.addElement(exclude);
		excludesList.setModel(model);
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
	
	public void loadExcludedLibraries(boolean excludedLibraries) {
		this.excludedLibraries.setSelected(excludedLibraries);
	}

	public void loadExcludedDataStructure(boolean excludeDataStructure) {
		this.excludeJavaDataStructures.setSelected(excludeDataStructure);
	}

	public void loadUnlimitedLevels(boolean unlimited) {
		this.unlimitedLevels.setSelected(unlimited);
	}

	public void loadUnlimitedNodes(boolean unlimited) {
		this.unlimitedNodes.setSelected(unlimited);
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

	public boolean getExcludedLibraries() {
		return excludedLibraries.isSelected();
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

	 public boolean getUnlimitedLevels() {
		 return unlimitedLevels.isSelected();
	 }

	 public boolean getUnlimitedNodes() {
		 return unlimitedNodes.isSelected();
	 }

	 public boolean getExcludedDataStructure() {
		 return excludeJavaDataStructures.isSelected();
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
				 int selected =  excludesList.getSelectedIndex();
				 if (selected == 0)
					 index = 0;
				 else
					 index =selected-1;
				
			 }else if (e.getKeyCode() == KeyEvent.VK_DOWN){
				 int selected = excludesList.getSelectedIndex()+1;
				 int maxList = excludesList.getModel().getSize()-1;
				 if (selected >= maxList) {
					 index = excludesList.getModel().getSize()-1;
				 } else index = selected;
			 }
			 refreshButtons(index);  

		 }

	 }

	 public void keyReleased(KeyEvent e) {}

	 public void keyTyped(KeyEvent e) {}
	
	 
	 public void loadAllXmls(List<String>xmlNames) {
		 
		 for (int i=0;i<xmlNames.size();i++) {
			 configuration.addItem(xmlNames.get(i));
		 }
		 
	 }
}
