import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Clase que implementa al jugador de Tres en Raya
 * @author paolajr
 *
 */
public class ClienteTresEnRaya extends JFrame implements Runnable {

	private JTextField campoId; // campo de texto para mostrar la marca del jugador
	private JTextArea areaPantalla; // objeto JTextArea para mostrar la salida
	private JPanel panelTablero; // panel para el tablero de tres en raya
	private JPanel panel2; // panel que contiene el tablero
	private Cuadro tablero[][]; // tablero de tres en raya
	private Cuadro cuadroActual; // el cuadro actual
	private Socket conexion; // conexi�n con el servidor
	private Scanner entrada; // entrada del servidor
	private Formatter salida; // salida al servidor
	private String hostTresEnRaya; // nombre de host para el servidor
	private String miMarca; // la marca de este cliente
	private boolean miTurno; // determina de qu� cliente es el turno
	private final String MARCA_X = "X"; // marca para el primer cliente
	private final String MARCA_O = "O"; // marca para el segundo cliente
	
	// Constructor de la clase que establece la GUI y el tablero
	public ClienteTresEnRaya( String host ){
		hostTresEnRaya = host; // establece el nombre del servidor
		areaPantalla = new JTextArea( 4, 30 ); // establece objeto JTextArea
		areaPantalla.setEditable( false );
		add( new JScrollPane( areaPantalla ), BorderLayout.SOUTH );
	
		panelTablero = new JPanel(); // establece panel para los cuadros en el tablero
		panelTablero.setLayout( new GridLayout( 3, 3, 0, 0 ) );
		tablero = new Cuadro[ 3 ][ 3 ]; // crea el tablero
	
		// itera a trav�s de las filas en el tablero
		for ( int fila = 0; fila < tablero.length; fila++ ){
			// itera a trav�s de las columnas en el tablero
			for ( int columna = 0; columna < tablero[ fila ].length; columna++ ){
				// crea un cuadro
				tablero[ fila ][ columna ] = new Cuadro( " " , fila * 3 + columna );
				panelTablero.add( tablero[ fila ][ columna ] ); // agrega el cuadro
			} // fin de for interior
		} // fin de for exterior
	
		campoId = new JTextField(); // establece campo de texto
		campoId.setEditable( false );
		add( campoId, BorderLayout.NORTH );

		panel2 = new JPanel(); // establece el panel que contiene a panelTablero
		panel2.add( panelTablero, BorderLayout.CENTER ); // agrega el panel del tablero
		add( panel2, BorderLayout.CENTER ); // agrega el panel contenedor
	
		setSize( 325, 225 ); // establece el tama�o de la ventana
		setVisible( true ); // muestra la ventana
		iniciarCliente();
	} // fin del constructor de ClienteTresEnRaya
	
	
	// inicia el subproceso cliente
	public void iniciarCliente(){
		// se conecta al servidor, obtiene los flujos e inicia subproceso de salida
		
		try {
			conexion = new Socket(InetAddress.getByName( hostTresEnRaya ), 12345 );
	
			entrada = new Scanner( conexion.getInputStream() );
			salida = new Formatter( conexion.getOutputStream() );
		}
		catch ( IOException excepcionES ){
			excepcionES.printStackTrace();
		} 

	// crea e inicia subproceso trabajador para este cliente
	ExecutorService trabajador = Executors.newFixedThreadPool( 1 );
	trabajador.execute( this ); // ejecuta el cliente
	} // fin del m�todo iniciarCliente
	
	// subproceso de control que permite la actualizaci�n continua de areaPantalla	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		miMarca = entrada.nextLine(); // obtiene la marca del jugador (X o O)
		SwingUtilities.invokeLater(new Runnable() {
													public void run(){
															// muestra la marca del jugador
														    campoId.setText( "Usted es el jugador \"" + miMarca + "\"" );
													} // fin del m�todo run
										} // fin de la clase interna an�nima
									); // fin de la llamada a SwingUtilities.invokeLater
			
		miTurno = ( miMarca.equals( MARCA_X ) ); // determina si es turno del cliente
		
