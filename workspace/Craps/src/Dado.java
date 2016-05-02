import java.lang.Math;

/**
 * Clase que representa un dado permitiendo obtener el valor de la cara visible al lanzarlo..
 * 
 * @author paolajr
 * @since 01/08/2000
 * @version 1.0
 *
 */

public class Dado {
	
	private int cara;
	

	private void lanzarDado(){
		cara=(int) (Math.random()*6 + 1);
	}
	
	
	/**
     * Método que devuelve el valor de la cara visible del dado una vez éste ha sido lanzado
     * @return El valor de la cara visible del dado (número aleatorio entre 1 y 6)
     */
	
	public int getCara(){
		lanzarDado();
		return cara;
	}
}
