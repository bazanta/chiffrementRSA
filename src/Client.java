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
import java.util.Scanner;
import java.math.BigInteger;

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
	private Keys porteCle;
	private Key hostKey;

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
		porteCle = new Keys();
		hostKey = null;
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
		sendMessage(Message.MESSAGE_TYPE.MESSAGE,"NAME CLIENT-" + client_name);
		sendMessage(Message.MESSAGE_TYPE.KEY, porteCle.getKeyPublic());
	}

	/**
	 * Sends a message using the ObjectOutpurStream
	 * @param o, the Object to send
	 * @throws IOException
	 */
	private void sendMessage(Message.MESSAGE_TYPE type , Object o) throws IOException 
	{
		Message msg = new Message(type,o.toString());
		if (type == Message.MESSAGE_TYPE.MESSAGE && hostKey != null){
			msg.encryptMess(hostKey);
		}
		out.writeObject(msg);
		out.flush();
	}

	/**
	 * Runs the thread
	 */
	public void run() 
	{
		try {
			connect(port);
			Scanner sc = new Scanner(System.in);
			Message message;
			/** Gets the Server input **/
			while (!(message = (Message) in.readObject()).equals("QUIT")) {
				if (message.decryptMess(hostKey).equals("WAIT")) {
					/** Wait for the server's instruction **/
				} else if (message.decryptMess(hostKey).equals("CONNECTED")) {
					System.out.println("CLIENT \tCONNECTED");
					//sendMessage(Message.MESSAGE_TYPE.MESSAGE,"QUIT");
				} else if (message.getType() == Message.MESSAGE_TYPE.KEY) {
					String string = message.getMessage();
					String[] hStrings = string.split(",");
					hostKey = new Key(new BigInteger(hStrings[0]),new BigInteger(hStrings[1]));
				} else {
					sendMessage(Message.MESSAGE_TYPE.MESSAGE,sc.nextLine());
				}
			}
			System.out.println("CLIENT " + client_name + "\tQUIT");
			sendMessage(Message.MESSAGE_TYPE.MESSAGE,"QUIT");
			
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
