package com.javatracer.view;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * 
 */

/**
 * @author Saskya
 *
 */
public class JListWindow  extends JFrame implements ActionListener{

	private Container content;
	private JButton add, deleteAll, deleteElement;
	private JLabel  mensaje;
	private JTextField argument;
	private JList argumentsList;
	private DefaultListModel model;
	private JScrollPane scrollLista;
	
	public JListWindow(){
		/*initializes the properties of the components*/
		initComponents();
   		/*Add a title*/
		setTitle("Add main arguments"); 
		/*Window size*/
		setSize(660,330);
		/*put the window in the center of the screen*/
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
    private void initComponents() {
    	content=getContentPane();
		/*	Define components  position*/
		content.setLayout(null);
		
		argument= new JTextField();
		argument.setBounds(20, 30, 435, 23);
		
		
		/*Buttons properties*/
		add= new JButton();
		add.setText("Add ");
		add.setBounds(510, 30, 100, 23);
		add.addActionListener(this);
		
		deleteAll= new JButton();
		deleteAll.setText("Delete all");
		deleteAll.setBounds(270, 210, 120, 23);
		deleteAll.addActionListener(this);
		
		deleteElement= new JButton();
		deleteElement.setText("Delete");
		deleteElement.setBounds(100, 210, 120, 23);
		deleteElement.addActionListener(this);
		
		
		mensaje= new JLabel();
		mensaje.setBounds(20, 250, 280, 23);
		
		argumentsList = new JList();
		argumentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		
		model = new DefaultListModel();
	   	
	    scrollLista = new JScrollPane();
		scrollLista.setBounds(20, 90,435, 80);
	    scrollLista.setViewportView(argumentsList);
		
		/*Add the components  at content*/
		content.add(argument);
		content.add(add);
		content.add(deleteAll);
		content.add(deleteElement);
		content.add(mensaje);
		content.add(scrollLista);
	    
    }


    public void actionPerformed(ActionEvent evento) {
		if (evento.getSource()==add)
		{
			if (!argument.getText().equals("")) {
				agregarNombre();
				mensaje.setText("Argument added");
			}else {
				JOptionPane.showMessageDialog(null, "You must add an argument","Error", JOptionPane.ERROR_MESSAGE);
				
				mensaje.setText("No argument added");
			}
			
		}
		if (evento.getSource()==deleteElement)
		{
			eliminarNombre(argumentsList.getSelectedIndex() );
		}
		if (evento.getSource()==deleteAll)
		{
			borrarLista();
			mensaje.setText("All list deleted");
		}
	}

	private void agregarNombre() {
		String nombre=argument.getText();
		model.addElement(nombre);
		argumentsList.setModel(model);
		argument.setText("");
	}
	
	private void eliminarNombre(int indice) {
		if (indice>=0) {
			model.removeElementAt(indice);	
			mensaje.setText("An item was removed at the position "+indice);
		}else{
			JOptionPane.showMessageDialog(null, "You must select an index","Error", JOptionPane.ERROR_MESSAGE);
			
				mensaje.setText("No item is selected");
		}
		
	}
	
	private void borrarLista() {
		model.clear();
	}

}
