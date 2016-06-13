import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Jugador implements Runnable{

	private Socket conexion; // conexi�n con el cliente
	private ObjectInputStream entrada; // entrada del cliente
	private ObjectOutputStream salida; // salida al cliente
	private int numeroJugador; // identifica al Jugador
	private boolean suspendido = true; // indica si el subproceso est� suspendido

	
	
	public Jugador (Socket socket)
	{
		conexion = socket;
		try {
			entrada = new ObjectInputStream(conexion.getInputStream());
			salida = new ObjectOutputStream(conexion.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		System.out.println("hablalo");
	}

	public int getNumeroJugador() {
		return numeroJugador;
	}

	public void setNumeroJugador(int numeroJugador) {
		this.numeroJugador = numeroJugador;
	}

	public boolean isSuspendido() {
		return suspendido;
	}

	public void setSuspendido(boolean suspendido) {
		this.suspendido = suspendido;
	}

}
