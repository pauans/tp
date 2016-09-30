package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.DummyAIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;

/**
 * A factory for creating connect-N games. See {@link ConnectNRules} for the
 * description of the game.
 * 
 * 
 * <p>
 * Factoria para la creacion de juegos Connect-n. Vease {@link ConnectNRules}
 * para la descripcion del juego.
 */
public class AtaxxFactory implements GameFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int rows;
	private int cols;
	private int obs;

	public AtaxxFactory() {
		this(7,7,0);
	}

	public AtaxxFactory(Integer numRows, Integer numCols, Integer obs) { //Puede ser rectangular, cambiar
		if(numRows == null){
			numRows = 0;
		}
		if(numCols == null){
			numCols = 0;
		}
		if(obs == null){
			obs = 0;
		}
		
		if (numCols < 5 || numRows < 5) {
			throw new GameError("Dimension must be at least 5: " + numRows + " x " + numCols);
		} else {
			if (numCols % 2 == 0 || numRows % 2 == 0) {
				throw new GameError("Dimension must be odd: " + numRows + " x " + numCols);
			} else {
				setRows(numRows);
				setCols(numCols);
				this.setObs(obs);
			}
		}
	}

	@Override
	public GameRules gameRules() {
		return new AtaxxRules(getRows(), getCols(), getObs());
	}

	@Override
	public Player createConsolePlayer() {
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AtaxxMove());
		return new ConsolePlayer(new Scanner(System.in), possibleMoves);
	}

	@Override
	public Player createRandomPlayer() {
		return new AtaxxRandomPlayer();
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		return new DummyAIPlayer(createRandomPlayer(), 1000);
	}

	/**
	 * By default, we have two players, X and O.
	 * <p>
	 * Por defecto, hay dos jugadores, X y O.
	 */
	@Override
	public List<Piece> createDefaultPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		return pieces;
	}

	@Override
	public void createConsoleView(Observable<GameObserver> g, Controller c) {
		new GenericConsoleView(g, c);
	}

	@Override
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece,
			Player random, Player ai) {
		throw new UnsupportedOperationException("There is no swing view");
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getObs() {
		return obs;
	}

	public void setObs(int obs) {
		this.obs = obs;
	}

}
