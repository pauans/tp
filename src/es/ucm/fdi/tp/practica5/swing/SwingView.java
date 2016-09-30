package es.ucm.fdi.tp.practica5.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public abstract class SwingView extends JFrame implements GameObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	protected Piece localPiece;
	protected Piece turn;
	protected List<Piece> pieces;
	protected Board board;

	protected Map<Piece, Color> pieceColors;
	private Map<Piece, PlayerMode> playerTypes;
	protected Map<Integer, Piece> playerPos;
	
	protected Observable<GameObserver> obser;
	
	private ArrayList<Color> rowColors;
	/*
	 * Este array relaciona las filas de la tabla con el color que deben llevar
	 * Se parece a pieceColors, pero no es igual
	 * No se pueden basar los colores de la tabla en pieceColors, porque 
	 * si un jugador se queda sin piezas, no hay forma de relacionar 
	 *  Piece con Color, pero la tabla debe de seguir manteniendo su color.
	 * Este array se mantiene sincronizado, al igual que pieceColors, y los
	 * colores se actualiza al momento 
	 */
	
	private Player randPlayer;
	protected GameRules rul; 
	
	protected JPanel panel;
	JTextArea messages;
	int index = 0;

	DefaultTableModel tabla;
	JTable playerInfos;
	JComboBox<String> piecesCombo1, piecesCombo2;
	JComboBox<String> changeMode;
	JButton botonColor, botonSet, botonRand, botonInte, botonSalir, botonRestart;
	
	public enum PlayerMode {
		MANUAL("m", "Manual"), RANDOM("r", "Random"), AI("a", "Automatics");

		private String id;
		private String desc;

		PlayerMode(String id, String desc) {
			this.id = id;
			this.desc = desc;
		}
		
		static PlayerMode typeOf(String id){
			
			switch (id){
				case  "Manual":
					return MANUAL;
				case "Random":
					return RANDOM;
				default:
					return AI;
				
			}	
					
		}

		public String getId() {
			return id;
		}

		public String getDesc() {
			return desc;
		}

		@Override
		public String toString() {
			return id;
		}
	}
	
	public SwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer, GameRules rules) {
		this.ctrl = c;
		this.localPiece = localPiece;
		this.rul = rules;
		this.randPlayer = randPlayer;
		initGUI();
		this.obser = g;
		this.pieces = new ArrayList<Piece>();
		this.playerTypes = new HashMap<Piece, PlayerMode>();
		g.addObserver(this);
	}
	
	private void initGUI() { 
		
		initBoardGui(); 		
		this.setSize(850, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.getContentPane().setLayout(new BorderLayout());
		//Separacion de la ventana en dos containers y su colocacion
		panel = new JPanel();
		JPanel Ops = new JPanel();
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.getContentPane().add(Ops, BorderLayout.LINE_END);
		//ColocaciÃƒÂ³n de los containers de Ops con el BoxLayout, ordenados uno debajo de otro
		Ops.setLayout(new BoxLayout(Ops, BoxLayout.PAGE_AXIS));
		
		/* PANEL DE MENSAJES */
		messages = new JTextArea();
		JScrollPane statusMessages = new JScrollPane(messages);
		statusMessages.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Status Messages"));
		statusMessages.setPreferredSize(new Dimension(100, 50));;
		//Este borde deberia estar fijo
		messages.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder(10, 15, 5, 15)));
		Font fuente=new Font("Monospaced", Font.BOLD, 10);
		messages.setFont(fuente);
        messages.setEditable(false); 
        messages.setMaximumSize(new Dimension(200, 50));
		
		/* INFORMACION DEL JUGADOR */
        Object[] colNames = { "Player", "Mode", "#Pieces" };
        Object[][] data = {};
        tabla = new DefaultTableModel(data, colNames);
        playerInfos = new JTable(this.tabla){
        	private static final long serialVersionUID = 1L;

        	@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				comp.setBackground(rowColors.get(row));
				return comp;
			}
        	
        };
        JScrollPane playerInfo = new JScrollPane(playerInfos);

        playerInfo.setPreferredSize(new Dimension(100,48));
		playerInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player Information"));
        playerInfos.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white),BorderFactory.createEmptyBorder(10, 15, 5, 15)));
        playerInfos.setEnabled(false);
        
        
		/*SELECCION DE COLORES PARA LAS PIEZAS*/
		JPanel piecesColors = new JPanel();
		piecesColors.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Piece Colors"));
		piecesColors.setSize(100,50);
		piecesCombo1 = new JComboBox<String>();
		botonColor = new JButton("Choose Color");
		botonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(piecesColors, "Elige un color", null);
				String seleccionado=(String)piecesCombo1.getSelectedItem();
				int i = 0;
				boolean encontrado = false;
				while(i<pieces.size() && !encontrado){
					encontrado = (pieces.get(i).getId() == seleccionado);
					i++;
				}
				if(encontrado){
					pieceColors.put(pieces.get(piecesCombo1.getSelectedIndex()), newColor);
					rowColors.set(piecesCombo1.getSelectedIndex(), newColor);
					setPieceColor(pieces.get(i-1), newColor);
					addMsg("Se cambió el color de la pieza: " + pieces.get(i-1).getId());
					redrawBoard(board, panel); 
				}
			}});

		piecesColors.add(piecesCombo1);
		piecesColors.add(botonColor);

		/* SELECCION DE MODO DE JUGADOR */
		JPanel playerModes = new JPanel();
		playerModes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player Modes"));
		playerModes.setSize(100,50);
		piecesCombo2 = new JComboBox<String>();
		changeMode = new JComboBox<String>();
		
		changeMode.addItem("Manual");
		changeMode.addItem("Automatic");
		changeMode.addItem("Intelligent");
		
		this.botonSet = new JButton("Set");
		botonSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int jug = piecesCombo2.getSelectedIndex();
				String mode = (String) changeMode.getSelectedItem();
				if (localPiece == null){
					playerTypes.put(pieces.get(jug), PlayerMode.typeOf(mode) );
					addMsg("Se cambia el modo de jugador de: " + pieces.get(jug).getId() + " a modo: " + mode );
					for(int i=0; i < pieces.size(); i++){
						tabla.setValueAt(playerTypes.get(pieces.get(i)).desc, i, 1);
					}
					if((pieces.get(jug).equals(turn)) && (!mode.equals(PlayerMode.MANUAL.toString()))){
						decideMakeAutomaticMove();
					}
				} else {
					playerTypes.put(localPiece, PlayerMode.typeOf(mode) );
					addMsg("Se cambia el modo de jugador de: " + localPiece.getId() + " a modo: " + mode );
					for(int i=0; i < pieces.size(); i++){
						tabla.setValueAt(playerTypes.get(pieces.get(i)).desc, i, 1);
					}
					if((localPiece.equals(turn)) && (!mode.equals(PlayerMode.MANUAL.toString()))){
						decideMakeAutomaticMove();
					}
				}
				
			}});
		
		playerModes.add(piecesCombo2);
		playerModes.add(changeMode);
		playerModes.add(botonSet);
		
		/* MODOS AUTOMATICOS */
		JPanel automaticMoves = new JPanel();
		automaticMoves.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Automatic Moves"));
		automaticMoves.setSize(100,50);
		this.botonRand = new JButton("Random");
		botonRand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				decideMakeAutomaticMove();
				}
			});
		this.botonInte = new JButton("Intelligent");
		botonInte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
				    Thread.sleep(1500);   //Espera segundo y medio
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}	
				decideMakeAutomaticMove();
				}
			});
		automaticMoves.add(botonRand);
		automaticMoves.add(botonInte);
		
		/* BOTONES */
		JPanel botones = new JPanel();
		botonSalir = new JButton("Quit");
		botonSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane deseaSalir = new JOptionPane();
				int i = JOptionPane.showConfirmDialog(deseaSalir, "¿Desea salir?", "Board Games", JOptionPane.YES_NO_OPTION);
				if(i == 0){
					ctrl.stop();
					System.exit(0);
				}
			}});
		botonRestart = new JButton("Restart");
		botonRestart.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			ctrl.stop();
			ctrl.restart();
		}});		
		//Los dos botones deben estar en el mismo container para poder cambiarles el orden
		botones.add(botonSalir);
		botones.add(botonRestart);

		//Añadir todos los containers al principal
		Ops.add(statusMessages);
		Ops.add(playerInfo);				
		Ops.add(piecesColors);
		Ops.add(playerModes);
		Ops.add(automaticMoves);
		Ops.add(botones);
		
		this.setVisible(true);	
	}
	
	final protected Piece getTurn() { return turn; }
	
	final protected Board getBoard() { return board; }
	
	final protected List<Piece> getPieces() { return pieces;}
	
	final protected Color getPieceColor(Piece p) { return pieceColors.get(p); }
	
	final protected Color setPieceColor(Piece p, Color c) { return pieceColors.put(p,c); }
	
	final protected void setBoardArea(JComponent c) {	} 
	
	final protected void addMsg(String msg) {
		messages.setText(messages.getText() + "\n" + msg);
	}
		
	final protected void decideMakeManualMove(Player manualPlayer) { 
		ctrl.makeMove(manualPlayer);
		this.repaint();
	}
	
	private void decideMakeAutomaticMove() { 
		ctrl.makeMove(randPlayer);
		this.repaint();
	}
	
	protected abstract void initBoardGui();
	protected abstract void activateBoard();
	protected abstract void deActivateBoard();
	protected abstract void redrawBoard(Board board, JPanel panel);
	
	 // GameObserver Methods
	protected boolean isPlayerPiece(Piece p) {
		return false;
	}

	public void onGameStart(final Board board, final String gameDesc, final List<Piece> pieces, final Piece turn) {			
			SwingUtilities.invokeLater(new Runnable(){
				 public void run(){ handleGameStart(board, gameDesc, pieces, turn); }
			 });
	}
	
	@SuppressWarnings("deprecation")
	private void handleGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.board = board;		
		this.turn = turn;
		this.pieces = pieces; 
		this.messages.setText("");

		Color[] iniciales = { Color.gray, Color.cyan, Color.pink, Color.magenta};
		int ind = 0;
		pieceColors = new HashMap<Piece, Color>();
		rowColors = new ArrayList<Color>();
		playerTypes = new HashMap<Piece, PlayerMode>();
		playerPos = new HashMap<Integer, Piece>();
		for (Piece i : pieces){
			this.pieceColors.put(i, iniciales[ind]);
			this.rowColors.add(iniciales[ind]);
			playerTypes.put(i, PlayerMode.MANUAL);
			this.playerPos.put(ind, i);
			ind++;
		}
		
		botonSet.setEnabled(true);
		botonColor.setEnabled(true);
		botonRand.setEnabled(true);
		botonInte.setEnabled(true);

		for(int i=0; i< pieces.size();i++){
			piecesCombo1.addItem(pieces.get(i).getId());
			piecesCombo2.addItem(pieces.get(i).getId());
			//Nueva fila con el id de la pieza, el modo de juego del jugador y las piezas de cada tipo
			if(board.getPieceCount(pieces.get(i)) != null){
				Object[] newRow = {pieces.get(i).getId(), playerTypes.get(pieces.get(i)).desc, board.getPieceCount(pieces.get(i))};
				tabla.addRow(newRow);
			} else {
				Object[] newRow = {pieces.get(i).getId(), playerTypes.get(pieces.get(i)).desc, "0"};
				tabla.addRow(newRow);
			}
		}
		
		if(tabla.getRowCount() > pieces.size()){
			for(int j=tabla.getRowCount(); j > pieces.size(); j--){
				tabla.removeRow(j-1);
			}	
		}
		
		if (localPiece == null) {
			this.setTitle("Board Games: " + gameDesc);			
		} else {
			if(!localPiece.equals(turn) && !localPiece.equals(null)){
				deActivateBoard();
				botonRand.setEnabled(false);
				botonInte.setEnabled(false);
			} else {
				activateBoard();
				botonRand.setEnabled(true);
				botonInte.setEnabled(true);
			}
			this.setTitle("Board Games: " + gameDesc + " (" + localPiece.getId() + ")");			
			piecesCombo2.removeAllItems();
			piecesCombo2.addItem(localPiece.getId());
			botonRestart.hide();
		}
		
		addMsg("Welcome to Board Games: " + gameDesc);
		addMsg("Turn for: " + turn);
		
	}

	protected void handleMouseClick(int row, int col, int mouseButton) {
	}
	
	
	public void onGameOver(final Board board, final State state, final Piece winner) {
			SwingUtilities.invokeLater(new Runnable(){
				 public void run(){ handleOnGameOver(); }
			 });

	}

	private void handleOnGameOver() {
		piecesCombo1.removeAllItems();
		piecesCombo2.removeAllItems();

		botonSet.setEnabled(false);
		botonColor.setEnabled(false);
		botonRand.setEnabled(false);
		botonInte.setEnabled(false);
	}
			
	public void onMoveStart(Board board, Piece turn) {		
		SwingUtilities.invokeLater(new Runnable(){
				 public void run(){ handleOnMoveStart(board, turn); }
			 });
	}
	
	protected void handleOnMoveStart(Board board, Piece turn) {
	}
		
		
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		this.board = board;
			SwingUtilities.invokeLater(new Runnable(){
				 public void run(){ handleOnMoveEnd(); }
			 });
	}
	
	protected void handleOnMoveEnd() {
		for(int i=0; i < pieces.size(); i++){
			tabla.setValueAt(board.getPieceCount(playerPos.get(i)), i, 2);
			tabla.setValueAt(playerTypes.get(pieces.get(i)).desc, i, 1);
			panel.repaint();
			//Si algún jugador se queda sin piezas
			if(board.getPieceCount(playerPos.get(i)) != null && board.getPieceCount(playerPos.get(i)) == 0){
				addMsg("Player " + playerPos.get(i).getId() + " has no pieces!");
			}
		}		
	}
		
		
	public void onChangeTurn(Board board, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable(){
				 public void run(){ handleOnChangeTurn(board,turn); }
			 });
	}
	
	
	protected void handleOnChangeTurn(Board board, Piece turn) {
		this.turn = turn;
		this.board = board;
		
		try{
			if(!(localPiece.equals(turn)) && !(localPiece.equals(null))){
				deActivateBoard();
				botonRand.setEnabled(false);
				botonInte.setEnabled(false);
			} else {
				activateBoard();
				botonRand.setEnabled(true);
				botonInte.setEnabled(true);
			}
		} catch(Exception e){}
		if(playerTypes.get(turn).equals(PlayerMode.AI)){
			//mov = aiPlayer.requestMove(turn, board, pieces, rul);
			decideMakeAutomaticMove();
		} else if (playerTypes.get(turn).equals(PlayerMode.MANUAL)){
			decideMakeManualMove(null);
		} else if (playerTypes.get(turn).equals(PlayerMode.RANDOM)){
			//mov = randPlayer.requestMove(turn, board, pieces, rul);
			decideMakeAutomaticMove();
		}
	}
		
	public void onError(String msg) {
		SwingUtilities.invokeLater(new Runnable(){
				 public void run(){ handleOnError(); }
			 });

	}
	
	protected void handleOnError() {
	}
		
}