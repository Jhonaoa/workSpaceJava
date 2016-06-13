import java.io.EOFException;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.net.ServerSocket;
 import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.swing.SwingUtilities;



public class ServidorBuscaminas {
	
	private Jugador[] jugadores;
	private ServerSocket servidor; // socket servidor
	private int contador =0; // contador del n�mero de conexiones
	private ExecutorService ejecutarJuego;
	
	public ServidorBuscaminas()
	{
		ejecutarJuego = Executors.newFixedThreadPool( 2 );
		try {
			servidor = new ServerSocket(12345,2);
			System.out.println("conexion");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jugadores = new Jugador[2];
	
	}
	
	public void execute()
	{
		for ( int i = 0; i < jugadores.length; i++ ) {
			try {
				jugadores[i] = new Jugador( servidor.accept() );
				jugadores[i].setNumeroJugador(i);//establece la conexi�n via Socket
				System.out.println(jugadores[i].getNumeroJugador());
				ejecutarJuego.execute( jugadores[ i ] ); // ejecuta el objeto Runnable jugador
			} // fin de try
			catch ( IOException excepcionES ) {
				excepcionES.printStackTrace();
				System.exit( 1 );
				
			} // fin de catch
		} // fin de for
		
		
	}
	
	

}
