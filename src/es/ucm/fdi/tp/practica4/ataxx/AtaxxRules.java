package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;

public class AtaxxRules implements GameRules{


	// This object is returned by gameOver to indicate that the game is not
	// over. Just to avoid creating it multiple times, etc.
	//
	protected final Pair<State, Piece> gameInPlayResult = new Pair<State, Piece>(State.InPlay, null);

	private int rows, cols, obs;

	public AtaxxRules(int numRows, int numCols, int obs) { //Puede ser rectangular, cambiar
		//dim = filas+columnas;
		if (numCols < 5 || numRows < 5) {
			throw new GameError("Dimension must be at least 5: " + numRows + " x " + numCols);
		} else {
			if (numCols % 2 == 0 || numRows % 2 == 0) {
				throw new GameError("Dimension must be odd: " + numRows + " x " + numCols);
			} else {
				rows  = numRows;
				cols = numCols;
				this.obs = obs;
			}
		}
	}

	@Override
	public String gameDesc() {
		return "Ataxx " + rows + "x" + cols;
	}

	@Override
	public Board createBoard(List<Piece> pieces) {
		//rellenar con  obstaculos aqui
		Board b = new FiniteRectBoard(rows, cols);
		//Para dos jugadores
		b.setPosition(0, 0, pieces.get(0));
		b.setPosition(rows-1, cols-1, pieces.get(0));
		b.setPosition(rows-1, 0, pieces.get(1));
		b.setPosition(0, cols-1, pieces.get(1));
		b.setPieceCount(pieces.get(0), 2);
		b.setPieceCount(pieces.get(1), 2);
		if (pieces.size() > 2) { //Tres jugadores
			b.setPosition((rows/2), 0, pieces.get(2));
			b.setPosition((rows/2), cols-1, pieces.get(2));
			b.setPieceCount(pieces.get(2), 2);
			if (pieces.size()==4) { //Cuatro jugadores	
				b.setPosition(0, (cols/2), pieces.get(3));
				b.setPosition(rows-1, (cols/2), pieces.get(3));
				b.setPieceCount(pieces.get(3), 2);
			}
		}
		generateObstacles(this.obs, b);
		return b;
	}

	public Board generateObstacles(int obs, Board b) {
		
		Piece ob = new Piece("*");
		
		int row, col;

		for (int i = 0; i < obs; i++){
			do {
				row = Utils.randomInt(rows);
				col = Utils.randomInt(cols);	
			} while(b.getPosition(row, col) != null);
			b.setPosition(row, col, ob);
		}
		return b;	
	}
	
	@Override
	public Piece initialPlayer(Board board, List<Piece> playersPieces) {
		return playersPieces.get(0);
	}

	@Override
	public int minPlayers() {
		return 2;
	}

