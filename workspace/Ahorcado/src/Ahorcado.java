import java.lang.Math;

public class Ahorcado {

	private String letraIngresada;
	private String[] palabras;
	private String palabra;
	private int ocurrencia;
	private int errores;
	private int[] posicion;
	private int aciertos;
	
	public Ahorcado()
	{
		palabras = new String[5];
		palabras[0] = "avion";
		palabras[1] = "barco";
		palabras[2] = "lista";
		palabras[3] = "hueva";
		palabras[4] = "carta";
		
		posicion = new int[5];
	}
	
	public void setLetraIngresada(String letra)
	{
		
		letraIngresada = letra;
		
	}
	
	public String getLetraIngresada()
	{
		return letraIngresada;
	}
	
	
	public void generarPalabra()
	{
		int aleatorio = 0 ;
		
		aleatorio = (int)(Math.random()*5);
		
		palabra = palabras[aleatorio];
		
	}
	
	
	public void compararLetra()
	{
		int pos = 0;
		int contador = 0;
		
		
		
		
		
		pos=palabra.indexOf(letraIngresada, pos);
		
		if(pos == -1)
		{
			
			ocurrencia = 0;
			errores++;
			
			
		} else 
		
			
		
			while (pos != -1)
			{
				posicion[contador] = pos;
				
				pos=palabra.indexOf(letraIngresada, pos+1);
				
				contador++;
			}
		contador = 0;
		
		while (posicion[contador] != 0)
		{
			aciertos++;
			
			contador++;
		}
		
	}



	public int getOcurrencia()
	{
		return ocurrencia;
	}


	public int getErrores()
	{
		return errores;
	}




	public String getPalabra()
	{
		return palabra;
	}




	public void borrarArray()
	{
		
		for(int i= 0; i < posicion.length;i++)
		{
			posicion[i] = 0;
		}
	}



	public int[] getPosicion()
	{
		return posicion;
	}







}
