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
	private String adresse;
	private int client_name;

	private Keys porteCle;
	private Key hostKey;

	/**
	 * Constructs a Client
	 * @param number, the number of the Client
	 * @param p, the port to connect to
	 */
	public Client(int p, String adr) 
	{
		this.socket = null;
		this.out = null;
		this.in = null;
		this.port = p;
		this.adresse = adr;
		this.client_name = ++number_of_clients;

		this.porteCle = new Keys();
		this.hostKey = null;
	}
	
	/**
	 * Connects the client to the server on the given port
	 * @param port, the port of the server you must connect to
	 * @throws Exception
	 */
	public void connect(int port, String adr) throws Exception 
	{
		System.out.println("CLIENT " + client_name + "\tCONNECT TO " + port);
		socket = new Socket(adr, port);
		out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		out.flush();
		in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		sendMessage(Message.MESSAGE_TYPE.KEY, porteCle.getKeyPublic());
	}

	/**
	 * Sends a message using the ObjectOutpurStream
	 * @param o, the Object to send
	 * @throws IOException
	 */
	private void sendMessage(Message.MESSAGE_TYPE type , Object o) throws IOException 
	{
		System.out.println("");
		System.out.println("Send message : "+o.toString());
		Message msg = new Message(type,o.toString());
		if (type == Message.MESSAGE_TYPE.MESSAGE && hostKey != null){
			msg.encryptMess(hostKey);
		}
		System.out.println("Send message encrypt : "+msg.getMessage());
		System.out.println("");
		out.writeObject(msg);
		out.flush();
	}

	/**
	 * Runs the thread
	 */
	public void run() 
	{
		try {
			connect(port, adresse);
			Scanner sc = new Scanner(System.in);
			Message message;
			String msg = "";
			String oldMsg = "";
			boolean kill = false;
			/** Gets the Server input **/
			while (!kill) {	
				System.out.println("Lecture reponse serveur");			
				message = (Message) in.readObject();
				if (message.getType() == Message.MESSAGE_TYPE.KEY) {
					msg = message.getMessage();
					System.out.println(msg);
					System.out.println("");
					String[] hStrings = msg.split(",");
					if (hStrings.length == 2) {
						hostKey = new Key(new BigInteger(hStrings[0]),new BigInteger(hStrings[1]));
						sendMessage(Message.MESSAGE_TYPE.MESSAGE, "OK");
					} else {
						sendMessage(Message.MESSAGE_TYPE.MESSAGE, "NO_KEY");
					}
				} else {
					if (hostKey != null) {
						message.decryptMess(porteCle.getKeyPrivate());
						msg = message.getMessage();
						System.out.println(msg);
						System.out.println("");
						if (msg.equals("QUIT")) {
							kill = true;
						} else if (msg.equals(oldMsg)) {
							sendMessage(Message.MESSAGE_TYPE.MESSAGE, "OK");	
						} else if (msg.equals("NEXT")) {
							oldMsg = sc.nextLine();
							sendMessage(Message.MESSAGE_TYPE.MESSAGE, oldMsg);							
						}
					} else if (message.getMessage().equals("NO_KEY")) {
						System.out.println("NO_KEY resend");
						sendMessage(Message.MESSAGE_TYPE.KEY, porteCle.getKeyPublic());
					} else {
						System.out.println("NO_KEY ask new");
						sendMessage(Message.MESSAGE_TYPE.MESSAGE, "NO_KEY");
					}
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
		if (args.length >=2) {
			Client cli = new Client(Integer.parseInt(args[0]),args[1]);
			cli.start();
		} else {
			System.out.println("Need to pass the port as argument and adress");
		}
	}

}
