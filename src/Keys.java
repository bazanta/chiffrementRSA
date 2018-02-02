/****************************/
/* Projet réseau            */
/* BAZANTE Alice            */
/* LEBLANC Maxime           */
/* MAINFROID Pierre-Olivier */
/****************************/

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class Keys {

	final static int TAILLE_NOMBRE = 2048;
	/**
	 * Class variables
	 */
	private Key key_public;
	private Key key_private;

	/**
	 * Constructeur clés public private
	 */
	public Keys() 
	{
		BigInteger m = generatePublicKey();
		generatePrivateKey(m);
	}
	public Keys(BigInteger p, BigInteger q) 
	{
		BigInteger m = generatePublicKey(p, q);
		generatePrivateKey(m);
	}

	/**
	 * Calcul la clé public
	 */
	private BigInteger generatePublicKey(BigInteger p, BigInteger q) 
	{
		BigInteger m = calculPublicKey(p,q);
		return m;
	}

	private BigInteger generatePublicKey() 
	{
		Random rand = new Random();		
		BigInteger p = BigInteger.probablePrime(TAILLE_NOMBRE, rand);
		BigInteger q = BigInteger.probablePrime(TAILLE_NOMBRE, rand);
		while (q.equals(p)) {
			q = BigInteger.probablePrime(TAILLE_NOMBRE, rand);
		}

		BigInteger m = calculPublicKey(p,q);
		return m;
	}

	private BigInteger calculPublicKey(BigInteger p, BigInteger q) 
	{
		Random rand = new Random();	
		BigInteger n = p.multiply(q);
		BigInteger m = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		boolean condition = false;
		BigInteger e = null;
		while (!condition) {
			e = BigInteger.probablePrime(8, rand);
			if (!(e.mod(new BigInteger("2")).equals(BigInteger.ZERO)) && m.gcd(e).equals(BigInteger.ONE)) {
				condition = true;
			}
		}
		this.key_public = new Key(n,e);
		return m;
	}

	/**
	 * Calcul la clé privée
	 */
	private void generatePrivateKey(BigInteger m) 
	{
		BigInteger u = euclide(this.key_public, m);
		this.key_private = new Key(this.key_public.first(),u);
	}

	public BigInteger euclide(Key pub, BigInteger m) 
	{
		// A = e & B = m
		BigInteger uMoins = BigInteger.ONE;
		BigInteger ui = BigInteger.ZERO;
		BigInteger uTmp = ui;

		BigInteger vMoins = BigInteger.ZERO;
		BigInteger vi = BigInteger.ONE;
		BigInteger vTmp = vi;

		BigInteger rMoins = pub.second();
		BigInteger ri = m;
		BigInteger rTmp = m;
		
		while (!ri.equals(BigInteger.ZERO)) {

			rTmp = rMoins.subtract(rMoins.divide(ri).multiply(ri));
			uTmp = uMoins.subtract(rMoins.divide(ri).multiply(ui));
			vTmp = vMoins.subtract(rMoins.divide(ri).multiply(vi));

			rMoins = ri;
			ri = rTmp;
			uMoins = ui;
			ui = uTmp;
			vMoins = vi;
			vi = vTmp;
		}
		
		BigInteger k = new BigInteger("-1");
		while (uMoins.compareTo(new BigInteger("2")) != 1 || uMoins.compareTo(m) != -1) {
			uMoins = uMoins.subtract(m.multiply(k));
			k = k.subtract(BigInteger.ONE);
		}
		return uMoins;
	}

	public String toString() 
	{
		String ret = "Clé publique(" + this.key_public.first() + "," + this.key_public.second() + ")" + "\n";
		ret += "Clé privée(" + this.key_private.first() + "," + this.key_private.second() + ")" + "\n";
		return ret;
	}

	public Key getKeyPublic() 
	{
		return this.key_public;
	}

	public Key getKeyPrivate() 
	{
		return this.key_private;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Keys keysVerif = new Keys(new BigInteger("53"), new BigInteger("97"));
		System.out.println(keysVerif.toString());

		Message msg = new Message(Message.MESSAGE_TYPE.MESSAGE, "Bravo");
		System.out.println(msg.getMessage());
		msg.encryptMess(keysVerif.getKeyPublic());
		System.out.println(msg.getMessage());

		msg.decryptMess(keysVerif.getKeyPrivate());
		System.out.println(msg.getMessage());
	}
}