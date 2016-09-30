package es.ucm.fdi.tp.practica5.ttt;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;

public class TicTacToeSwingPlayer extends Player {

	private int row, col;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		ConnectNMove mov = new ConnectNMove(row, col, p);
		return mov;
	}

	
	public void setMove(int origRow, int origCol){
		this.row = origRow;
		this.col = origCol;
	}

}
