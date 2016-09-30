package es.ucm.fdi.tp.practica5.ataxx;

import java.util.List;

import es.ucm.fdi.tp.practica4.ataxx.AtaxxMove;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AtaxxSwingPlayer extends Player {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int origRow, origCol, destRow, destCol;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		AtaxxMove mov = new AtaxxMove(this.destRow, this.destCol, this.origRow, this.origCol, p);
		return mov;
	}
	
	public void setOrigin(int origRow, int origCol){
		this.origRow = origRow;
		this.origCol = origCol;
	}

	public void setDestiny(int destRow, int destCol){
		this.destRow = destRow;
		this.destCol = destCol; 
	}
}
