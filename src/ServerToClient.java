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

public class ServerToClient extends Thread {
	
	/**
	 * Class variables
	 */
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String client_name;
	
	/**
	 * Constructs a ServerToClient
	 * @param se, the Server it belongs to
	 * @param so, the Socket it communicates with
	 */
	public ServerToClient(Socket so) {
		socket = so;
		out = null;
		in = null;
		client_name = "";
	}
	
	/**
	 * Runs the ServerToClient
	 */
	public void run() {
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
			/** Communication protocol **/
			String message;
			while (!(message = (String) in.readObject()).contains("NAME")) {}
			String[] tab = message.split(" ");
			if (tab.length > 1) {
				client_name = tab[1];
				System.out.println(client_name + " : ASKED TO CONNECT");
			}

			sendMessage("CONNECTED");

			/**
			 * Géneration des clés private and public
			 */

			/** While the Client doesn't quit, read its input **/
			while (!(message = (String) in.readObject()).equals("QUIT")) {
				if (message != null) {
					/**
					 *
					 */
				}
			}
			/** End the communication **/
			System.out.println(client_name + " : QUIT");
			sendMessage("QUIT");
			
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Envoi des messages
	 * @param o,
	 * @throws IOException
	 */
	private void sendMessage(Object o) throws IOException {
		out.writeObject(o);
		out.flush();
	}
}
