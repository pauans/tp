package es.ucm.fdi.tp.practica6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.responses.ChangeTurnResponse;
import es.ucm.fdi.tp.practica6.responses.ErrorResponse;
import es.ucm.fdi.tp.practica6.responses.GameOverResponse;
import es.ucm.fdi.tp.practica6.responses.GameStartResponse;
import es.ucm.fdi.tp.practica6.responses.MoveEndResponse;
import es.ucm.fdi.tp.practica6.responses.MoveStartResponse;
import es.ucm.fdi.tp.practica6.responses.Response;
import es.ucm.fdi.tp.practica6.Connection;


public class GameServer extends Controller implements GameObserver{
	
	private int port; //Puerto usado por el servidor
	private int numPlayers; //Número de jugadores necesario para iniciar el juego
	private int numOfConnectedPlayers; //Número de jugadores conectados
	private GameFactory gameFactory; //GameFactory para las GameRules
	private List<Connection> clients; //Lista de clientes conectados
	
	volatile private ServerSocket server; //Referencia al servidor
	volatile private boolean stopped; //Indica si servidor apagado
	volatile private boolean gameOver; //Indica si juego terminado
	
	JTextArea infoArea;
	JButton quitButton;
	
	public GameServer(GameFactory gameFactory, List<Piece> pieces, int port){
		super(new Game(gameFactory.gameRules()), pieces);
		this.gameFactory = gameFactory;
		this.port = port;
		numPlayers = pieces.size();
		stopped = false;
		gameOver = false;
		clients = new ArrayList<Connection>();		
		game.addObserver(this);
	}
	
	public synchronized void makeMove(Player player){
		try { super.makeMove(player);} catch (GameError e) {}
	}
	
	public synchronized void stop(){
		try { 
			super.stop();
		} catch (GameError e) {} 
	}
	
	public synchronized void restart(){
		try { super.restart();} catch (GameError e) {}
	}
	
	public void start() {
		controlGUI();
		startServer();		
	}

	private void controlGUI() {
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				public void run() { constructGUI(); }

			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new GameError("Something went wrong when constructing the GUI");
		}
	}
	
	private void constructGUI() {
		JFrame window = new JFrame("Game Server");
		infoArea = new JTextArea();
		infoArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Info Area"));
		infoArea.setPreferredSize(new Dimension(100, 50));;
		infoArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder(10, 15, 5, 15)));
		Font fuente=new Font("Monospaced", Font.BOLD, 10);
		infoArea.setFont(fuente);
		infoArea.setEditable(false); 
		infoArea.setMaximumSize(new Dimension(200, 50));
		log("Welcome,\nyou're going to play:\n"+ game.gameDesc());
		
		quitButton = new JButton("Stop Server");
		quitButton.setPreferredSize(new Dimension(100, 50));
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane deseaSalir = new JOptionPane();
				int i = JOptionPane.showConfirmDialog(deseaSalir, "¿Desea salir?", "Board Games", JOptionPane.YES_NO_OPTION);
				if(i == 0){
					gameOver = true;
					stopped = true;
					stop();
						try {
							for(int j=0; j < clients.size(); j++){
								clients.get(j).stop();
								System.exit(0);
							}
							server.close();
							System.exit(0);
						} catch (IOException e1) {}
				}
			}});
		
		window.setLayout(new BorderLayout());
		window.add(infoArea, BorderLayout.CENTER);
		window.add(quitButton, BorderLayout.SOUTH);
		
		window.setPreferredSize(new Dimension(200, 200));
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
	
	private void log(String msg){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				infoArea.setText(infoArea.getText() + "\n" + msg);
			}});
		
	}

	private void startServer() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			log("Error trying to start the server: " + e.getMessage());
		}
		
		stopped = false;
		
		while(!stopped){
			try {
				Socket s = server.accept();
				log("Connection aceppted");
				handleRequest(s);
			} catch (IOException e1){
				if(!stopped){
					log("error while waiting for a connection: " + e1.getMessage());
				}	
			}
		}
	}
	
	private void handleRequest(Socket s){
		try {
			Connection c = new Connection(s);
			
			Object clientRequest = c.getObject();
			if(!(clientRequest instanceof String) && !((String)clientRequest).equalsIgnoreCase("Connect")){
				c.sendObject(new GameError("Invalid Request"));
				c.stop();
				return;
			}

			//1.si numero de clientes conectados llega a maximo, gameerror
			if(numOfConnectedPlayers == numPlayers){
				c.sendObject(new GameError("Game has already enough players"));
				c.stop();
				return;
			}
		
			//2.Incrementar num de clientes conectados y añadir c a la lista de clientes
			numOfConnectedPlayers++;
	
			//3.Enviar string ok al cliente, seguido del gamefactory y piece a usar. asignar cliente y ficha
			c.sendObject("OK");
			c.sendObject(gameFactory);
			c.sendObject(pieces.get(numOfConnectedPlayers-1));
			clients.add(c);
			
			startClientListener(c);

			//4.Si número suficiente de clientes iniciar el juego, primero start y luego restart
			if(numOfConnectedPlayers == numPlayers){
				try {
					Thread.sleep(2000);	// Necesario para dar tiempo al ultimo cliente a crearse completamente 
				} catch (InterruptedException e) {	}
				
				log("Starting the game...");
				
				//Iniciar el juego
				if(game.getState().equals(Game.State.Starting)){
					game.start(pieces);
				} else {
					game.restart();
				}
			}
		} catch (IOException | ClassNotFoundException _e) {}
	}
	
	private void startClientListener(Connection c) {
		gameOver = false;
		Thread t = new Thread(){
			public void run() {
				while (!stopped && !gameOver) {
					try {
						Command cmd;
						cmd = (Command) c.getObject();
						cmd.execute(GameServer.this);
					} catch (ClassNotFoundException | IOException e) {
						if (!stopped && !gameOver) {
							// stop the game (not the server)
							gameOver = true;
							game.stop();
							for(Connection cl: clients){
								try {
									cl.stop();
								} catch (IOException e1) {	}
							}
					 }
				 }
			}
		}};
		t.start();

		
	}	
	
	void forwardNotification(Response r){
		for (Connection c: clients){
			try {
				c.sendObject(r);
			} catch (IOException e) {}
		}
	}
	

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		forwardNotification(new GameStartResponse(board, gameDesc, pieces, turn));
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		forwardNotification(new GameOverResponse(board, state, winner));
		gameOver = true;
		game.stop();
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		forwardNotification(new MoveStartResponse(board, turn));
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		forwardNotification(new MoveEndResponse(board, turn, success));
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		forwardNotification(new ChangeTurnResponse(board, turn));
	}

	@Override
	public void onError(String msg) {
		forwardNotification(new ErrorResponse(msg));
	}
	
	
	
}
