import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
public class VistaMemoria extends JFrame implements MouseListener {

	
		private JFrame contenedor  = new JFrame("Memory game");
		private JPanel panel = new JPanel();
		private ControlMemoria control = new ControlMemoria();
		private ImageIcon imagen;
		private Ficha[][] tablero;
		
		public VistaMemoria()
		{
			
			panel.setLayout(new GridLayout(4,4,10,10) );
			control.setColumnas(4);
			control.setFilas(4);
			tablero = control.generarMatriz();
			control.modificarMatriz();
			int aux1 = control.getFilas();
			int aux2 = control.getColumnas();
			
			imagen = new ImageIcon("src/images/fondo.gif");

			//System.out.print(imagen.getIconWidth());
			
			for(int i = 0; i<aux1;i++)
			{
				for(int j = 0 ; j< aux2;j++)
				{
					panel.add(tablero[i][j]);
					tablero[i][j].addMouseListener(this);
					tablero[i][j].setIcon(imagen);
					System.out.print(" "+tablero[i][j].getID());
					
				}
				System.out.println();
			}
		
			
		contenedor.setSize(630,630);
		
		contenedor.add(panel);
		contenedor.setVisible(true);
			
		}

		@SuppressWarnings("deprecation")
		@Override
		public void mouseClicked(MouseEvent e)
		{
			int aux1 = 0;
			int aux2 = 0;
			int contador=0;
			imagen = new ImageIcon("src/images/"+((Ficha) e.getComponent()).getID()+".png");
			// TODO Auto-generated method stub
			((AbstractButton) e.getComponent()).setIcon(imagen);
			((Ficha) e.getComponent()).setEstado(true);
			
			
			if(control.compararFichas(control.buscar()) == true )
			{
				for(int i = 0; i<control.getFilas();i++)
				{
					for(int j = 0; j< control.getColumnas();j++)
					{
						if(tablero[i][j].getID() == control.buscar()[0])
						{
							tablero[i][j].setEnabled(false);
							tablero[i][j].setEstado(false);
							
						}
					}
				}
			}
			/*else if (control.compararFichas(control.buscar()) == false)
			{
				for(int i = 0; i<control.getFilas();i++)
				{
					for(int j = 0; j< control.getColumnas();j++)
					{
						if(tablero[i][j].getEstado() == true)
						{
							imagen = new ImageIcon("src/images/fondo.gif");
							tablero[i][j].setEstado(false);
							tablero[i][j].setIcon(imagen);
						}
					}
				}
			}*/
			
			
			
			// System.out.print(((Ficha) e.getComponent()).getID());
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		

	
			

}
