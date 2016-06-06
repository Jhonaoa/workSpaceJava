import java.lang.Math;



public class ControlPoker {
	
	private Carta[] mazo;
	private Carta[] comunes;
	
	public void ControlPoker()
	{
		mazo = new Carta[52];
		
		for(int i =0 ; i<52 ; i++)
		{
			mazo[i] = new Carta();
		}
		
		
	}
	
	public void llenarMazo()
	{
		int posicion = 0;
		for(int i= 1; i<=13 ; i++)
		{ 	
						
			for(int j=0; j<=3; j++)
			{
				mazo[posicion].setNumero(i);				
				mazo[posicion].setPalo(20+j);
				posicion++;
			}
		}
	}
	
	
	
	public void setComunes()
	{
		int aleatorio1= 0;
		int contador = 0;
		
		while(contador != 5)
		{
			aleatorio1 = (int)(Math.random()*52);
			
			while(mazo[aleatorio1].getNumero() != 0)
			{
				comunes[contador].setNumero(mazo[aleatorio1].getNumero());
				comunes[contador].setPalo(mazo[aleatorio1].getPalo());
				mazo[aleatorio1].setNumero(0);
				contador++;
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	

}
