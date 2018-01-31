/****************************/
/* Projet réseau            */
/* BAZANTE Alice            */
/* LEBLANC Maxime           */
/* MAINFROID Pierre-Olivier */
/****************************/

import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread 
{

	/**
	 * Class variables
	 */
	private int port;
	
	/**
	 * Constructs a Server with the port given in parameters
	 * @param p, the port of the Server
	 */
	public Server(int p) 
	{
		port = p;
	}
	
	/**
	 * Runs the Server
	 */
	public void run() 
	{
		/**
			Création des clés
		 */
		
		try {
			/** Creates a ServerSocket to accepts the Clients **/
			ServerSocket server = new ServerSocket(port);
			
			/** Accepts the Clients and creates a ServerToClient to communicate with it **/
			while (true) {
				Socket socket = server.accept();
				
				ServerToClient server_to_client = new ServerToClient(socket);
				server_to_client.start();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		if (args.length >=1) {
			Server server = new Server(Integer.parseInt(args[0]));
			server.start();
		} else {
			System.out.println("Need to pass the port as argument");
		}
	}

}
