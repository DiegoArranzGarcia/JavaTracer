package com.javatracer.view;
import java.awt.Container;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


@SuppressWarnings("serial")
public class MainArgumentesView  extends JFrame implements ActionListener, MouseListener, DocumentListener{

	private static int FIRST_COLUM = 20;
	private static int SECOND_COLUM = 510; 
	private static int WEIGHT_TEXT = 435;
	private static int WEIGHT_BUTTONS  = 120;
	private static int HIGH_COMPONENTS= 24;
	private static int FIRST_ROW =  30;
	private static int BETWEEN_DISTANCE = 40;
	private Container content;
	private JButton add, deleteAll, deleteElement,saveArguments,edit;
	private JLabel  message;
	private JTextField argument;
	@SuppressWarnings("rawtypes")
    private JList argumentsList;
	@SuppressWarnings("rawtypes")
    private DefaultListModel model;
	private JScrollPane scrollLista;
	private String[] mainArguments;
/*	private String actualDate;
	private int actIndex;*/
	

	
	public MainArgumentesView(){
		/*initializes the properties of the components*/
		initComponents();
   		/*Add a title*/
		setTitle("Add main arguments"); 
		/*Window size*/
		setSize(660,330);
		/*put the window in the center of the screen*/
		setLocationRelativeTo(null);
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	
	
    @SuppressWarnings("rawtypes")
    private void initComponents() {
    	
    	
    	content=getContentPane();
		/*	Define components  position*/
		content.setLayout(null);
		
		argument= new JTextField();
		argument.setBounds(FIRST_COLUM, FIRST_ROW, WEIGHT_TEXT, HIGH_COMPONENTS);
		//argument.addKeyListener(this);
		argument.getDocument().addDocumentListener(this);
		
		/*Buttons properties*/
		add= new JButton();
		add.setText("Add ");
		add.setBounds(SECOND_COLUM, FIRST_ROW, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		add.addActionListener(this);
		
		edit= new JButton();
		edit.setText("Edit ");
		edit.setBounds(SECOND_COLUM, FIRST_ROW + BETWEEN_DISTANCE, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		edit.addActionListener(this);
		
		deleteElement= new JButton();
		deleteElement.setText("Delete");
		deleteElement.setBounds(SECOND_COLUM, FIRST_ROW+BETWEEN_DISTANCE*2, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		deleteElement.addActionListener(this);
		
		deleteAll= new JButton();
		deleteAll.setText("Delete all");
		deleteAll.setBounds(SECOND_COLUM, FIRST_ROW +BETWEEN_DISTANCE*3, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		deleteAll.addActionListener(this);
		
		saveArguments=new JButton();
		saveArguments.setText("Save");
		saveArguments.setBounds(220, 210, WEIGHT_BUTTONS, HIGH_COMPONENTS);
		saveArguments.addActionListener(this); 
		
		message= new JLabel();
		message.setBounds(20, 250, 280, HIGH_COMPONENTS);
		
		argumentsList = new JList();
		argumentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		argumentsList.addMouseListener(this);
		
		model = new DefaultListModel();
	   	
	    scrollLista = new JScrollPane();
		scrollLista.setBounds(FIRST_COLUM, 90,WEIGHT_TEXT, 80);
	    scrollLista.setViewportView(argumentsList);
		
		/*Add the components  at content*/
		content.add(argument);
		content.add(add);
		content.add(edit);
		content.add(deleteAll);
		content.add(deleteElement);
		content.add(message);
		content.add(scrollLista);
		content.add(saveArguments);
	    
    }


    public void actionPerformed(ActionEvent event) {
		if (event.getSource()==add){
			if (!argument.getText().equals("")) {
				addArgument();
				message.setText("Argument added");
			}else {
				JOptionPane.showMessageDialog(null, "You must add an argument","Error", JOptionPane.ERROR_MESSAGE);	
				message.setText("No argument added");
			}			
		}
		
		if (event.getSource() == edit) {
			System.out.println("Value: "+argumentsList.getSelectedValue());
			argument.setText(argumentsList.getSelectedValue().toString());
		}
		
		
		
		if (event.getSource()==deleteElement)	{
			deleteArgument(argumentsList.getSelectedIndex() );
		}
		
		if (event.getSource()==deleteAll)	{
			deleteList();
			message.setText("All list deleted");
		}
		
		if(event.getSource()==saveArguments) {
			
		}
	}

	@SuppressWarnings("unchecked")
    private void addArgument() {
		String arg=argument.getText();
		model.addElement(arg);
		argumentsList.setModel(model);
		argument.setText("");
	}
	
	private void deleteArgument(int index) {
		if (index>=0) {
			model.removeElementAt(index);	
			argument.setText(""); 
			message.setText("An item was removed at the position "+index);
		}else{
			JOptionPane.showMessageDialog(null, "You must select an index","Error", JOptionPane.ERROR_MESSAGE);
			
				message.setText("No item is selected");
		}
		
	}
	
	private void deleteList() {
		model.clear();
		argument.setText(""); 
	}

	@SuppressWarnings("rawtypes")
    public DefaultListModel getModel() {
		return model;
	}



	@SuppressWarnings("rawtypes")
    public void setModel(DefaultListModel model) {
		this.model = model;
	}



    public void mouseClicked(MouseEvent e) {
    
	    
    }


    public void mouseEntered(MouseEvent e) {
    }



    public void mouseExited(MouseEvent e) {
    	
    	
    	
    	/*if (e.getSource().equals(argument)&& !actualDate.equals("")) {
    		model.set(actIndex, actualDate);
    	}*/
    	
    }


    public void mousePressed(MouseEvent e) {
    	
    	if (e.getSource().equals(argumentsList)) {
    		argument.setText(argumentsList.getSelectedValue().toString());
    		
    	}
    }


    public void mouseReleased(MouseEvent e) {
	    
    }

	public String[] getMainArguments() {
		int size=argumentsList.getModel().getSize();
		mainArguments = new  String[size];
    	for (int i=0;i<size;i++) {
    		mainArguments[i]=argumentsList.getModel().getElementAt(i).toString();
    		System.out.println("ARGS"+mainArguments[i]);
    		
    	}
	    return mainArguments;
    }



	public void setMainArguments(String[] mainArguments) {
	    this.mainArguments = mainArguments;
    }



	@Override
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
}
