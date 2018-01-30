/****************************/
/* Projet réseau            */
/* BAZANTE Alice            */
/* LEBLANC Maxime           */
/* MAINFROID Pierre-Olivier */
/****************************/

public class Message {

	public enum MESSAGE_TYPE {
		KEY,
		MESSAGE
	};
	/**
	 * Attributs de classes :
	 * type : le type de message qui peut être une clé ou un message encypté
	 * msg : le message passé
	 */
	private MessageType type;
	private String msg;


	public Message(MessageType t, String m) {
		type = t;
		msg = m;
	}

	public MessageType getType() {
		return type;
	}

	public String getMessage() {
		return msg;
	}
}
