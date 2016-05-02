import java.awt.*;
import java.awt.event.*;
import javax.swing.*;;

/**
 * Esta clase pemrite construir la ventana gráfica con la cual interactuará el usuario durante el juego de Craps
 * debe permitir visualizar los dados, lanzar y visualizar el resultado del juego.
 * 
 * @author paolajr
 *@since 01/08/2000
 *@version 1.0
 */
public class Vista extends JFrame implements ActionListener{
	
	private JLabel dado1, dado2, etiquetaLanzar, punto;
	private JButton lanzar;
	private JTextField tiro, valorPunto;
	
	private Icon imagen;
	private Craps control;
	
	/**
	 * Constructor de la Clase que permite establecer la Configuración inicial de la GUI
	 */
	public Vista(){
		super("Juego de Craps"); //invoca el constructor de la Superclase
		
		Container contenedor = getContentPane();
		contenedor.setLayout(new FlowLayout());
		
		imagen = new ImageIcon("src/imagenes/dado.png");
		dado1 = new JLabel(imagen);
		dado2 = new JLabel(imagen);
			
		contenedor.add(dado1);
		contenedor.add(dado2);
		
		lanzar = new JButton("Lanzar");
		contenedor.add(lanzar);
		lanzar.addActionListener(this);
		
		etiquetaLanzar = new JLabel("Lanzaste: ");
		etiquetaLanzar.setVisible(false);
		contenedor.add(etiquetaLanzar);
		
		tiro = new JTextField(3);
		tiro.setVisible(false);
		contenedor.add(tiro);
		
		punto = new JLabel("Tu punto es: ");
		punto.setVisible(false);
		contenedor.add(punto);
		
		valorPunto = new JTextField("");
		valorPunto.setVisible(false);
		contenedor.add(valorPunto);
		
		
		control = new Craps();
		
		setSize(420, 250);
		setVisible(true);		
	}
	
    //Método manejar de eventos (Escucha del boton lanzar)
	//Este método pertence a la interfaz ActionListener y debe ser sobreescrito 
	
	@Override
	public void actionPerformed(ActionEvent evento) {
		// TODO Auto-generated method stub
		
		if (evento.getSource() == lanzar) {
			
			visualizarCaras();
			
			control.determinarJuego();
			int resultado = control.getEstado();	
			
			if(resultado==1){
				JOptionPane.showMessageDialog(null,"Has Ganado!!");
				etiquetaLanzar.setVisible(false);
				tiro.setVisible(false);
				punto.setVisible(false);
				valorPunto.setVisible(false);
			}
				
			if (resultado==2){
				JOptionPane.showMessageDialog(null,"Lo siento ... Has Perdido!!");
				etiquetaLanzar.setVisible(false);
				tiro.setVisible(false);
				punto.setVisible(false);
				valorPunto.setVisible(false);
			}
				
			if(resultado==3){		
				JOptionPane.showMessageDialog(null,"Has establecido punto \n Sigue lanzando hasta sacar " + control.getPunto()
							                      + " pero si antes sacas 7 perderás!!");
				valorPunto.setText(Integer.toString(control.getPunto()));
					
				punto.setVisible(true);
				valorPunto.setVisible(true);
			}		
									
		}		
	}

	public void visualizarCaras(){
		int[] caras;
		caras = control.calcularTiro();
		
		imagen = new ImageIcon("src/imagenes/"+caras[0]+".png");
		dado1.setIcon(imagen);
		imagen = new ImageIcon("src/imagenes/"+caras[1]+".png");
		dado2.setIcon(imagen);	
		
		etiquetaLanzar.setVisible(true);
		tiro.setText(Integer.toString(control.getTiro()));
		tiro.setVisible(true);
	
	}
	
}