		// recibe los mensajes que se env�an al cliente y los imprime en pantalla
		while ( true ) {
			if ( entrada.hasNextLine() )
			 procesarMensaje( entrada.nextLine() );
		} // fin de while
	}//Fin del run
	
	// procesa los mensajes recibidos por el cliente
	private void procesarMensaje( String mensaje ){
		
		if ( mensaje.equals( "Movimiento valido." ) ) {
			mostrarMensaje( "Movimiento valido, por favor espere.\n" );
			establecerMarca( cuadroActual, miMarca ); // establece marca en el cuadro
		} 
		else if ( mensaje.equals( "Movimiento invalido, intente de nuevo" ) ){
	 			mostrarMensaje( mensaje + "\n" ); // muestra el movimiento inv�lido
	 			miTurno = true; // sigue siendo turno de este cliente
				} // fin de else if
				else if ( mensaje.equals( "El oponente realizo movimiento" ) ){
						int ubicacion = entrada.nextInt(); // obtiene la ubicaci�n del movimiento
						entrada.nextLine(); // salta nueva l�nea despu�s de la ubicaci�n 
						int fila = ubicacion / 3; // calcula la fila
						int columna = ubicacion % 3; // calcula la columna
						establecerMarca( tablero[ fila ][ columna ],( miMarca.equals( MARCA_X ) ? MARCA_O : MARCA_X ) ); // marca el movimiento
						mostrarMensaje( "El oponente hizo un movimiento. Ahora es su turno.\n" );
						miTurno = true; // ahora es turno de este cliente
						
				
				}
				else if(mensaje.equals("El otro jugador ha ganado"))
				{
					int ubicacion = entrada.nextInt(); // obtiene la ubicaci�n del movimiento
					entrada.nextLine(); // salta nueva l�nea despu�s de la ubicaci�n 
					int fila = ubicacion / 3; // calcula la fila
					int columna = ubicacion % 3; // calcula la columna
					establecerMarca( tablero[ fila ][ columna ],( miMarca.equals( MARCA_X ) ? MARCA_O : MARCA_X ) ); // marca el movimiento
					mostrarMensaje("El otro jugador ha ganado.\n");
					miTurno = false;
				}
				else if(mensaje.equals("Hay un empate"))
				{
					int ubicacion = entrada.nextInt(); // obtiene la ubicaci�n del movimiento
					entrada.nextLine(); // salta nueva l�nea despu�s de la ubicaci�n 
					int fila = ubicacion / 3; // calcula la fila
					int columna = ubicacion % 3; // calcula la columna
					establecerMarca( tablero[ fila ][ columna ],( miMarca.equals( MARCA_X ) ? MARCA_O : MARCA_X ) ); // marca el movimiento
					mostrarMensaje("Hay un empate!!\n");
					miTurno = false;
				}
					// fin de else if
				else
					mostrarMensaje( mensaje + "\n" ); // muestra el mensaje
	} // fin del m�todo procesarMensaje
	
	// actualiza areaSalida en el subproceso despachador de eventos
	private void mostrarMensaje( final String mensajeAMostrar ){
		SwingUtilities.invokeLater( new Runnable() {
										 public void run() {
											areaPantalla.append( mensajeAMostrar ); // actualiza la salida
										 } // fin del m�todo run
									 } // fin de la clase interna
								  ); // fin de la llamada a SwingUtilities.invokeLater
	} // fin del m�todo mostrarMensaje
	
	
	// m�todo utilitario para establecer una marca en el tablero, en el subproceso despachador de eventos
	private void establecerMarca( final Cuadro cuadroAMarcar, final String marca ){
		SwingUtilities.invokeLater( new Runnable() {
											public void run(){
												 cuadroAMarcar.establecerMarca( marca );
											} // fin del m�todo run
									 } // fin de la clase interna an�nima
								  ); // fin de la llamada a SwingUtilities.invokeLater
    } // fin del m�todo establecerMarca
	
	// comunica al servidor cual fue el cuadro en el que se hizo clic
	public void enviarCuadroClic( int ubicacion ) {
		
	    if ( miTurno ){
	    	 salida.format( "%d\n", ubicacion ); // env�a la ubicaci�n al servidor
	    	 salida.flush();
	    	 miTurno = false; // ya no es mi turno
	    } // fin de if
	} // fin del m�todo enviarCuadroClic
	
	// establece el cuadro actual
	public void establecerCuadroActual( Cuadro cuadro ) {
		cuadroActual = cuadro; // asigna el argumento al cuadro actual
	} // fin del m�todo establecerCuadroActual
	
	
	// clase interna privada para los cuadros en el tablero
	private class Cuadro extends JPanel {
		private String marca; // marca a dibujar en este cuadro
		private int ubicacion; // ubicacion del cuadro
	
		public Cuadro( String marcaCuadro, int ubicacionCuadro ){
			marca = marcaCuadro; 
			ubicacion = ubicacionCuadro; 
			
			addMouseListener( new MouseAdapter() {
									public void mouseReleased( MouseEvent e ){
											establecerCuadroActual( Cuadro.this ); // establece el cuadro actual
											enviarCuadroClic( obtenerUbicacionCuadro() );  // env�a la ubicaci�n de este cuadro
											
									} // fin del m�todo mouseReleased
								  } // fin de la clase interna an�nima
							 ); // fin de la llamada a addMouseListener
			} // fin del constructor de Cuadro
	
		// devuelve la ubicaci�n del objeto Cuadro
		public int obtenerUbicacionCuadro(){
			return ubicacion; // devuelve la ubicaci�n del cuadro
		} // fin del m�todo obtenerUbicacionCuadro
		
		public void establecerMarca( String nuevaMarca ){
			marca = nuevaMarca; // establece la marca del cuadro
			repaint(); // vuelve a pintar el cuadro
		} // fin del m�todo establecerMarca
		
		// dibuja el objeto Cuadro
		public void paintComponent( Graphics g ){
			super.paintComponent( g );
			g.drawRect( 0, 0, 29, 29 ); // dibuja el cuadro
			g.drawString( marca, 11, 20 ); // dibuja la marca
		} // fin del m�todo paintComponent
		
		
		// devuelve el tama�o preferido del objeto Cuadro
		public Dimension getPreferredSize() {
			return new Dimension( 30, 30 ); 
		} // fin del m�todo getPreferredSize
		
		// devuelve el tama�o m�nimo del objeto Cuadro
		public Dimension getMinimumSize(){
			return getPreferredSize(); 
		} // fin del m�todo getMinimumSize
			
				
	}//Fin clase interna Cuadro
	
}//Fin clase ClienteTresEnRaya
