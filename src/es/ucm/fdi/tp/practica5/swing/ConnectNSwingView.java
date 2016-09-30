package es.ucm.fdi.tp.practica5.swing;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.practica5.connectn.ConnectNSwingPlayer;

public class ConnectNSwingView extends RectBoardSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConnectNSwingPlayer player;
	
	public ConnectNSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer, GameRules rules) {
		super(g, c, localPiece, randPlayer, aiPlayer, rules);
		player = new ConnectNSwingPlayer();	
	}
	
	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) {
	 // do nothing if the board is not active
		if (tablero.isEnabled()){
			player.setMove(row, col);
			try{
				decideMakeManualMove(player);
			} catch (GameError e){
				//addMsg(e.getMessage());
				addMsg("Already ocuppied");
			}
		}
	}
	
	
	
	@Override
	protected void activateBoard() {
	 // - declare the board active, so handleMouseClick accepts moves
		tablero.setEnabled(true);
	 // - add corresponding message to the status messages indicating
	 // what to do for making a move, etc.
	 }
	
	@Override
	protected void deActivateBoard() {
	 // declare the board inactive, so handleMouseClick rejects moves
		tablero.setEnabled(false);
	 }
	
	@Override
	public void onMoveStart(Board board, Piece turn) {
		super.onMoveStart(board, turn);
	}
	
	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		super.onMoveEnd(board, turn, success);
	}
	
	@Override
	public void onChangeTurn(Board board, Piece turn) {
		super.onChangeTurn(board, turn);
		addMsg("Turn for: " + turn);
		if(localPiece == turn){
			addMsg("(You!)");
		}	}
	
	@Override
	public void onError(String msg) {		
	}

	@Override
	protected void initBoardGui() {		
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		super.onGameOver(board, state, winner);
		addMsg("Game over!");
		addMsg("State: " + state.name());
		if(state.name() != "Stopped"){
			addMsg("The winner is " + winner.getId());
		}
	}
	
}
