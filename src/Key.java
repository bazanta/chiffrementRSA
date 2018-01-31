/****************************/
/* Projet réseau            */
/* BAZANTE Alice            */
/* LEBLANC Maxime           */
/* MAINFROID Pierre-Olivier */
/****************************/

import java.math.BigInteger;

public class Key {

	private BigInteger first;
    private BigInteger second;

    public Key(BigInteger p, BigInteger q) {
		this.first = p;
        this.second = q;
	}
	
	public BigInteger first() {
        return this.first;
    }
    public BigInteger second() {
        return this.second;
    }

    public String toString() {
		return "(" + this.first + "," + this.second + ")" + "\n";
    } 
    
    /**
     * Encrypte un message
     * @param msg
     * @return msgEncrypté
     */
    public String encrypt(String msg)
    {
		String msgASCII = String2ASCII(msg);
		String[] msgASCIITab = msgASCII.split(" ");
        String msgEncrypt = "";
        
		for (int i = 0; i < msgASCIITab.length ; i++) {
			BigInteger lettre = new BigInteger(msgASCIITab[i]);
			BigInteger lettreEncrypt = lettre.modPow(this.second, this.first);
			msgEncrypt += lettreEncrypt.toString() + " ";
		}
       	return msgEncrypt;       
    }

    /**
     * Décrypte un message
     * @param msg
     * @return msgDecrypté
     */
	public String decrypt(String msg) 
	{
		String msgDecrypt = "";
		String[] tabDecrypt = msg.split(" ");
		
		for (int i = 0; i < tabDecrypt.length; i++) {
			if (!tabDecrypt[i].contains(" ") && !tabDecrypt[i].equals("")) {
				BigInteger lettre = new BigInteger(""+tabDecrypt[i]);
				BigInteger lettreDecrypt = lettre.modPow(this.second, this.first);
				msgDecrypt += ((char)lettreDecrypt.intValue());
			}
		}

       	return msgDecrypt;
	}
    
    /**
     * Converti String vers ASCII
     * @param message
     * @return
     */
	public static String String2ASCII(String msg) 
	{
        String msgASCII = "";
        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);
			msgASCII += new Integer(c);	
			if (i < msg.length()-1) {
				msgASCII += " ";
			}		
        }
        return msgASCII;
	}
}