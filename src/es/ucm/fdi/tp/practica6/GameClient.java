package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.*;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.responses.Response;

public class GameClient extends Controller implements Observable<GameObserver> , GameObserver {

	private String host;
	private int port;
	private List<GameObserver> observers;
	private Piece localPiece;
	private GameFactory gameFactory;
	private Connection connectionToServer;
	private boolean gameOver;
	
	
	public GameClient(String host, int port) {
		super(null, null);
		this.host = host;
		this.port = port;
		
		observers = new ArrayList<GameObserver>();
		try {
			connect();
		} catch (Exception e) {	} 
	}

	public GameFactory getGameFactory(){
		return this.gameFactory;
	}
	
	public Piece getPlayerPiece(){
		return localPiece;
	}
	
	@Override
	public void addObserver(GameObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(GameObserver o) {
		observers.remove(o);
	}
	
	public void makeMove(Player p){
		forwardCommand(new PlayCommand(p));
	}

	public void stop(){
		forwardCommand(new QuitCommand());
	}
	
	public void restart(){
		forwardCommand(new RestartCommand());
	}

	private void forwardCommand(Command cmd) {
		if(!gameOver){ //Si gameOver, no hacer nada
			try {
				connectionToServer.sendObject(cmd);
			} catch (IOException e) {	}
			
		}
		
	}
	
	private void connect() throws Exception {
		connectionToServer = new Connection(new Socket(host, port));
		connectionToServer.sendObject("Connect");
		
		Object response = connectionToServer.getObject();
		if(response instanceof Exception){
			throw (Exception) response;
		}
		
		try {
			//connectionToServer.getObject(); //OK
			gameFactory = (GameFactory) connectionToServer.getObject(); //gameFactory
			localPiece = (Piece) connectionToServer.getObject(); //piece

		} catch (Exception e) {	}
	}
	
	public void start() {
		this.observers.add(this);

		gameOver = false;
		while(!gameOver){
			try {
				Response res = (Response) connectionToServer.getObject(); //leer response
				for(int i=0; i < observers.size(); i++) {
					//Execute the response on the observer o
					res.run(observers.get(i));
				}
			} catch (ClassNotFoundException | IOException e) {	}
		}
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {		
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		gameOver = true;
		try {
			connectionToServer.stop();
		} catch (IOException e) {}		
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {		
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {		
	}

	@Override
	public void onError(String msg) {		
	}
}

