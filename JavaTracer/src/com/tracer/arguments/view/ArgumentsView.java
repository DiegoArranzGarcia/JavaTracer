package com.tracer.arguments.view;
import java.awt.Container;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.tracer.arguments.presenter.ArgumentsPresenterInterface;


@SuppressWarnings("serial")
public class ArgumentsView  extends JFrame implements ActionListener, MouseListener, DocumentListener, ArgumentsViewInterface{

	private static String WINDOWS_TITLE = "Add main arguments";
	
	private static String ADD = "Add";
	private static String DELETE = "Delete";
	private static String DELETE_ALL = "Delete All";
	private static String SAVE = "Save";
	
	private static String ERROR = "Error";
	private static String ERROR_EMPTY_DELETE = "You must select an argument";
	private static String ERROR_EMPTY_ADD = "You must write an argument";
	
	private static int FIRST_COLUM = 20;
	private static int SECOND_COLUM = 510; 
	private static int WEIGHT_TEXT = 435;
	private static int WEIGHT_BUTTONS  = 120;
	private static int HIGH_COMPONENTS= 24;
	private static int FIRST_ROW =  30;
	private static int BETWEEN_DISTANCE = 40;
	
	private Container content;
	private JButton add, deleteAll, deleteElement,saveArguments;
	private JLabel  message;
	private JTextField argument;
    private JList<String> argumentsList;
    private DefaultListModel<String> model;
	private JScrollPane scrollLista;

	private ArgumentsPresenterInterface presenter;
	
	public ArgumentsView(){
		/*initializes the properties of the components*/
		initComponents();
   		/*Add a title*/
		setTitle(WINDOWS_TITLE); 
		/*Window size*/
		setSize(660,330);
		/*put the window in the center of the screen*/
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
    private void initComponents() {
    	
    	
    	content=getContentPane();
		/*	Define components  position*/
		content.setLayout(null);
		
		argument= new JTextField();
		argument.setBounds(FIRST_COLUM, FIRST_ROW, WEIGHT_TEXT, HIGH_COMPONENTS);
		argument.getDocument().addDocumentListener(this);
		
		/*Buttons properties*/
		add= new JButton();
		add.setText(ADD);
		add.setBounds(SECOND_COLUM, FIRST_ROW, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		add.addActionListener(this);
		
		deleteElement= new JButton();
		deleteElement.setText(DELETE);
		deleteElement.setBounds(SECOND_COLUM, FIRST_ROW+BETWEEN_DISTANCE, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		deleteElement.addActionListener(this);
		
		deleteAll= new JButton();
		deleteAll.setText(DELETE_ALL);
		deleteAll.setBounds(SECOND_COLUM, FIRST_ROW +BETWEEN_DISTANCE*2, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		deleteAll.addActionListener(this);
		
		saveArguments=new JButton();
		saveArguments.setText(SAVE);
		saveArguments.setBounds(220, 210, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		saveArguments.addActionListener(this); 
		
		message= new JLabel();
		message.setBounds(20, 250, 280, HIGH_COMPONENTS);
		
		argumentsList = new JList<String>();
		argumentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		argumentsList.addMouseListener(this);
		//argumentsList.setSelectionModel(new ToggleSelectionModel());
		
		model = new DefaultListModel<String>();
	   	
	    scrollLista = new JScrollPane();
		scrollLista.setBounds(FIRST_COLUM, 90,WEIGHT_TEXT, 80);
	    scrollLista.setViewportView(argumentsList);
		
		/*Add the components  at content*/
		content.add(argument);
		content.add(add);
		content.add(deleteAll);
		content.add(deleteElement);
		content.add(message);
		content.add(scrollLista);
		content.add(saveArguments);
		content.addMouseListener(this);
		
	    
    }


    public void actionPerformed(ActionEvent event) {
    	Object source = event.getSource();
		if (source == add){
			if (!argument.getText().equals("")) {
				addArgument(argument.getText());
				message.setText("Argument added");
			}else {
				JOptionPane.showMessageDialog(null,ERROR_EMPTY_ADD,ERROR, JOptionPane.ERROR_MESSAGE);	
				message.setText("No argument added");
			}			
		}		
		
		if (source == deleteElement)	{
			deleteArgument(argumentsList.getSelectedIndex());
		}
		
		if (source == deleteAll)	{
			deleteAll();
			message.setText("All list deleted");
		}
		
		if(source == saveArguments) {
			clickedOnSave();
		}
	}

    private void clickedOnSave() {
    	presenter.clickedOnSave();
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
			message.setText("An item was removed at the position "+index);
		}else{
			JOptionPane.showMessageDialog(null,ERROR_EMPTY_DELETE,ERROR, JOptionPane.ERROR_MESSAGE);
			message.setText("No item is selected");
		}
		
	}
	
	private void deleteAll() {
		model.clear();
		argument.setText(""); 
	}

    public DefaultListModel<String> getModel() {
		return model;
	}

    public void setModel(DefaultListModel<String> model) {
		this.model = model;
	}

    public void mouseClicked(MouseEvent e) {
    	if (e.getSource().equals(content)) {
    		boolean found = false;
    		int size=argumentsList.getModel().getSize();
    		int i = 0;
    		
    		while (i<size && !found){
        		found = argumentsList.isSelectedIndex(i);
        		if (found) {
        			argumentsList.removeSelectionInterval(i, i); 
        			argument.setText(""); 
        		}
        		i++;
    		}
    		
    	}
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
    	
    	if (e.getSource().equals(argumentsList)) {
    		argument.setText(argumentsList.getSelectedValue().toString());
    	}
    }


    public void mouseReleased(MouseEvent e) {
	    
    }

	public void changedUpdate(DocumentEvent arg0) {
		
	}

	public void insertUpdate(DocumentEvent arg0) {
		updateText();
	}

	public void removeUpdate(DocumentEvent arg0) {
		updateText();
	}

	private void updateText() {
		int size=argumentsList.getModel().getSize();
    	boolean found = false;
    	int i = 0;
    	
    	while (i<size && !found){
    		found = argumentsList.isSelectedIndex(i);
    		if (!found)
    			i++;
		}
	
		if (found)
			model.set(i, argument.getText());
		
	}
	
	// ArgumentsViewInterface methods	

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
}
