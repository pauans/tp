package es.ucm.fdi.tp.practica5.connectn;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNFactory;
import es.ucm.fdi.tp.basecode.connectn.ConnectNRules;
import es.ucm.fdi.tp.practica5.swing.ConnectNSwingView;

public class ConnectNFactoryExt extends ConnectNFactory{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int dim;
	
	public ConnectNFactoryExt(Integer dimRows) {
		super();
		dim = dimRows;
	}

	public ConnectNFactoryExt() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece, Player random, Player ai) {
		//throw new UnsupportedOperationException("There is no swing view");
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ new ConnectNSwingView(g, c, viewPiece, random, ai, new ConnectNRules(dim));}
		});
		
	}
}
