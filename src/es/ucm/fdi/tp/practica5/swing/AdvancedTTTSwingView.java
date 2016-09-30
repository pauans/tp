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
import es.ucm.fdi.tp.practica5.attt.AdvancedTTTSwingPlayer;

public class AdvancedTTTSwingView extends RectBoardSwingView {

	private static final long serialVersionUID = 1L;
	private AdvancedTTTSwingPlayer player;
	Piece turn;
	Piece localPiece;
	private int estado;
	
	public AdvancedTTTSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer, GameRules rules) {
		super(g, c, localPiece, randPlayer, aiPlayer, rules);
		player = new AdvancedTTTSwingPlayer();
		player.setpCount(6);
		this.localPiece = localPiece;
		this.turn = super.playerPos.get(0);
	}


	@Override
	public void onChangeTurn(Board board, Piece turn) {
		super.onChangeTurn(board, turn);
		addMsg("Turn for: " + turn);
		this.turn = turn;
		if(localPiece == turn){
			addMsg("(You!)");
		}
	}
	@Override
	public void onMoveStart(Board board, Piece turn) {
		super.onMoveStart(board, turn);
		if (player.getpCount() > 0){
			estado = 0;
			addMsg("Click on an empty box to put a piece");
		}else{
			estado = 1;
			addMsg("Click on one of your pieces to move it");
		}
		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		super.onMoveEnd(board, turn, success);
		for(int i=0; i < super.pieceColors.size(); i++){
			tabla.setValueAt(board.getPieceCount(playerPos.get(i)), i, 2);
			//Si algún jugador se queda sin piezas
		}
		player.setpCount(player.getpCount()-1);
		if (player.getpCount() > 0){
			estado = 0;
		}else{
			estado = 1;
		}
	}


	@Override
	public void onError(String msg) {
		addMsg(msg);		
	}

	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) {
	
		if(tablero.isEnabled()){
			switch(estado){
				case 1:
				
					if(mouseButton == 1){ 
						if(board.getPosition(row, col) == turn){
							estado = 1;
							player.setOrigin(row,col);
							estado = 0;
						}
						else{
							addMsg("This piece is not yours!");
						}
					}
							
					break;
				case 0:
					if(mouseButton == 1){
						if(board.getPosition(row, col) == null){
							player.setDestiny(row,col);	
						} else {
						addMsg("Box is not empty!");					
						}
					}
				try{
					decideMakeManualMove(player);
				} catch (GameError e){
					addMsg(e.getMessage());
				}
				break;
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
