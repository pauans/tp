package es.ucm.fdi.tp.practica5.attt;

import java.util.List;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;

public class AdvancedTTTSwingPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pCount;
	private int origRow, origCol, destRow, destCol;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		ConnectNMove mov = new AdvancedTTTMove(origRow, origCol, destRow, destCol, p);
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


	public int getpCount() {
		return pCount;
	}


	public void setpCount(int pCount) {
		this.pCount = pCount;
	}


}
