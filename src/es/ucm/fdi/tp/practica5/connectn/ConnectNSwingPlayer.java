package es.ucm.fdi.tp.practica5.connectn;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;

public class ConnectNSwingPlayer extends Player {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int x, y;
	
	public ConnectNSwingPlayer(){
		this.x = -1;
		this.y = -1;
	}
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		
		while(x == -1 && y == -1){}
		
		ConnectNMove dev = new ConnectNMove(x,y,p);
		x = -1;
		y = -1;
		return dev;
	}

	public void setMove(int row, int col) {
		x = row;
		y = col;
	}

}
