package Tracer;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class WindowPath extends JFrame implements ActionListener {

	private Container contenedor;/**declaramos el contenedor*/
	JButton changeButton;/**declaramos el objeto Boton*/
	JLabel labelPath;/**declaramos el objeto Label*/
	JTextField path; /**Recoge informacíon sobre la ruta*/
	
	public WindowPath(){
		 /**permite iniciar las propiedades de los componentes*/
		  iniciarComponentes();
		  /**Asigna un titulo a la barra de titulo*/
		  setTitle("Ruta del archivo ");
		  /**tamaño de la ventana*/
		  setSize(600,280);
		  /**pone la ventana en el Centro de la pantalla*/
		  setLocationRelativeTo(null);
		
	}

	private void iniciarComponentes() {
		  contenedor=getContentPane();/**instanciamos el contenedor*/
		  /**con esto definmos nosotros mismos los tamaños y posición
		   * de los componentes*/
		  contenedor.setLayout(null);
		  
		  /**Etiqueta para pedir los datos
		   */
		   
		  labelPath = new JLabel("Escriba la ruta: ");
		  labelPath.setVisible(true);
		  labelPath.setSize(100, 150); 
		  
		  /**
		   * Recolección de datos
		   */
		  path = new JTextField();
		  path.setVisible(true);
		  path.setBounds(100, 60, 400, 35);
		//  path.setSize(500, 50);
		  
		  /**Propiedades del boton, lo instanciamos, posicionamos y
		   * activamos los eventos*/
		  changeButton= new JButton();
		  changeButton.setText("Ejecutar");
		  changeButton.setBounds(250, 150, 90, 23);
		  changeButton.addActionListener(this);

		  contenedor.add(labelPath);
		  contenedor.add(path);
		  contenedor.add(changeButton);
		  
		 }
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().getClass().getSimpleName().equals("JButton")){
			String[] args= processPath(path.getText());
			 new Trace(args);
		}
		
	}

	private String[] processPath(String path) {
		
		String filePath= path.substring(0,path.indexOf("bin")+4);
		String nameClass = path.substring(path.indexOf("bin")+4).replaceAll("/", ".");
		String[] args={filePath,nameClass};
		return args;
	}
}
