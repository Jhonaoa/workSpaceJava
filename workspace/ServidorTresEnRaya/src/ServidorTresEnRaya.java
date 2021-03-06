import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Servidor que controla la l�gica de juego y administra los turnos de los jugadores
 * @author paolajr
 *
 */

public class ServidorTresEnRaya extends JFrame{

	private String[] tablero = new String[ 9 ]; // tablero de tres en raya
	private JTextArea areaSalida; // para imprimir los movimientos en pantalla
	private Jugador[] jugadores; // arreglo de objetos Jugador
	private ServerSocket servidor; // socket servidor para conectarse con los clientes
	private int jugadorActual; // lleva la cuenta del jugador que sigue en turno
	private final static String[] MARCAS = { "X", "O" }; // arreglo de marcas
	private final static int JUGADOR_X = 0; // constante para el primer jugador
	private final static int JUGADOR_O = 1; // constante para el segundo jugador
	private ExecutorService ejecutarJuego; // ejecuta a los jugadores
	private Lock bloqueoJuego; // para bloquear el juego y estar sincronizado
	private Condition otroJugadorConectado; // para esperar al otro jugador
	private Condition turnoOtroJugador; // para esperar el turno del otro jugador
	
	// establece el servidor y crea la GUI para mostrar mensajes en pantalla
	public ServidorTresEnRaya() {
		super( "Servidor de Tres en raya" ); 
	
		ejecutarJuego = Executors.newFixedThreadPool( 2 ); // crea el ejecutor de subprocesos con un subproceso para cada jugador
		bloqueoJuego = new ReentrantLock(); // crea el manejador de bloqueos para el juego
		otroJugadorConectado = bloqueoJuego.newCondition(); // asocia al bloqueo la condici�n para los dos jugadores conectados
		turnoOtroJugador = bloqueoJuego.newCondition(); //// asocia al bloqueo la condici�n para el turno del otro jugador
	
		// crea tablero de tres en raya vac�o.
		for ( int i = 0; i < 9; i++ ){
			tablero[ i ] = new String( "" );
		}
		
		// crea arreglo de jugadores
		jugadores = new Jugador[ 2 ]; 
		
		jugadorActual = JUGADOR_X; // establece el primer jugador como el jugador actual

		try{
			servidor = new ServerSocket( 12345, 2 ); // establece el servidor
		} // fin de try
		catch ( IOException excepcionES ){
			excepcionES.printStackTrace();
			System.exit( 1 ); //cierra la aplicaci�n
		} // fin de catch
	
		areaSalida = new JTextArea(); // crea objeto JTextArea para mostrar la salida
		add( areaSalida, BorderLayout.CENTER );
		areaSalida.setText( "Servidor esperando conexiones\n" );
	
		setSize( 300, 300 ); // establece el tama�o de la ventana
		setVisible( true ); // muestra la ventana
	} // fin del constructor de ServidorTresEnRaya
	
	// espera dos conexiones para poder jugar
	public void execute() {
		// espera a que se conecte cada cliente
		for ( int i = 0; i < jugadores.length; i++ ) {
			try {
				jugadores[ i ] = new Jugador( servidor.accept(), i ); //establece la conexi�n via Socket
				ejecutarJuego.execute( jugadores[ i ] ); // ejecuta el objeto Runnable jugador
			} // fin de try
			catch ( IOException excepcionES ) {
				excepcionES.printStackTrace();
				System.exit( 1 );
			} // fin de catch
		} // fin de for
	
		bloqueoJuego.lock(); // bloquea el juego para avisar al subproceso del jugador X que inicie
	
		try {
			jugadores[ JUGADOR_X ].establecerSuspendido( false ); // contin�a el jugador X
			otroJugadorConectado.signal(); // despierta el subproceso del jugador X
		} // fin de try
		finally{
			bloqueoJuego.unlock(); // desbloquea el juego despu�s de avisar al jugador X
		} // fin de finally
	} // fin del m�todo execute
	
	// muestra un mensaje del subproceso de ejecuci�n despachador de eventos de la GUI
	private void mostrarMensaje( final String mensajeAMostrar ){
		SwingUtilities.invokeLater( new Runnable() {
										 public void run() {
											 areaSalida.append( mensajeAMostrar ); // agrega el mensaje
										 } // fin del m�todo run
									   } // fin de la clase interna
								  ); // fin de la llamada a SwingUtilities.invokeLater
	} // fin del m�todo mostrarMensaje
	
