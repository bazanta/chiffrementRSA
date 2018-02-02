import java.math.BigInteger;

/****************************/
/* Projet réseau            */
/* BAZANTE Alice            */
/* LEBLANC Maxime           */
/* MAINFROID Pierre-Olivier */
/****************************/

import java.io.Serializable;

public class Message implements Serializable {

	public enum MESSAGE_TYPE {
		KEY,
		MESSAGE,
		PROTOCOL,
	};

	/**
	 * Attributs de classes :
	 * type : le type de message qui peut être une clé ou un message encypté
	 * msg : le message passé
	 */
	private MESSAGE_TYPE type;
	private String msg;


	public Message(MESSAGE_TYPE t, String m) 
	{
		this.type = t;
		this.msg = m;
	}

	public MESSAGE_TYPE getType() 
	{
		return this.type;
	}

	public String getMessage() 
	{
		return this.msg;
	}

	public void decryptMess(Key key) 
	{
        if (this.type == MESSAGE_TYPE.MESSAGE) {
            String msgDecrypt = key.decrypt(this.msg);
            this.msg = msgDecrypt;
        }		       
    }

	public void encryptMess(Key key)
	{
        if (this.type == MESSAGE_TYPE.MESSAGE) {
            String msgEncrypt = key.encrypt(this.msg);
            this.msg = msgEncrypt;
        }       
	}
}
