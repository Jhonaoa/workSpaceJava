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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
public class ClienteBuscaminas extends JFrame implements Runnable {

	
		private JFrame contenedor;
		private JTextArea informacion;
		private JPanel panel,panel1;
		private Juego control;
		private ImageIcon imagen;
		private Casilla[][] tablero;
		private Socket conexion;
		private Formatter salida;
		private Scanner entrada;
		private String host;
		private EventosMouse eventos;
		private Casilla casillaAux;
		private int posX;
		private int posY;
		private String posicion;
		private boolean miTurno;
		private String miIdentidad;
		private String mensaje;
		
		
		
		public ClienteBuscaminas(String host)
		{
			
			contenedor  = new JFrame("Buscaminas");
			informacion = new JTextArea(70,10);
			posX = 0;
			posY = 0;
			posicion = "";
			miTurno = true;
			panel = new JPanel();
			panel1 = new JPanel();
			this.host = host;
			control = new Juego();
			casillaAux = new Casilla();
			eventos = new EventosMouse();
			panel.setLayout(new GridLayout(10,10,5,5) );
			mensaje = "";
			tablero = new Casilla[10][10];
			
			for (int i=0; i<10; i++)
			{
				for(int j=0; j<10; j++)
				{
					tablero[i][j] = new Casilla();
					
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
					tablero[i][j].setUbicacionX(i);
					tablero[i][j].setUbicacionY(j);
					tablero[i][j].addMouseListener(eventos);
				
					
					
					
				}
				System.out.println();
			}
		
			informacion.setEditable(false);
			panel1.add(informacion);
			
			panel1.setBounds(0,630,630,100);
			
		
		
		
		contenedor.setSize(700,700);
		contenedor.add(new JScrollPane(panel1), BorderLayout.WEST);
		contenedor.add(panel, BorderLayout.CENTER);
		contenedor.setVisible(true);
		
		iniciarCliente();
			
		}
		
		public void iniciarCliente()
		{
			// se conecta al servidor, obtiene los flujos e inicia subproceso de salida
			
			try {
				conexion = new Socket(InetAddress.getByName( host ), 12345 );
				salida = new Formatter(conexion.getOutputStream());
				entrada = new Scanner(conexion.getInputStream());
				
			}
			catch ( IOException excepcionES ){
				excepcionES.printStackTrace();
			} 

		// crea e inicia subproceso trabajador para este cliente
		ExecutorService trabajador = Executors.newFixedThreadPool( 1 );
		trabajador.execute( this ); // ejecuta el cliente
		}
		
	
		public void run() 
		{
			miIdentidad = entrada.nextLine();
			
			posicion = entrada.nextLine();
			llenarTablero();
			
			miTurno = (miIdentidad.equals("0"));
			
			
			
			while(true)
			{
				
				if(entrada.hasNextLine())
				{
					
					procesarCasilla( entrada.nextLine());
				}
			
			} 
		
		}
		
		public void llenarTablero()
		{
			int contador = 0;
			int x =0;
		
			for(int i= 0; i<10;i++)
			{
				for(int j= 0; j<10;j++)
				{
					x=Integer.parseInt(String.valueOf(posicion.charAt(contador)));
					tablero[i][j].setTipo(x);
					contador++;
				}
			}
			
			for(int i=0; i<10; i++)
			{
				for(int j=0; j<10; j++)
				{
					System.out.print(tablero[i][j].getTipo() + "  ");
				}
				System.out.println();
			}
		}
		
		public void procesarCasilla(String s)
		{
			
				if(s.equals("Jugador ha movido"))
				{
					
					
					posicion = entrada.nextLine();
					posX = Integer.parseInt(String.valueOf(posicion.charAt(0)));
					posY = Integer.parseInt(String.valueOf(posicion.charAt(1)));
					System.out.println("cari"+posX);
					System.out.println("cari"+posY);
					setImagen(posX, posY);
					
					
					
				}
				else if (s.equals("movimiento invalido"))
				{
					
					mostrarMensaje(s);
				}
					
				
				else if(s.equals("El otro jugador movio, es tu turno"))
				{
					
					posicion = entrada.nextLine();
					posX = Integer.parseInt(String.valueOf(posicion.charAt(0)));
					posY = Integer.parseInt(String.valueOf(posicion.charAt(1)));
					setImagen(posX, posY);
					mostrarMensaje(s);
					miTurno = true;
					
				}
				
					
					else
						if(s.equals("eres el jugador 1"))
						{
							mostrarMensaje(s);
						}
				
		
		
	}
		
		public void mostrarMensaje(final String m)
		{
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run()
				{
						// muestra la marca del jugador
					    informacion.append( m+"\n" );
				} // fin del m�todo run
			} // fin de la clase interna an�nima
					);
		}
		
		
		public void setImagen( int x, int y)
		{
			
			imagen = new ImageIcon("src/images/"+tablero[x][y].getTipo()+".png");
			tablero[x][y].setIcon(imagen);
			
			
		}
		
		public void enviarInfo(String p)
		{
			if(miTurno)
			{
			System.out.println(p);
			/*System.out.println(casillaAux.getTipo());
			System.out.println(casillaAux.getEstado());*/
			System.out.println(miIdentidad);
			salida.format("%s\n",p);
			salida.flush();
			miTurno = false;
			}
		}
		
		public class EventosMouse implements MouseListener
		{

			@Override
			public void mouseClicked(MouseEvent e) {
				
				posicion = "";
				posX = ((Casilla) e.getComponent()).getUbicacionX();
				posY = ((Casilla) e.getComponent()).getUbicacionY();
				//System.out.println("hola");
				if(miTurno)
				{
				mostrarMensaje("true"+miIdentidad);
				
				}else 
					mostrarMensaje("false"+miIdentidad);
				
				
				/*System.out.println(posX);
				System.out.println(posY);*/
				posicion += Integer.toString(posX)+Integer.toString(posY);
				
				enviarInfo(posicion);
				
			
				
				/*for(int i = 0; i<10; i++)
				{
					for(int j = 0; j<9; j++)
					{
						System.out.print(tablero[i][j].getTipo()+" ");
					}
					System.out.println();
				}*/
			
				
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}

		
		// fin del m�todo iniciarCliente

		
		
		
		
		
}