	@Override
	public int maxPlayers() {
		return 4;
	}

	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> playersPieces, Piece lastPlayer) {
		/* Se acaba al quedarse sin piezas porque todas desaparezcan o
		 * porque el tablero esté lleno entero
		 */
		
		Piece p;
		
		//setPieceCount(Piece p, Integer n);
	/*
		// check rows & cols
		for (int i = 0; i < dim; i++) {
			// row i
			p = board.getPosition(i, 0);
			if (p != null) {
				j = 1;
				while (j < dim && board.getPosition(i, j) == p)
					j++;
				if (j == dim)
					return new Pair<State, Piece>(State.Won, p);
			}

			// col i
			p = board.getPosition(0, i);
			if (p != null) {
				j = 1;
				while (j < dim && board.getPosition(j, i) == p)
					j++;
				if (j == dim)
					return new Pair<State, Piece>(State.Won, p);
			}
		}

		// diagonal 1 - left-up to right-bottom
		p = board.getPosition(0, 0);
		if (p != null) {
			j = 1;
			while (j < dim && board.getPosition(j, j) == p) {
				j++;
			}
			if (j == dim) {
				return new Pair<State, Piece>(State.Won, p);
			}
		}

		// diagonal 2 - left-bottom to right-up
		p = board.getPosition(dim - 1, 0);
		if (p != null) {
			j = 1;
			while (j < dim && board.getPosition(dim - j - 1, j) == p) {
				j++;
			}
			if (j == dim) {
				return new Pair<State, Piece>(State.Won, p);
			}
		}

		*/
		//Al ser solo dos jugadores, si uno se queda sin piezas el otro gana, PERFECT
		if (playersPieces.size() == 2) {  
			if (board.getPieceCount(playersPieces.get(0)) == 0) {
				return new Pair<State, Piece>(State.Won, playersPieces.get(1));
			} else if (board.getPieceCount(playersPieces.get(1)) == 0) {
				return new Pair<State, Piece>(State.Won, playersPieces.get(0));
			}
		}
		
		//Si algún jugador se queda sin piezas, se elimina. PERFECT
		for (int i = 0; i < playersPieces.size(); ++i){
			if (board.getPieceCount(playersPieces.get(i)) == 0){
				//Notificar que se ha eliminado el jugador?
				playersPieces.remove(i);
				//System.out.println('\n' + "Eliminado jugador: " + lastPlayer.getId() + '\n');
				System.out.println('\n' + "Eliminado jugador: " + playersPieces.get(i).getId() + '\n');

			}
		}
		
		//
		//Si tablero lleno
		if (board.isFull()) { //Quien más piezas es el ganador
			p = obtBigger(board, playersPieces);
			
			if(p == null){
				return new Pair<State, Piece>(State.Draw, p);
			}else{
				return new Pair<State, Piece>(State.Won, p);
			}
			
			
		}
		
		
		
			
			//return new Pair<State, Piece>(State.Draw, null);
		

		return gameInPlayResult;
	}
	
	Piece obtBigger(Board board, List<Piece> playersPieces){ //Contemplar la posibilidad de que sean iguales para poder llegar a tablas
		List<Piece> pieces = playersPieces;
		Piece p;
		int gandAux;
		int cont1 = board.getPieceCount(playersPieces.get(0));
		int cont2 = board.getPieceCount(playersPieces.get(1));
		if (cont1 > cont2){
			p =  pieces.get(0);	
			gandAux = cont1;
		} else if(cont1 < cont2) {
			p =  pieces.get(1);
			gandAux = cont2;
		}
		else{
			p = null; //Hay tablas entre los dos primeros
			gandAux = cont2;
		}
		if (playersPieces.size() > 2) { //Si hay mas de dos jugadores...
			int cont3 = board.getPieceCount(playersPieces.get(2));
			if (cont3 > gandAux) {
				p =  pieces.get(2);	
				gandAux = cont3;
			} else if (cont3 == gandAux){ //Si hay tablas con el anterior ganador...
				p = null;
				
			}
			if (playersPieces.size() == 4) { //Si hay cuatro jugadores ...
				int cont4 = board.getPieceCount(playersPieces.get(4));
				if (cont4 > gandAux){ 
					p = pieces.get(4);
					gandAux = cont4;
				} else if (cont4 == gandAux){//Si el cuarto empata con el anterior ganador...
					p = null;
				
				}
			}
		}
		return p;	
	}
	
	@Override
	public Piece nextPlayer(Board board, List<Piece> playersPieces, Piece lastPlayer) {
		List<Piece> pieces = playersPieces;
		int i = pieces.indexOf(lastPlayer);
		Piece next = pieces.get((i + 1) % pieces.size());
		/*if(validMoves(board,playersPieces, next).size() == 0){
			return pieces.get((i + 2) % pieces.size());
		}else{
			return next;
		}*/
		int index = 0;
		while (validMoves(board,playersPieces, next).size() == 0 && index  < playersPieces.size()){
			next = pieces.get((index + 1) % pieces.size());
			index++;
		}
		if (index == playersPieces.size()){
			return null;			
		}
		return next;
		
		
	}

	public double evaluate(Board board, List<Piece> playersPieces, Piece turn,  Piece p) {
		return 0;
	}

	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces, Piece turn) {
		List<GameMove> moves = new ArrayList<GameMove>();

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if (turn.equals(board.getPosition(i, j))) {
					for(int x = -2; x < 2; x++){
						for(int y = -2; y <= 2; y++){
							if((i + x >= 0) && (i + x < board.getRows()) && (j + y >= 0) && (j + y <board.getCols())){
								if(board.getPosition(i+x, j + y) == null){
									moves.add(new AtaxxMove(i,j,x+i,j+y, turn));
								}							
							}
						}						
					}
				}
			}
		}
		return moves;
	}


	
}
