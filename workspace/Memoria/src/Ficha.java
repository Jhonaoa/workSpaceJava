

import javax.swing.JButton;



public class Ficha extends JButton {
	
	private boolean estadoFicha;
	private int iD;
	
	public Ficha()
	{
		estadoFicha = false;
		iD = 0;
	}
	
	public void setEstado(boolean estado)
	{
		estadoFicha = estado;
	}
	
	public void setID(int id)
	{
		iD = id;
	}
	
	public boolean getEstado()
	{
		return estadoFicha;
	}
	
	public int getID()
	{
		return iD ;
	}

	
		
	
}
	

	