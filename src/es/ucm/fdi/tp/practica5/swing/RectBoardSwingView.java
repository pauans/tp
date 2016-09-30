package es.ucm.fdi.tp.practica5.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//import es.ucm.fdi.tp.basecode.Main.PlayerMode;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;


public abstract class RectBoardSwingView extends SwingView implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel tablero;
	int nCols;
	int nFils;
	Piece localPiece;
	BoardComponent boComp;
	
	public class BoardComponent extends JComponent implements GameObserver {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private int _CELL_HEIGHT = 0;
		private int _CELL_WIDTH = 0;
		private int rows;
		private int cols;
		Board b;
		
		public BoardComponent(int rows, int cols,Map<Piece, Color> pieceColors, Board b, final Observable<GameObserver> game) {
			initBoard(rows, cols, pieceColors, b);
			initGUI();
			game.addObserver(BoardComponent.this);
		}

		private void initBoard(int rows, int cols, Map<Piece, Color> pieceColors, Board b) {
			this.rows = rows;
			this.cols = cols;
			this.b = b;
		}

		private void initGUI() {

			addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					int cX = e.getX();
					int cY = e.getY();
					int r = cY/_CELL_HEIGHT;
					int c = cX/_CELL_WIDTH;
					handleMouseClick(r,c,e.getButton());
				}
			});
			this.setSize(new Dimension(rows * _CELL_HEIGHT, cols * _CELL_WIDTH));
			repaint();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			_CELL_WIDTH = this.getWidth() / cols;
			_CELL_HEIGHT = this.getHeight() / rows;

			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++)
					drawCell(i, j, g);
		}

		private void drawCell(int row, int col, Graphics g) {
			int x = col * _CELL_WIDTH;
			int y = row * _CELL_HEIGHT;
			
			if(board == null){
				return;
			}
			
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x + 2, y + 2, _CELL_WIDTH - 4, _CELL_HEIGHT - 4);
			if(b.getPosition(row, col) != null){
				if(pieceColors.get(b.getPosition(row, col)) == null ){
					this.setBackground(Color.BLACK);
					g.setColor(Color.BLACK);
					
				}else{
					this.setBackground(pieceColors.get(b.getPosition(row, col)));
					g.setColor(pieceColors.get(b.getPosition(row, col)));
				}
				g.fillOval(x + 4, y + 4, _CELL_WIDTH - 8, _CELL_HEIGHT - 8);
	
				g.setColor(Color.black);
				g.drawOval(x + 4, y + 4, _CELL_WIDTH - 8, _CELL_HEIGHT - 8);
			}
			
		}

		public void setBoardSize(int rows, int cols) {
			initBoard(rows, cols, pieceColors, b);
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					repaint();
				}
			});
		}

		
		@Override
		public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
			this.b = board;
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					repaint();
				}
			});
		}


		@Override
		public void onGameOver(Board board, State state, Piece winner) {
			this.b = board;
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					repaint();
				}
			});
		}

		@Override
		public void onMoveStart(Board board, Piece turn) {
			this.b = board;
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					repaint();
				}
			});		
		}

		@Override
		public void onMoveEnd(Board board, Piece turn, boolean success) {
			this.b = board;
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					repaint();
				}
			});		
		}

		@Override
		public void onChangeTurn(Board board, Piece turn) {
			this.b = board;
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					repaint();
				}
			});
		}

		@Override
		public void onError(String msg) {			
		}


	} // Cierre de la clase BoardComponent
	
	public RectBoardSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer,GameRules rules){
		super(g, c, localPiece, randPlayer, aiPlayer,rules);
		this.tablero = new JPanel();
		this.localPiece = localPiece;
	}
	
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn ) {
		super.onGameStart(board, gameDesc, pieces, turn);
		nFils = board.getRows();
		nCols = board.getCols();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white),BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		panel.setPreferredSize(getMaximumSize());
		BoardComponent bc = new BoardComponent(nFils,nCols, pieceColors,board, obser);
		panel.add(bc, 0);
		panel.setSize(getMaximumSize());
	}

	public void mouseClicked(MouseEvent e) {
	}	

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		super.onGameOver(board, state, winner);
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}

	protected void redrawBoard(Board board, JPanel panel) {
		for (int i = 0; i < nFils; i++)
			for (int j = 0; j < nCols; j++){
				//panel.getComponent(1).paint(getGraphics());
			}
				
	}

	protected abstract void handleMouseClick(int row, int col, int mouseButton);


}
