package es.ucm.fdi.tp.practica5.ataxx;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.practica4.ataxx.AtaxxFactory;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxRules;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.swing.AtaxxSwingView;

public class AtaxxFactoryExt extends AtaxxFactory {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public AtaxxFactoryExt(Integer dimRows, Integer dimCols, Integer dimObs) {
			super(dimRows,dimCols,dimObs);
	}

	public AtaxxFactoryExt() {
		super();
	}

	@Override
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece, Player random, Player ai) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ new AtaxxSwingView(g, c, viewPiece, random, ai, new AtaxxRules(getRows(), getCols(), getObs()));}
		});
	}
}