	// determina si el movimiento es v�lido
	public boolean validarYMover( int ubicacion, int jugador ) {
		// mientras no sea el jugador actual, debe esperar su turno
		while ( jugador != jugadorActual ){
			bloqueoJuego.lock(); // bloquea el juego para esperar a que el otro jugador	haga su movmiento
			try{
				turnoOtroJugador.await(); // espera el turno de jugador
			} 
			catch ( InterruptedException excepcion ){
				excepcion.printStackTrace();
			} 
			finally{
				bloqueoJuego.unlock(); // desbloquea el juego despu�s de esperar
			} 
		} // fin de while
	
		// si la ubicaci�n no est� ocupada, realiza el movimiento
		if ( !estaOcupada( ubicacion ) ){
			tablero[ ubicacion ] = MARCAS[ jugadorActual ]; // establece el movimiento en el tablero
			jugadorActual = ( jugadorActual + 1 ) % 2; // cambia el jugador
	
			// permite al jugador saber que se realiz� un movimiento
			jugadores[ jugadorActual ].otroJugadorMovio( ubicacion );
	
			bloqueoJuego.lock(); // bloquea el juego para indicar al otro jugador que realice su movimiento
			
			try {
				turnoOtroJugador.signal(); // indica al otro jugador que debe continuar
			} 
			finally {
				bloqueoJuego.unlock(); // desbloquea el juego despues de avisar
			} 
		
			return true; // notifica al jugador que el movimiento fue v�lido
		} // fin de if
		else {
			  return false; // notifica al jugador que el movimiento fue inv�lido
		}
	} // fin del m�todo validarYMover
	
	// determina si la ubicaci�n est� ocupada
	public boolean estaOcupada( int ubicacion ){
		if ( tablero[ ubicacion ].equals( MARCAS[ JUGADOR_X ] ) || 
			 tablero [ ubicacion ].equals( MARCAS[ JUGADOR_O ] ) ) {
	     return true; // la ubicaci�n est� ocupada
		}
	    else{
	     return false; // la ubicaci�n no est� ocupada
	    }
	} // fin del m�todo estaOcupada
	
	
	public boolean matchHorizontal()
	{
		
		boolean aux = false;
		for(int i = 1; i<=7;i= i+=3)
		{
			
			if(tablero[i] != "" 
					&& tablero[i]== tablero[i-1] 
					&& tablero[i] == tablero[i+1] )
			{
				aux = true;
				break;
			}
			else
				aux = false;
			
		}
	
		 return aux;	 
	}
	
	public boolean matchVertical()
	{
		boolean aux = false;
		for(int i = 3; i<=5;i++)
		{
			if(tablero[i] != ""
					&& tablero[i]== tablero[i+3] 
					&& tablero[i] == tablero[i-3]  )
			{
				aux = true;
				break;
			}
			else
				aux = false;
		}
		
		 return aux;
			
	}
	
	public boolean matchDiagonal()
	{
		boolean aux = false;
		
			if(tablero[4] != ""
					&& tablero[4]== tablero[2] 
					&& tablero[4] == tablero[6] )
			{
				aux = true;
			}
			else if(tablero[4] != ""
					&& tablero[4]== tablero[0] 
					&& tablero[4] == tablero[8] )
				{
				aux = true;
				}
			else
				aux = false;
		
		
		 return aux;
		 
	}
	
	 public boolean empate()
	 {
		 boolean aux = false;
		 int contador = 0;
		 
		 for(int i= 0; i<9; i++)
		 {
			 if(tablero[i] == "X" || tablero[i] == "O")
			 {
				 contador++;
			 }
			 
		 }
		 
		 if(contador == 9)
		 {
			 aux = true;
		 }
		 else
			 aux = false;
		 
		 
		 return aux;
	 }
	// Pr�ctica:  Completar el  m�todo para determinar si termin� el juego
	public boolean seTerminoJuego(){
		
		boolean aux = false;
		if(matchHorizontal() == true  
				|| matchVertical()==true 
				|| matchDiagonal()==true
				|| empate() == true)
		{
			aux = true;
		}
		
		else aux = false;
		
	
		
		return aux; 
	} // fin del m�todo seTerminoJuego
	
	
	// la clase interna Jugador que maneja a cada Jugador como subproceso
	private class Jugador implements Runnable {
		private Socket conexion; // conexi�n con el cliente
		private Scanner entrada; // entrada del cliente
		private Formatter salida; // salida al cliente
		private int numeroJugador; // identifica al Jugador
		private String marca; // marca para este jugador
		private boolean suspendido = true; // indica si el subproceso est� suspendido
	
