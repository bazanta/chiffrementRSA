/****************************/
/* Projet réseau            */
/* BAZANTE Alice            */
/* LEBLANC Maxime           */
/* MAINFROID Pierre-Olivier */
/****************************/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread 
{
	private static int number_of_clients = 0;

	/**
	 * Class variables
	 */
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;
	private int port;
	private int client_name;

	/**
	 * Constructs a Client
	 * @param number, the number of the Client
	 * @param p, the port to connect to
	 */
	public Client(int p) 
	{
		socket = null;
		out = null;
		in = null;
		port = p;
		client_name = ++number_of_clients;
	}
	
	/**
	 * Connects the client to the server on the given port
	 * @param port, the port of the server you must connect to
	 * @throws Exception
	 */
	public void connect(int port) throws Exception 
	{
		System.out.println("CLIENT " + client_name + "\tCONNECT TO " + port);
		socket = new Socket("localhost", port);
		out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		out.flush();
		in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));		
		sendMessage("NAME CLIENT-" + client_name);
	}

	/**
	 * Sends a message using the ObjectOutpurStream
	 * @param o, the Object to send
	 * @throws IOException
	 */
	private void sendMessage(Object o) throws IOException 
	{
		out.writeObject(o);
		out.flush();
	}

	/**
	 * Runs the thread
	 */
	public void run() 
	{
		try {
			connect(port);
		
			String message;
			/** Gets the Server input **/
			while (!(message = (String) in.readObject()).equals("QUIT")) {
				if (message.equals("WAIT")) {
					/** Wait for the server's instruction **/
				} else if (message.equals("CONNECTED")) {
					System.out.println("CLIENT \tCONNECTED");
					sendMessage("QUIT");
				}
			}
			System.out.println("CLIENT " + client_name + "\tQUIT");
			sendMessage("QUIT");
			
			/** Closes the connection **/
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Client server = new Client(20000);
		server.start();
	}

}
