package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

	volatile private Socket s;
	volatile private ObjectOutputStream out;
	volatile private ObjectInputStream in;
	
	/**
	 * Almacena los streams de entrada y salida en atributos
	 * @param s Socket
	 * @throws IOException
	 */
	public Connection(Socket s) throws IOException  {
		this.s = s;
		this.out = new ObjectOutputStream(s.getOutputStream());
		this.in = new ObjectInputStream(s.getInputStream());
	}
	
	/**
	 * Envía un objeto
	 * @param r, objeto a envíar
	 * @throws IOException
	 */
	public void sendObject(Object r) throws IOException{
		out.writeObject(r);
		out.flush();
		out.reset();
	}
	
	/**
	 * Recibe un objeto
	 * @return Devuelve el objeto leído
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object getObject() throws ClassNotFoundException, IOException{
		return in.readObject();
	}
	
	/**
	 * Cierra el socket
	 * @throws IOException
	 */
	public void stop() throws IOException{
		s.close();
	}
}
