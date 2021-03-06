

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.*;
import java.net.*;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel{
	
	public LaminaMarcoCliente(){
	
		nick = new JTextField(5);
		add(nick);
		
		JLabel texto=new JLabel("-CHAT-");
		add(texto);
		
		ip = new JTextField(8);
		add(ip);
		
		
		
		campoChat = new JTextArea(12,20);
		
		add(campoChat);
		
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		
		EnviaTexto mievento = new EnviaTexto();
		miboton.addActionListener(mievento);
		add(miboton);	
		
	}
	
	
	
	private class EnviaTexto implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println(campo1.getText());
			
			try {
				Socket miSocket = new Socket("127.0.0.1", 8888);
				
				Paquete datos = new Paquete();
				
				datos .setNick(nick.getText());
				
				datos.setIp(ip.getText());
				
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());
				
				paquete_datos.writeObject(datos);
				
				miSocket.close();
				/*DataOutputStream flujo_salida = new DataOutputStream(miSocket.getOutputStream());
				
				flujo_salida.writeUTF(campo1.getText());
				
				flujo_salida.close();*/
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
		
	}
		
		
	private JTextField campo1,nick,ip;
	
	private JTextArea campoChat;
	private JButton miboton;
	
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