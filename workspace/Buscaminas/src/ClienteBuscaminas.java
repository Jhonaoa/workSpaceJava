import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractButton;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
public class ClienteBuscaminas extends JFrame implements Runnable {

	
		private JFrame contenedor  = new JFrame("Memory game");
		private JPanel panel = new JPanel();
		private Juego control;
		private ImageIcon imagen;
		private Casilla[][] tablero;
		private Socket conexion;
		private ObjectOutputStream salida;
		private ObjectInputStream entrada;
		private String host;
		
		public ClienteBuscaminas(String host)
		{
			this.host = host;
			control = new Juego();
			
			panel.setLayout(new GridLayout(10,10,5,5) );
			
			tablero = new Casilla[10][10];
			for (int i=0; i<10; i++)
			{
				for(int j=0; j<10; j++)
				{
					tablero[i][j] = new Casilla();
					tablero[i][j].setTipo(0);
				}
		
			}
			
			imagen = new ImageIcon("src/images/fondo.gif");

			//System.out.print(imagen.getIconWidth());
			
			for(int i = 0; i<10;i++)
			{
				for(int j = 0 ; j< 10;j++)
				{
					panel.add(tablero[i][j]);
					//tablero[i][j].addMouseListener(this);
					tablero[i][j].setBackground(new java.awt.Color(255,0,77));
					
				
					System.out.print(" "+tablero[i][j].getTipo());
					
					
				}
				System.out.println();
			}
		
			
		contenedor.setSize(630,630);
		
		contenedor.add(panel);
		contenedor.setVisible(true);
		
		iniciarCliente();
			
		}
		
		public void iniciarCliente(){
			// se conecta al servidor, obtiene los flujos e inicia subproceso de salida
			
			try {
				conexion = new Socket(InetAddress.getByName( host ), 12345 );
		
				entrada = new ObjectInputStream( conexion.getInputStream() );
				salida = new ObjectOutputStream( conexion.getOutputStream() );
			}
			catch ( IOException excepcionES ){
				excepcionES.printStackTrace();
			} 

		// crea e inicia subproceso trabajador para este cliente
		ExecutorService trabajador = Executors.newFixedThreadPool( 1 );
		trabajador.execute( this ); // ejecuta el cliente
		}

		@Override
		public void run() {
			System.out.println("hablalo");
			
		} // fin del método iniciarCliente

		
		
		/*public void mouseClicked(MouseEvent e)
		{
			
			
			
			
			// System.out.print(((Ficha) e.getComponent()).getID());
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}*/
		
		
}
