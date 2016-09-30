package es.ucm.fdi.tp.practica5.swing;

import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.practica5.ttt.TicTacToeSwingPlayer;

public class TicTacToeSwingView extends RectBoardSwingView  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TicTacToeSwingPlayer player;
	Piece turn;
	Piece localPiece;
	
	public TicTacToeSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer, GameRules rules) {
		super(g, c, localPiece, randPlayer, aiPlayer, rules);
		player = new TicTacToeSwingPlayer();	
		this.localPiece = localPiece;
		this.turn = super.playerPos.get(0);
	}


	@Override
	public void onChangeTurn(Board board, Piece turn) {
		super.onChangeTurn(board, turn);
		addMsg("Turn for: " + turn);
		this.turn = turn;
		if(localPiece == turn){
			addMsg(" (You!)");
		}

		addMsg("Click on an empty box");
	}
	
	public void onMoveStart(Board board, Piece turn) {
		super.onMoveStart(board, turn);		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		super.onMoveEnd(board, turn, success);
	}


	@Override
	public void onError(String msg) {
		addMsg(msg);		
	}

	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) {
		if(tablero.isEnabled()){
			if(mouseButton == 1){
				if (board.getPosition(row, col) != null){ 
					addMsg("Position already occupied");
				} else {
					player.setMove(row, col);	
				}
			}
			try{
				decideMakeManualMove(player);
			} catch (GameError e){
				addMsg(e.getMessage());
			}
		}
	}		
	
	@Override
	protected void initBoardGui() {		
	}

	public void onGameOver(Board board, State state, Piece winner) {
		super.onGameOver(board, state, winner);
		addMsg("Game over!");
		addMsg("State: " + state.name());
		if(state.name() != "Stopped"){
			addMsg("The winner is " + winner.getId());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void activateBoard() {
		tablero.enable();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void deActivateBoard() {
		tablero.disable();
	}

	@Override
	protected void redrawBoard(Board board, JPanel panel) {
		super.redrawBoard(board, panel);
	}


}
