import javax.swing.JFrame;

public class PruebaClienteTresEnRaya {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ClienteTresEnRaya aplicacion; // declara la aplicaci�n cliente
		// si no hay argumentos de l�nea de comandos
		if ( args.length == 0 ){
			aplicacion = new ClienteTresEnRaya( "127.0.0.1" ); // localhost
		}
		else {
			aplicacion = new ClienteTresEnRaya( args[ 0 ] ); // usa args
		}
		aplicacion.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}//fin main

}//fin clase PruebaCliente