		// Constructor establece el Socket del Jugador y prepara el estado inicial
		public Jugador( Socket socket, int numero ){
			numeroJugador = numero; // almacena el n�mero de este jugador
			marca = MARCAS[ numeroJugador ]; // especifica la marca del jugador
			conexion = socket; // almacena socket para el cliente
			
			// obtiene los flujos del objeto Socket
			try {
				entrada = new Scanner( conexion.getInputStream() );
				salida = new Formatter( conexion.getOutputStream() );
			} 
			catch ( IOException excepcionES ){
				excepcionES.printStackTrace();
				System.exit( 1 );
			} // fin de catch
		} // fin del constructor de Jugador
	
		//env�a mensaje que indica que el otro jugador hizo un movimiento
		public void otroJugadorMovio( int ubicacion ){
			
			if(seTerminoJuego() == true && empate() == false)
			{
				salida.format( "El otro jugador ha ganado\n" );
				salida.format( "%d\n", ubicacion );
				salida.flush();
			}
			else if(empate() == true)
			{
				salida.format( "Hay un empate\n" );
				salida.format( "%d\n", ubicacion );
				salida.flush();
			}
			else
			
			salida.format( "El oponente realizo movimiento\n" );
			salida.format( "%d\n", ubicacion ); // env�a la ubicaci�n del movimiento
			salida.flush(); // vac�a la salida
		} // fin del m�todo otroJugadorMovio
			
		// controla la ejecuci�n del subproceso
		public void run(){
		
			// env�a al cliente su marca (X o O), procesa los mensajes del cliente
			try{
				mostrarMensaje( "Jugador " + marca + " conectado\n" );
				salida.format( "%s\n", marca ); // env�a la marca del jugador
				salida.flush(); // vac�a la salida
		        // si es el jugador X, espera a que llegue el otro jugador
				if ( numeroJugador == JUGADOR_X ){
					salida.format( "%s\n%s", "Jugador X conectado","Esperando al otro jugador\n" );
					salida.flush(); // vac�a la salida
					bloqueoJuego.lock(); // bloquea el juego para esperar al segundo jugador

					try{
						while( suspendido ){
							otroJugadorConectado.await(); // espera al jugador O
						} // fin de while
					} // fin de try
					catch ( InterruptedException excepcion ){
						excepcion.printStackTrace();
					} // fin de catch
					finally {
						bloqueoJuego.unlock(); // desbloquea el juego 
					} // fin de finally
		
					// env�a un mensaje que indica que el otro jugador se conect�
					salida.format( "El otro jugador se conecto. Ahora es su turno.\n" );
					salida.flush(); // vac�a la salida
				} // fin de if
				
				else {
					salida.format( "El jugador O se conecto, por favor espere\n" );
					salida.flush(); // vac�a la salida
				} // fin de else
		
				// mientras el juego no termine
				while ( !seTerminoJuego() ) {
					int ubicacion = 0; // inicializa la ubicaci�n del movimiento
					
					if ( entrada.hasNext() ){
						ubicacion = entrada.nextInt(); // obtiene la ubicaci�n del movimiento
					}
					// comprueba si el movimiento es v�lido
					if ( validarYMover( ubicacion, numeroJugador ) ) {
						mostrarMensaje( "\nubicacion: " + ubicacion );
						salida.format( "Movimiento valido.\n" ); // notifica al cliente
						salida.flush(); // vac�a la salida
					}// fin de if
					
					else{
						salida.format( "Movimiento invalido, intente de nuevo\n" );
						salida.flush(); // vac�a la salida
					} // fin de else
				} // fin de while
			} // fin de try
			finally {
				try {
					
					if (empate() == true)
					{
						salida.format( "Hay un empate!!" );
						salida.flush();
					} 
					
					else if (seTerminoJuego() == true && empate() == false)
					{
						salida.format( "Has ganado!!" );
						salida.flush();
					}
					
					
					conexion.close(); // cierra la conexi�n con el cliente
				} // fin de try
				catch ( IOException excepcionES ){
					excepcionES.printStackTrace();
					System.exit( 1 );
				} // fin de catch
				} // fin de finally
	} // fin del m�todo run
	
	// establece si se suspende el subproceso o no	
	public void establecerSuspendido( boolean estado ){
		suspendido = estado; // establece el valor de suspendido
	} // fin del m�todo establecerSuspendido

  } // fin de la clase Jugador	
				
}//Fin clase ServidorTresEnRaya
