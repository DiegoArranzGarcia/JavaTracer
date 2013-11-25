package Interface;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Inspector {

	JTextArea inspector;
	
	public Inspector() {
		
	 JFrame f= new JFrame();
   	 f.setVisible(true);
   	 f.setSize(400, 400);
   	  
   	JPanel TextArea = new JPanel();
   	TextArea.setLayout(new BorderLayout());
 		
 	
 		
 		inspector = new JTextArea("hola",20,20);
 		inspector.append(" q ase \n /n");
 		inspector.setEnabled(false);
        inspector.setDisabledTextColor(Color.black);	   
 		TextArea.add(inspector);
 		
 		inspector.validate(); 
       
 		f.setContentPane(inspector);
 		
 		f.validate();
 		
	}
	
	private void PrintObject()
	{
		
		
		
	}

}
