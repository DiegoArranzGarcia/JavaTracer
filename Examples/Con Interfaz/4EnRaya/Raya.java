/*
	Programa: Conecta 4 Java
	Autor: Borja
	Web: http://todojava.awardspace.com/
	Version: 1.0
	
	Descripción: Juego del Conecta 4 con modo de 1 o 2 jugadores.
	
	Dificultad: Media
*/

//	Clase Principal

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Raya extends JFrame implements ActionListener
{
	int turno=0;
	boolean fin;
	boolean dosJugadores;
	boolean pulsado;
	//botones
	JButton boton[][]=new JButton[7][7];
	//menus
	JMenuBar Barra=new JMenuBar();
	JMenu Archivo=new JMenu("Archivo");
	JMenu Opciones=new JMenu("Opciones");
	JMenuItem Nuevo=new JMenuItem("Nuevo");
	JMenuItem Salir=new JMenuItem("Salir");
	JMenuItem M1Jugador=new JMenuItem("1 Jugador");
	JMenuItem M2Jugador=new JMenuItem("2 Jugadores");
	JLabel Nombre=new JLabel("http://todojava.awardspace.com",JLabel.CENTER);
	
	//imagenes
	ImageIcon foto1;
	ImageIcon foto2;
	
	Raya()
	{
		//Cargar imagenes
		
		foto1=new ImageIcon("./foto1.gif");
		foto2=new ImageIcon("./foto2.gif");
		
		//menu
		Nuevo.addActionListener(this);
		Archivo.add(Nuevo);
		Archivo.addSeparator();
		Salir.addActionListener(this);
		Archivo.add(Salir);
		M1Jugador.addActionListener(this);
		Opciones.add(M1Jugador);
		M2Jugador.addActionListener(this);
		Opciones.add(M2Jugador);
		Barra.add(Archivo);
		Barra.add(Opciones);
		setJMenuBar(Barra);
		
		
		//Panel Principal 
		JPanel Principal=new JPanel();
		Principal.setLayout(new GridLayout(7,7));

		//Colocar Botones
		for(int i=0;i<7;i++)
		{
			for(int j=0;j<7;j++)
			{
				
				boton[i][j]=new JButton();
				boton[i][j].addActionListener(this);
				boton[i][j].setBackground(Color.BLACK); 
				Principal.add(boton[i][j]);
			}
			Nombre.setForeground(Color.BLUE);
			add(Nombre,"North");
			add(Principal,"Center");
			
		}
		
		//Para cerrar la Ventana
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				System.exit(0);
			}
		});
		
		//tamaño frame
		setLocation(170,25);
		setSize (600,600);
		setResizable(false);
		setTitle("CONECTA 4      v 1.0                                      http://todojava.awardspace.com/");
		setVisible(true);
		
	}
	void ganar(int x,int y)
	{
		/*
		 *	x fila
		 *	y columna
		 */
		 
		fin=false;
		//Quien gana en?¿ horizontal	
			int ganar1=0;
		    int ganar2=0;
		    for(int j=0;j<7;j++)
	     	{
			    if(boton[y][j].getIcon()!=null)
			    {
				     if(boton[y][j].getIcon().equals(foto1))
				     {
					    ganar1++;
				     }
				     else ganar1=0;
				     if(ganar1==4)
				     {
				     	 
					      JOptionPane.showMessageDialog(this,"Gana Jugador Rojo","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto1);
					      VolverEmpezar();
					      fin=true;
				     }
				     if(fin!=true)
				     {
				     	if(boton[y][j].getIcon().equals(foto2))
				        {
					           ganar2++;
				        }
				        else ganar2=0;
				        if(ganar2==4)
				        {
				          JOptionPane.showMessageDialog(this,"Gana Jugador Verde","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto2);	
				          VolverEmpezar();
				        }
				     }
		    	}
		    	 else
		         {
		    	   ganar1=0;
		    	   ganar2=0;
		         }
		   }
		// Quien gana en?¿ vertical
			 ganar1=0;
		     ganar2=0;
		    for(int i=0;i<7;i++)
	     	{
			    if(boton[i][x].getIcon()!=null)
			    {
				     if(boton[i][x].getIcon().equals(foto1))
				     {
					    ganar1++;
				     }
				     else ganar1=0;
				     if(ganar1==4)
				     {
					      JOptionPane.showMessageDialog(this,"Gana Jugador Rojo","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto1);
					      VolverEmpezar();
					      fin=true;
				     }
				     if(fin!=true)
				     {
				     	if(boton[i][x].getIcon().equals(foto2))
				     	{
				     		ganar2++;
				     	}
				        else ganar2=0;
				        if(ganar2==4)
				        {
				          JOptionPane.showMessageDialog(this,"Gana Jugador Verde","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto2);
				          VolverEmpezar();	
				        }
				     }
		    	}
		    }
		// Quien gana en Oblicuo 1º Posicion De izquierda a derecha
		 ganar1=0;
		 ganar2=0;
			int a=y;
			int b=x;
		    	while(b>0 && a>0)
		    	{
		    		a--;
		    		b--;   		
		    	}
		    	while(b<7 && a<7)
		    	{	
		    	 if(boton[a][b].getIcon()!=null)
		    	 {		    		
		    		if(boton[a][b].getIcon().equals(foto1))
				     {
					    ganar1++;
				     }
				     else ganar1=0;
				     if(ganar1==4)
				     {
					      JOptionPane.showMessageDialog(this,"Gana Jugador Rojo","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto1);
					      VolverEmpezar();
					      fin=true;	    
				     }
				     if(fin!=true)
				     {
				     	if(boton[a][b].getIcon().equals(foto2))
				     	{
				     		ganar2++;
				     	}
				     
				        else ganar2=0;
				        if(ganar2==4)
				        {
				          JOptionPane.showMessageDialog(this,"Gana Jugador Verde","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto2);
				          VolverEmpezar();	
				        }   
				     }
				 }
				 else 
				 {
				 	ganar1=0;
				 	ganar2=0;
				 } 
		    	    a++;
		    		b++;  
		         } 
		 // Quien gana en oblicuo? 2º Posicion de derecha a izquierda 
	        ganar1=0;
		    ganar2=0;
			 a=y;
			 b=x;
		      	//buscar posición de la esquina
		    	while(b<6 && a>0)
		    	{
		    		a--;
		    		b++;
		    		
		    	}
		    	while(b>-1 && a<7 )
		    	{
		    	 if(boton[a][b].getIcon()!=null)
		    	 {		
		    		if(boton[a][b].getIcon().equals(foto1))
				     {
					    ganar1++;
				     }
				     else ganar1=0;
				     if(ganar1==4)
				     {
					      JOptionPane.showMessageDialog(this,"Gana Jugador Rojo","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto1);
					      VolverEmpezar();
					      fin=true;	    
				     }
				     if(fin!=true)
				     {
				     	if(boton[a][b].getIcon().equals(foto2))
				     	{
				     		ganar2++;
				       	}
				        else ganar2=0;
				        if(ganar2==4)
				        {
				          JOptionPane.showMessageDialog(this,"Gana Jugador Verde","Conecta 4",JOptionPane.INFORMATION_MESSAGE,foto2);
				          VolverEmpezar();	
				        }
				     }
				 } 
				 else 
				 {
				 	ganar1=0;
				 	ganar2=0;
				 } 
		    	    
		    	 a++;
		         b--;
		         } 
		}
	// Volver el programa al estado inicial	
	void VolverEmpezar()
		{
			for(int i=0;i<7;i++)
			{
				for(int j=0;j<7;j++)
				{
					boton[i][j].setIcon(null);
				}
			}
			turno=0;
		}
		
	 public void actionPerformed(ActionEvent ae)
		 {
		   if(ae.getSource()==M1Jugador)
		   {
		   	dosJugadores=false;
		   	VolverEmpezar();
		   }	
		   if(ae.getSource()==M2Jugador)
		   {
		   	dosJugadores=true;
		   	VolverEmpezar();
		   }
           if(ae.getSource()==Salir)
		    {	
		      dispose();
		    }
		   if(ae.getSource()==Nuevo)
		    {
		      VolverEmpezar();
		    }
		 	int x=0;
		 	int y=0;
		 	for(int i=0;i<7;i++)
		 	{
		 		for(int j=0;j<7;j++)
		 		{
		 			    if (ae.getSource()==boton[i][j])
		 			    {	
		 				//Ir hasta la ultima posicion
		 				int k=7;
		 				do
		 				{
		 					k--;
		 				}
		 				while(boton[k][j].getIcon()!=null & k!=0);
		 				//Pintar Ficha
							if(boton[k][j].getIcon()==null)
		 				 	{	
		 				 	  	if(dosJugadores) //Si es modo un jugador o dos
						         { //Dos Jugadores
		 				  			if(turno %2==0)boton[k][j].setIcon(foto1);
		 				  			else boton[k][j].setIcon(foto2);
		 			      			turno++;
		 			      			x=j;
		 			      			y=k;
							     }
							    else
		 			        	{  //Un Jugador
		 			        	 boton[k][j].setIcon(foto1);
		 			        	 turno++;
		 			        	 pulsado=true;
		 			      		 x=j;
		 			      		 y=k;
		 			        	} 
		 			        } 
		 			}
		 		}
		 	}
		 	ganar(x,y);
		 	if(pulsado)
		 		{
		 	      if(!dosJugadores) juegaMaquina(x,y); 
		 	  	} 

		 	//Empate!!!
		    if(turno==49)  
		    {
		    	 JOptionPane.showMessageDialog(this,"Has Empatado","Conecta 4",JOptionPane.INFORMATION_MESSAGE);
				 VolverEmpezar();
		    }
		    fin=false;
		}
	void juegaMaquina(int x,int y)
	{
		boolean Cubrir_izquierda=false;
		int ganarVert=0;	
		int ganarHorz=0;
		int posicion=0;
        posicion=GenerarAleatorio(x); //Generar Aleatorio por defecto
               
//_________________________ATAQUE MAQUINA En horizontal_________________________________
	ganarHorz=0;
	
	  for(int i=0;i<7;i++) //buscamos en todo el tablero
	     	{
	     		for(int j=0;j<7;j++)
	     		{
	     			
	     		if(boton[i][j].getIcon()!=null)
			    	{
				     
				     if(boton[i][j].getIcon().equals(foto2))
				     		      {
								    ganarHorz++;
				     			  }
				     			else ganarHorz=0;
				     			if(ganarHorz==3)
				     		      {
			 		 				posicion=j;
			 		 				if(posicion!=6) 
			 		 				{
			 		 				  if(boton[y][j+1].getIcon()==null)	posicion++;
			 		 				  else if(j>=3 && boton[y][j-3].getIcon()==null)posicion=posicion-3;
			 		 				  System.out.println("Por la derecha");
			 		 				}
			 		 			 }
			 		 }			 
	     		}
			ganarHorz=0;
		   }
        	
//_____________________________Defenderse En Horizontal____________________________Hacia la izquierda
			ganarHorz=0;
  			for(int j=6;j>=0;j--)
	    		 	{
					    if(boton[y][j].getIcon()!=null)
			    			{
				    	 		if(boton[y][j].getIcon().equals(foto1))
				     		      {
								    ganarHorz++;
				     			  }
				     			else ganarHorz=0;
				     			if(ganarHorz==3 && j!=0)
				     		      {
				     		      	posicion=j;
			 		 			
			 		 				  if(boton[y][j-1].getIcon()==null)
			 		 				  {
			 		 				  	posicion--;
			 		 				  	Cubrir_izquierda=true;
			 		 				  }	
			 		 				  //System.out.println("Por la izquierda");
			 	     	       	  }
			 			}
			 		} 
			 		
//_____________________________Defenderse En Horizontal____________________________Hacia la derecha
			ganarHorz=0;
  		if(!Cubrir_izquierda)
  		{
  			for(int j=0;j<7;j++)
	    		 	{
					    if(boton[y][j].getIcon()!=null)
			    			{
				    	 		if(boton[y][j].getIcon().equals(foto1))
				     		      {
								    ganarHorz++;
				     			  }
				     			else ganarHorz=0;
				     			if(ganarHorz==3)
				     		      {
			 		 				posicion=j;
			 		 				if(posicion!=6) 
			 		 				{
			 		 				  if(boton[y][j+1].getIcon()==null)	posicion++;
			 		 				  //System.out.println("Por la derecha");
			 		 				}
			 	     			}
			 				} 
			 			}
  		}	
  			      
//____________________________Defenderse en Vertical______________________________
		    for(int i=0;i<7;i++)
	     	{
			    if(boton[i][x].getIcon()!=null)
			    {
				     if(boton[i][x].getIcon().equals(foto1))
				     {
					    ganarVert++;
				     }
				     else ganarVert=0;
				     if(ganarVert==3)
				     {
				     	  posicion=x; //obtiene la columna en la que se puede ganar;
				     	  //System.out.println("Por la arriba");
				     }
		    	}
		    }
//___________________________ATAQUE MÁQUINA EN VERTICAL__________________________
			    
		 ganarVert=0; 
		 for(int i=0;i<7;i++) //buscamos en todo el tablero
	     	{
	     		for(int j=0;j<7;j++)
	     		{
	     		if(boton[j][i].getIcon()!=null)
			    	{
								     
				     	if(boton[j][i].getIcon().equals(foto2))
				     	{
					    	ganarVert++;
				     	}
				     	else ganarVert=0;
				     	if(ganarVert==3 && j!=0) //si en alguna columna hay 3 fichas seguidas de la máquina
				     	{
				     	  posicion=i; //obtiene la columna en la que se puede ganar;
				     	}
		    		}
		   		}
		   ganarVert=0;
		   }
			 		
			 	if(boton[0][posicion].getIcon()!=null) //si no se pude poner ficha en la columna (columna llena)
		 	     { 	
		 	  	posicion=GenerarAleatorio(posicion); //Genera posición aleatoria
		 	     }	
		 		 	int k=7;
		 		 //Ir a la última posición de la columna	
		 		 	do
		 		 	 {
		 		 	 	k--;
		 		 	 }
		 		 	 while(boton[k][posicion].getIcon()!=null & k!=0);
		 				//Pintar Ficha
		 		 	    boton[k][posicion].setIcon(foto2);
		ganar(posicion,k);
		pulsado=false;  
		Cubrir_izquierda=false;      
	}	 
	int GenerarAleatorio(int posicion)
	{
		//Buscar columna en la que se puede poner
		double aleatorio=0;
		do
	 	{
			aleatorio=Math.random()*7;
	 		posicion=(int)aleatorio;
	 	}
	 	while(boton[0][posicion].getIcon()!=null); //posicion 0: para q busque las columnas q no esten llenas
	 	return posicion;
	}
	
}