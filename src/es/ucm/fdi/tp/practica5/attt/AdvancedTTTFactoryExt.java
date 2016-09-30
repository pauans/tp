package es.ucm.fdi.tp.practica5.attt;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTFactory;
import es.ucm.fdi.tp.basecode.attt.AdvancedTTTRules;
import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayerFromListOfMoves;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.swing.AdvancedTTTSwingView;

public class AdvancedTTTFactoryExt extends AdvancedTTTFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AdvancedTTTFactoryExt(){
		super();
	}
	
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece, Player random, Player ai) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ new AdvancedTTTSwingView(g, c, viewPiece, random, ai, new AdvancedTTTRules());}
		});
	}
	
	@Override
	public Player createConsolePlayer() {
		// unlink ConnectN, we use the console player that shows a list of
		// possible move, etc.
		return new ConsolePlayerFromListOfMoves(new Scanner(System.in));
	}
}
