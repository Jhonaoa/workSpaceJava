import javax.swing.JFrame;

public class ClientePrincipal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClienteBuscaminas buscaminas= new ClienteBuscaminas("127.0.0.1");
		buscaminas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
