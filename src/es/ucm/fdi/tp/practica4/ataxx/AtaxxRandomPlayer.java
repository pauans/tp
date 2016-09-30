package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;
import es.ucm.fdi.tp.practica4.ataxx.Posicion;
import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AtaxxRandomPlayer extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		if (board.isFull()) {
			throw new GameError("The board is full, cannot make a random move!!");
		}
		
		int rows = board.getRows();
		int cols = board.getCols();
		int index = 0;
		int currRow, currCol, row, col; 
		Posicion[] posiciones = new Posicion[board.getRows() * board.getCols()];
		board.getRows();
		board.getCols();
		//Recorrer todo el tablero y buscar las piezas que sean del jugador del turno y clasificarlas en una matriz de booleans
		for (int i=0; i < board.getRows(); i++) {
			for (int j=0; j < board.getCols(); j++){
				if (board.getPosition(i, j) == p) {
					posiciones[index] = new Posicion(i,j);
					index++;
				} 
			}
		}
		
		int curr = Utils.randomInt(index);
		currRow = posiciones[curr].getRow();
		currCol = posiciones[curr].getColumn();
	
		
		// Tenemos la pieza a mover, seleccionar a dónde se mueve
		do {
			do{
				row = Utils.randomInt(rows);
				col = Utils.randomInt(cols);	
								
			} while (((Math.abs(row - currRow)) > 2) || ((Math.abs(col - currCol)) > 2));
			
			/*do{
				row = (int) (Math.random()*(currRow + 1) + (currRow - 2)); 
				col = (int) (Math.random()*(currCol + 1) + (currCol - 2)); 
			} while (((row >= rows) && (row < 0)) && ((col >= cols) && (col < 0))); 
			*/
			
			//Salta lejos, pero no siempre. ESTO MAL.
		} while (board.getPosition(row, col) != null);
			
		return createMove(row, col, currRow, currCol, p);
	}	
	/**
	 * Creates the actual move to be returned by the player. Separating this
	 * method from {@link #requestMove(Piece, Board, List, GameRules)} allows us
	 * to reuse it for other similar games by overriding this method.
	 * 
	 * <p>
	 * Crea el movimiento concreto que sera devuelto por el jugador. Se separa
	 * este metodo de {@link #requestMove(Piece, Board, List, GameRules)} para
	 * permitir la reutilizacion de esta clase en otros juegos similares,
	 * sobrescribiendo este metodo.
	 * 
	 * @param row
	 *            row number.
	 * @param col
	 *            column number..
	 * @param p
	 *            Piece to place at ({@code row},{@code col}).
	 * @return
	 */
	protected GameMove createMove(int row, int col, int rowO, int colO, Piece p) {
		return new AtaxxMove(row, col, rowO, colO, p);
	}
	
}
