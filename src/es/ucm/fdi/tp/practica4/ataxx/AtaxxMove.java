package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;

import java.lang.Math;

public class AtaxxMove extends GameMove {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The row where to place the piece return by {@link GameMove#getPiece()}.
	 * <p>
	 * Fila en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int row;

	/**
	 * The column where to place the piece return by {@link GameMove#getPiece()}
	 * .
	 * <p>
	 * Columna en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int col;

	/**
	 * This constructor should be used ONLY to get an instance of
	 * {@link ConnectNMove} to generate game moves from strings by calling
	 * {@link #fromString(String)}
	 * 
	 * <p>
	 * Solo se debe usar este constructor para obtener objetos de
	 * {@link ConnectNMove} para generar movimientos a partir de strings usando
	 * el metodo {@link #fromString(String)}
	 * 
	 */

	protected int rowO, colO; //Row and col origin
	
	public AtaxxMove() {
	}

	/**
	 * Constructs a move for placing a piece of the type referenced by {@code p}
	 * at position ({@code row},{@code col}).
	 * 
	 * <p>
	 * Construye un movimiento para colocar una ficha del tipo referenciado por
	 * {@code p} en la posicion ({@code row},{@code col}).
	 * 
	 * @param row
	 *            Number of row.
	 *            <p>
	 *            Numero de fila.
	 * @param col
	 *            Number of column.
	 *            <p>
	 *            Numero de columna.
	 * @param p
	 *            A piece to be place at ({@code row},{@code col}).
	 *            <p>
	 *            Ficha a colocar en ({@code row},{@code col}).
	 */
	public AtaxxMove(int row, int col, int rowO, int colO, Piece p) {
		super(p);
		this.row = row;
		this.col = col;
		this.rowO = rowO;
		this.colO = colO;
	}

	@Override
	public void execute(Board board, List<Piece> pieces) { 
		int i;
		if (board.getPosition(rowO, colO) == null) { //Mover de huevo vacío
			throw new GameError("position (" + rowO + "," + colO + ") is empty!");
		} else {
			if (!board.getPosition(rowO, colO).equals(getPiece())){ //Comprobar que seleccionamos una nuestra
				throw new GameError("position (" + rowO + "," + colO + ") does not have any piece of you!"); //WTF
			}
			if (board.getPosition(row, col) == null) { //Si hay hueco donde queremos mover
				if ((Math.abs(row - rowO) <= 2) && (Math.abs(col - colO) <= 2)) { //Donde mover está a tiro
					board.setPosition(row, col, getPiece());
					i = board.getPieceCount(getPiece());
					board.setPieceCount(getPiece(), i+1);
					changeNeigh(row, col, getPiece(), board); //Transformar
					//Añadir el decremento a la pieza que se pierde
					if ((Math.abs(row - rowO) > 1) || (Math.abs(col - colO) > 1)) { //Borrar la antigua y vamos lejos
						board.setPosition(rowO, colO, null);
						i = board.getPieceCount(getPiece());
						board.setPieceCount(getPiece(), i-1);
					}
				} else {
					throw new GameError("position (" + row + "," + col + ") is too far!");
				}				
			} else { //Ocupada, no se puede mover
				throw new GameError("position (" + row + "," + col + ") is already occupied!");
			}
		}
	}

	/**
	 * This move can be constructed from a string of the form "row SPACE col"
	 * where row and col are integers representing a position.
	 * 
	 * <p>
	 * Se puede construir un movimiento desde un string de la forma
	 * "row SPACE col" donde row y col son enteros que representan una casilla.
	 */
	@Override
	public GameMove fromString(Piece p, String str) {
		String[] words = str.split(" ");
		if (words.length != 4) {
			return null;
		}

		try {
			int row, col, rowO, colO;
			row = Integer.parseInt(words[2]);
			col = Integer.parseInt(words[3]);
			rowO = Integer.parseInt(words[0]);
			colO = Integer.parseInt(words[1]);
			return createMove(row, col, rowO, colO, p);
		} catch (NumberFormatException e) {
			return null;
		}

	}

	/**
	 * Creates a move that is called from {@link #fromString(Piece, String)}.
	 * Separating it from that method allows us to use this class for other
	 * similar games by overriding this method.
	 * 
	 * <p>
	 * Crea un nuevo movimiento con la misma ficha utilizada en el movimiento
	 * actual. Llamado desde {@link #fromString(Piece, String)}; se separa este
	 * metodo del anterior para permitir utilizar esta clase para otros juegos
	 * similares sobrescribiendo este metodo.
	 * 
	 * @param row
	 *            Row of the move being created.
	 *            <p>
	 *            Fila del nuevo movimiento.
	 * 
	 * @param col
	 *            Column of the move being created.
	 *            <p>
	 *            Columna del nuevo movimiento.
	 */
	protected GameMove createMove(int row, int col, int rowO, int colO, Piece p) {
		return new AtaxxMove(row, col, rowO, colO, p);
	}

	@Override
	public String help() {
		return "Row and column for origin and for destination, separated by spaces (four numbers).";
	}

	@Override
	public String toString() {
		if (getPiece() == null) {
			return help();
		} else {
			return "Move a piece '" + getPiece() + "' Origin: (" + row + "," + col + ")" + "' Destination: (" + row + "," + col + ")";
		}
	}
	
	public void changeNeigh(int row, int col, Piece p, Board board) {
		Piece obs = new Piece("*");
		int ind;
		for (int i = row-1; i < row+2; i++){
			for (int j = col-1; j < col+2; j++){
				if (i < board.getRows() && j < board.getCols() && (i>-1) && (j>-1) && board.getPosition(i, j) != board.getPosition(row, col) && board.getPosition(i, j) != null){
					if(!obs.equals(board.getPosition(i, j))){ //Si no es un obstaculo
						ind = board.getPieceCount(board.getPosition(i, j));
						board.setPieceCount(board.getPosition(i, j), ind-1);
						board.setPosition(i, j, p);
						board.setPieceCount(p, board.getPieceCount(p)+1);
						//De la pieza a modificar, se decrementa su contador
					}
					
				}
			}
		}
	}
	
}
