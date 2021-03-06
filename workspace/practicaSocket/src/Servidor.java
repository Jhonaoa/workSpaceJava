

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.*;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame  implements Runnable{
	
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		Thread miHilo = new Thread(this);
		
		miHilo.start();
		}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket servidor = new ServerSocket(8888);
			
			String nick,ip,mensaje;
			
			Paquete paquete_recibido;
			while(true)
			{
			
			Socket miSocket = servidor.accept();
			
			ObjectInputStream paquete_datos = new ObjectInputStream(miSocket.getInputStream());
			
			paquete_recibido =(Paquete) paquete_datos.readObject();
			
			nick =  paquete_recibido.getNick();
			
			ip = paquete_recibido.getIp();
			
			mensaje = paquete_recibido.getMensaje();
			
			
			/*DataInputStream flujo_entrada = new DataInputStream(miSocket.getInputStream());
			
			String  mensaje = flujo_entrada.readUTF();
			
			areatexto.append("\n" + mensaje);
			*/
			
			areatexto.append("\n" +nick +": "+mensaje +" para: "+ ip);
			
			
			miSocket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private	JTextArea areatexto;
}

class Paquete implements Serializable
{
	private String nick, ip, mensaje;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}