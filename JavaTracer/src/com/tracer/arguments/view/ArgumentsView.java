package com.tracer.arguments.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.general.resources.ImageLoader;
import com.tracer.arguments.presenter.ArgumentsPresenterInterface;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class ArgumentsView extends JFrame implements ActionListener,MouseListener,DocumentListener,ArgumentsViewInterface,KeyListener{

	private static String WINDOWS_TITLE = "Add main arguments";
	
	private static String ADD = "Add";
	private static String DELETE = "Delete";
	private static String DELETE_ALL = "Delete All";
	private static String SAVE = "Save";
	private static String CANCEL = "Cancel";
	
	private static String ERROR = "Error";
	private static String ERROR_EMPTY_DELETE = "You must select an argument";
	private static String ERROR_EMPTY_ADD = "You must write an argument";
	
	private JPanel contentPane;
	private JTextField argument;
	private DefaultListModel<String> model;
	private JButton addButton,deleteButton,deleteAllButton,saveButton,cancelButton,upButton,downButton;
	
	private ArgumentsPresenterInterface presenter;
	private JList<String> argumentsList;
	private JScrollPane _scrollPane;

	/**
	 * Create the frame.
	 */
	public ArgumentsView() {
		setMaximumSize(new Dimension(2147483590, 2147483647));
		setTitle(WINDOWS_TITLE);
		setMinimumSize(new Dimension(500, 330));
		setSize(new Dimension(607, 345));
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
	    addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    		presenter.closeWindow();
	    	   }
		});

		ImageLoader imageLoader = ImageLoader.getInstance();
		
		model = new DefaultListModel<String>();
		
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.addMouseListener(this);
		contentPane.setLayout(null);

		argument = new JTextField();
		argument.setBounds(30, 59, 357, 25);
		contentPane.add(argument);
		argument.setColumns(10);
		argument.getDocument().addDocumentListener(this);
		
		cancelButton = new JButton(CANCEL);
		cancelButton.setBounds(292, 260, 100, 35);
		contentPane.add(cancelButton);
		cancelButton.addActionListener(this);
		
		saveButton = new JButton(SAVE);
		saveButton.setBackground(Color.WHITE);
		saveButton.setBounds(147, 260, 100, 35);
		contentPane.add(saveButton);
		saveButton.addActionListener(this);
		
		downButton = new JButton("");
		downButton.setBounds(362, 202, 27, 27);
		downButton.setIcon(imageLoader.getArrowDownIcon());
		downButton.setEnabled(false);
		contentPane.add(downButton);
		downButton.addActionListener(this);
		
		upButton = new JButton("");
		upButton.setBounds(362, 114, 27, 27);
		upButton.setIcon(imageLoader.getArrowUpIcon());
		upButton.setEnabled(false);
		upButton.addActionListener(this);
		contentPane.add(upButton);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(null, "Arguments", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 27, 571, 222);
		contentPane.add(panel);
		panel.setLayout(null);
		
		addButton = new JButton(ADD);
		addButton.setBounds(432, 30, 115, 28);
		panel.add(addButton);
		
		deleteButton = new JButton(DELETE);
		deleteButton.setBounds(432, 69, 115, 28);
		panel.add(deleteButton);
		
		deleteAllButton = new JButton(DELETE_ALL);
		deleteAllButton.setBounds(432, 108, 115, 28);
		panel.add(deleteAllButton);
		
		_scrollPane = new JScrollPane();
		_scrollPane.setBounds(23, 90, 320, 113);
		panel.add(_scrollPane);
		
		argumentsList = new JList<String>();
		_scrollPane.setViewportView(argumentsList);
		argumentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		argumentsList.setBorder(new LineBorder(new Color(0, 0, 0)));
		argumentsList.setModel(model);
		argumentsList.addMouseListener(this);
		argumentsList.addKeyListener(this);
		deleteAllButton.addActionListener(this);
		deleteButton.addActionListener(this);
		addButton.addActionListener(this);
		
	}
	
	private void clickedOnSave() {
		presenter.saveAction();
	}
	
	private void clickedOnCancel() {
		presenter.cancelAction();
	}
	
	private void clickedOnDownButton() {
		int index = argumentsList.getSelectedIndex();
		String argument1 = model.get(index);
		String argument2 = model.get(index+1);
		model.set(index,argument2);
		model.set(index+1,argument1);
		argumentsList.setSelectedIndex(index+1);
		refreshButtons(index+1);
	}

	private void clickedOnUpButton() {
		int index = argumentsList.getSelectedIndex();
		String argument1 = model.get(index);
		String argument2 = model.get(index-1);
		model.set(index,argument2);
		model.set(index-1,argument1);		
		argumentsList.setSelectedIndex(index-1);
		refreshButtons(index-1);
	}

	public String[] getArguments() {
		int size=argumentsList.getModel().getSize();
		String[] mainArguments = new  String[size];
    	for (int i=0;i<size;i++) {
    		mainArguments[i] = argumentsList.getModel().getElementAt(i).toString();
    	}
	    return mainArguments;
	}

	public void setPresenter(ArgumentsPresenterInterface presenter) {
		this.presenter = presenter;
	}
	
	public void loadArguments(String[] args) {
		deleteAll();
		for (int i=0;i<args.length;i++){
			addArgument(args[i]);
		}
	}
	
	private void updateText() {
    	int index = argumentsList.getSelectedIndex();
		if (index != -1)
			model.set(index, argument.getText());
	}

	private void addArgument(String argument_text) {
		model.addElement(argument_text);
		argumentsList.setModel(model);
		argument.setText("");
	}
	
	private void deleteArgument(int index) {
		if (index>=0) {
			model.removeElementAt(index);	
			argument.setText(""); 
		}else{
			JOptionPane.showMessageDialog(null,ERROR_EMPTY_DELETE,ERROR, JOptionPane.ERROR_MESSAGE);
		}
		refreshButtons(-1);
	}
	
	private void deleteAll() {
		model.clear();
		argument.setText(""); 
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
		if (e.getSource().equals(argumentsList)) {
    		int index = argumentsList.getSelectedIndex();
     		if (index != -1)
    			argument.setText(model.getElementAt(index));
    		refreshButtons(index);
		} else if (e.getSource().equals(contentPane)){
			argumentsList.clearSelection();
			argument.setText("");
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

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == addButton){
			if (!argument.getText().equals("")) {
				String text = argument.getText();
				addArgument(text);
			}else {
				JOptionPane.showMessageDialog(null,ERROR_EMPTY_ADD,ERROR, JOptionPane.ERROR_MESSAGE);	
			}			
		}		
		
		if (source == deleteButton)	{
			deleteArgument(argumentsList.getSelectedIndex());
		}
		
		if (source == deleteAllButton)	{
			deleteAll();
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

	
    
	public void keyPressed(KeyEvent e) {
		 if (e.getSource().equals(argumentsList)) {
			 int index = -1;
			
			 if (e.getKeyCode() == KeyEvent.VK_UP) {
				 int selected =  argumentsList.getSelectedIndex();
				 if (selected == 0)
					 index = 0;
				 else
					 index =selected-1;
				
			 }else if (e.getKeyCode() == KeyEvent.VK_DOWN){
				 int selected = argumentsList.getSelectedIndex()+1;
				 int maxList = argumentsList.getModel().getSize()-1;
				 if (selected >= maxList) {
					 index = argumentsList.getModel().getSize()-1;
				 } else index = selected;
			 }
			 refreshButtons(index);  

		 }

	 }

	
    public void keyReleased(KeyEvent e) {}
	
    public void keyTyped(KeyEvent e) {}
}
