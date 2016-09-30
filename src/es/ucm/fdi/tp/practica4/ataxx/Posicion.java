package es.ucm.fdi.tp.practica4.ataxx;

/**
 * Clase que determina la posición de la célula y las posibles posiciones a las que se puede mover
 */
public class Posicion {
	//Campos de la clase
	private int row, column;
	public enum Location {CORNER, EDGE, INNER}; //Posiciones tipo
	
	/**
	 * Método que obtiene la fila en la que está la célula
	 * @return Número de fila
	 */
	public int getRow(){
		return this.row;
	}
	
	/**
	 * Método que obtiene la columna en la que está la célula
	 * @return Número de columna
	 */
	public int getColumn(){
		return this.column;
	}
	
	/**
	 * Constructor de posiciones
	 * @param i Indica la fila en la que está la célula
	 * @param j Indica la columna en la que está la célula
	 */
	public Posicion(int i, int j){
		row = i;
		column = j;
	} //Cierre del constructor
	
	/**
	 * Método que determina la posición tipo en la que se encuentra la célula
	 * @param pos Posición exacta en la que se encuentra la célula
	 * @param rows Número máximo de filas alcanzables
	 * @param columns Número máximo de columnas alcanzables
	 * @return Posición tipo en la que está la célula
	 */
	public Location location(Posicion pos, int rows, int columns){
		Location location;
		
		if ((pos.column == 0 || pos.column == columns-1 ) && (pos.row == 0 || pos.row == rows-1 )){
			location = Location.CORNER;			
		}
		else if ((pos.column == 0) ^ (pos.column ==columns-1 ) ^ (pos.row == 0 ^ pos.row == rows-1 )){
			location = Location.EDGE;			
		}else{
			location = Location.INNER;
		}
		return location;
	}
	
	/**
	 * Método que crea el array de posiciones vecinas de la célula respetando los límites de la superficie y 
	 * adaptándose a la posición tipo de la célula
	 * @param rows Número máximo de filas alcanzables
	 * @param columns Número máximo de columnas alcanzables
	 * @return Array de posiciones vecinas
	 */
	public Posicion[] neighboringPositions(int rows, int columns){
		Posicion[] neighboring = null ; //Creamos el array vacío
	
		switch (this.location(this, rows, columns)){
		case CORNER:{
			neighboring = new Posicion[3];
		} break;
		case EDGE:{
			neighboring = new Posicion[5];
		} break;
		case INNER:{
			neighboring = new Posicion[8];	
		} break;
		default:{
			//Detección de errores
		}	
		}
		int index  = 0; //Índice para recorrer array de vecinas
		for (int i = row-1; i<row+2; i++){
			for(int j = column-1; j < column+2; j++) {
				if (j != column || i != row){ //Comprobar que no es la misma que llama al procedimiento
					if( (i < rows) && (j < columns) && (i > -1) && (j > -1)){ 
						neighboring[index] = new Posicion(i,j); 
						index++;
					}
				}
			}
		}
		return neighboring;
	}
	
	/**
	 * Método que muestra la posición en la que está la célula
	 */
	public String toString (){
		String devuelto = new String();
		devuelto = "(" + row + "," + column + ")";		
		return devuelto;
	}
	
} // Cierre de la clase posición
