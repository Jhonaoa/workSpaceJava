import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Vista extends JFrame implements ActionListener {
	
	private JButton ingresar;
	
	private JTextField letraIng,letra1,letra2,letra3,letra4,letra5;
	
	private JLabel etiquetaLetra,horca;
	
	private Icon imagen;
	
	private Ahorcado control;
	
	public Vista()
	{
		super("Ahorcado");
		
		Container contenedor = getContentPane();
		contenedor.setLayout(new FlowLayout());
		
		imagen = new ImageIcon("src/imagenes/horca.png");
		horca = new JLabel(imagen);
		letraIng = new JTextField(2);
		letra1 = new JTextField(4);
		letra2 = new JTextField(4);
		letra3 = new JTextField(4);
		letra4 = new JTextField(4);
		letra5 = new JTextField(4 );
		ingresar = new JButton("ingresar");
		etiquetaLetra = new JLabel("Letra ingresada: ");
		
		
		
		
		contenedor.add(etiquetaLetra);
		contenedor.add(letraIng );
		contenedor.add(ingresar);
		ingresar.addActionListener(this);
		contenedor.add(horca);
		
		contenedor.add(letra1);
		contenedor.add(letra2);
		contenedor.add(letra3);
		contenedor.add(letra4);
		contenedor.add(letra5);
		
		
		control = new Ahorcado();
		setSize(700, 600);
		setVisible(true);
	}
	

	public void actionPerformed(ActionEvent evento)
	{
		
		if (evento.getSource() == ingresar)
			
		{
			control.generarPalabra();
			
			
			
			
			String aux = letraIng.getText();
			
			if (aux.length() != 1)
			{
				JOptionPane.showMessageDialog(null,"ingreso no válido");
			}
			
				else
				{
					control.setLetraIngresada(aux);
					control.compararLetra();
					int ocurrencia = control.getOcurrencia();
					int errores = control.getErrores();
				
					if(ocurrencia == 0)
					{
					
						imagen = new ImageIcon("src/imagenes/"+errores+".png");
						horca.setIcon(imagen);
					}
				
			
					
					else
					{
				
				
						letra1.setText(control.getLetraIngresada());
							
					}
				
				
				}
			
			
				
		}
	}
}


































