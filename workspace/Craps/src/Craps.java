
public class Craps {
	private Dado dado1, dado2;
	private int tiro, punto, estado, flag;
	
	public Craps(){
		dado1 = new Dado();
		dado2 = new Dado();
		flag=0;
	}
	
	public int[] calcularTiro(){
		int[] caras = new int[2];
		caras[0] = dado1.getCara();
		caras[1] = dado2.getCara();
		tiro = caras[0] +  caras[1];
	    return caras;
	}
	
	public void determinarJuego(){
		
	 if(flag==0){
		//Es primer lanzamiento
		if (tiro==7 || tiro == 11){
			estado=1; //ganó
		}
		else
			if(tiro==2 || tiro==3 || tiro==12){
				estado=2; //perdió
			}
			else{
				estado=3; //Estableció punto
				punto=tiro;
				flag = 1; 
			}
      }
	 else{ 
	    //Ha establecido punto
		rondaPunto(); 
	 }
	  
	}
	
	private void rondaPunto(){
		if(tiro==punto){
			estado=1;
			flag=0;
		}
		if(tiro==7){
			estado=2;
			flag=0;
		}
	}
	
	public int getTiro(){
		return tiro;
	}
	
	public int getEstado(){
		return estado;
	}
	
	public int getPunto(){
		return punto;
	}
	
	
}
