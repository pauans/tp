package es.ucm.fdi.tp.practica5.swing;

import javax.swing.JPanel;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxSwingPlayer;

public class AtaxxSwingView extends RectBoardSwingView  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AtaxxSwingPlayer player;
	private int estado;
	Controller c;
	
	public AtaxxSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer,GameRules rules) {
		super(g, c, localPiece, randPlayer, aiPlayer, rules);
		player = new AtaxxSwingPlayer();	
		this.localPiece = localPiece;
		estado = 0;
		this.c = c;
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		super.onMoveStart(board, turn);
		estado = 0;
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		super.onMoveEnd(board, turn, success);
		estado=0;
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		super.onChangeTurn(board, turn);
		addMsg("Turn for: " + turn);
		if(localPiece == turn){
			addMsg("(You!)");
		}
		addMsg("Click on an origin piece");
	}

	@Override
	public void onError(String msg) {
		addMsg(msg);
	}

	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) {
		if(panel.isEnabled()){
			switch(estado){
			case 0:
				if(mouseButton == 1){
					if (board.getPosition(row, col) == null){ 
						addMsg("Empty position");
					} else {
						if(board.getPosition(row, col) != this.turn){
							addMsg("Not your piece");
						} else {
							estado = 1;
							player.setOrigin(row, col);
							addMsg("Click on the destination position");
						}
					}
				}
				break;
			case 1:
				if(mouseButton == 1){
					estado = 0;
					player.setDestiny(row, col);
					try{
						decideMakeManualMove(player);
					} catch (GameError e){
						addMsg(e.getMessage());
					}
				} else if(mouseButton == 3){
					estado = 0;
					addMsg("Move cancelled");
					addMsg("Click on an origin piece");
				}
				break;
			}
		}		
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
	
	@Override
	protected void activateBoard() {
		panel.setEnabled(true);;
	}

	@Override
	protected void deActivateBoard() {
		panel.setEnabled(false);
	}


	@Override
	protected void redrawBoard(Board board, JPanel panel) {
		super.redrawBoard(board, panel);
	}



}

