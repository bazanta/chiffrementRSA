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
import java.math.BigInteger;

public class ServerToClient extends Thread 
{
	/**
	 * Class variables
	 */
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String client_name;
	private Keys porteCle;
	private Key clientKey;
	
	/**
	 * Constructs a ServerToClient
	 * @param se, the Server it belongs to
	 * @param so, the Socket it communicates with
	 */
	public ServerToClient(Socket so, Keys keys) 
	{
		this.socket = so;
		this.out = null;
		this.in = null;
		this.client_name = "";
		this.porteCle = keys;
		this.clientKey = null;
	}
	
	/**
	 * Runs the ServerToClient
	 */
	public void run() 
	{
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
			/** Send public key */
			sendMessage(Message.MESSAGE_TYPE.KEY, porteCle.getKeyPublic());

			/** Begin the communication */
			Message message;
			String msg = "";
			boolean kill = false;
			while (!kill) {	
				System.out.println("Lecture reponse client");			
				message = (Message) in.readObject();
				if (message.getType() == Message.MESSAGE_TYPE.KEY) {
					msg = message.getMessage();
					System.out.println(msg);
					String[] hStrings = msg.split(",");
					if (hStrings.length == 2) {
						clientKey = new Key(new BigInteger(hStrings[0]), new BigInteger(hStrings[1]));
					} else {
						sendMessage(Message.MESSAGE_TYPE.MESSAGE, "NO_KEY");
					}
				} else {
					if (clientKey != null) {
						message.decryptMess(porteCle.getKeyPrivate());
						msg = message.getMessage();
						System.out.println(msg);
						if (msg.equals("QUIT")) {
							kill = true;
						} else if (msg.equals("OK")) {
							sendMessage(Message.MESSAGE_TYPE.MESSAGE, "NEXT");			
						} else {								
							sendMessage(Message.MESSAGE_TYPE.MESSAGE, msg);						
						}
					} else if (message.getMessage().equals("NO_KEY")) {
						System.out.println("NO_key resend");
						sendMessage(Message.MESSAGE_TYPE.KEY, porteCle.getKeyPublic());
					} else {
						System.out.println("NO_key ask new");
						sendMessage(Message.MESSAGE_TYPE.MESSAGE, "NO_KEY");
					}
				}
			}
			/** End the communication **/
			System.out.println("CLIENT \tQUIT");
			sendMessage(Message.MESSAGE_TYPE.MESSAGE, "QUIT");
			
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Sends a message using the ObjectOutpurStream
	 * @param o, the Object to send
	 * @throws IOException
	 */
	private void sendMessage(Message.MESSAGE_TYPE type , Object o) throws IOException 
	{
		System.out.println("Send message : "+o.toString());
		Message msg = new Message(type,o.toString());
		if (type == Message.MESSAGE_TYPE.MESSAGE && clientKey != null){
			msg.encryptMess(clientKey);
		}
		System.out.println("Send message encrypt : "+msg.getMessage());
		out.writeObject(msg);
		out.flush();
	}
}
