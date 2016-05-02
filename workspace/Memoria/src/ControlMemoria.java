
import java.lang.Math;
import javax.swing.ImageIcon;



public class ControlMemoria  {

	private Ficha[][] matriz;
	
	private int columnas;
	
	private int filas;
	ImageIcon imagen;
	
	
	public Ficha[][] generarMatriz()
	{
		
		matriz = new Ficha[filas][columnas];
		
		
		for(int i = 0; i<columnas;i++)
		{
			for(int j = 0; j<columnas; j++)
			{
				matriz[i][j] = new Ficha();
				
			}
		}
		
		return matriz;
	}
	
	
	public void setFilas(int F)
	{
		filas = F;
	}
	
	public void setColumnas(int C)
	{
		columnas = C;
	}
	
	public int getFilas()
	{
		return filas;
	}
	
	public int getColumnas()
	{
		return columnas;
	}
	
	public void modificarMatriz()
	{
		int aleatorio1 = 0;
		int aleatorio2 = 0;
		int acumulador = 0;
		
		for(int i = 1 ;i<=filas*columnas/2;i++)
		{
			 
			acumulador = 0;
			
			while(acumulador != 2)
			{
				aleatorio1 = (int)(Math.random()*4);
				aleatorio2 = (int)(Math.random()*4);
				//System.out.print(aleatorio1+" "+aleatorio2);
				if(matriz[aleatorio1][aleatorio2].getID() != 0)
				{
					matriz[aleatorio1][aleatorio2].setID(matriz[aleatorio1][aleatorio2].getID());
				}
				else
				{
					matriz[aleatorio1][aleatorio2].setID(i);
					acumulador++;
				}
				
				
			}
		}
		
	}
	
	
	public int[] buscar()
	{
		int [] aux = new int[16];
		int contador = -1;
		for(int i = 0; i<filas; i++)
	      {
	    	  for(int j = 0 ; j<columnas; j++)
	    	  {
					
	    		  if(matriz[i][j].getEstado() == true)
	    		  {
	    			  contador+=1;
	    			  aux[contador]=  matriz[i][j].getID();
	    		  }
	    			  
	    	  }
	    	 
	      }
		
		System.out.print(aux[0]);
		System.out.print(aux[1]);
		return aux;
		
	}
	
	
	public boolean compararFichas(int[] fichas)
	{
		if(fichas[0] == fichas[1])
		{
			return true;
			
		}
		else
			return false;
	}



	
}
