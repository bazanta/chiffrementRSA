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
	public Keys() {
		BigInteger n = generatePublicKey();
		generatePrivateKey(n);
	}
	public Keys(BigInteger p, BigInteger q) {
		BigInteger n = generatePublicKey(p, q);
		generatePrivateKey(n);
	}

	/**
	 * Calcul la clé public
	 */
	private BigInteger generatePublicKey(BigInteger p, BigInteger q) {
		BigInteger n = calculPublicKey(p,q);
		return n;
	}

	private BigInteger generatePublicKey() {
		Random rand = new Random();		
		BigInteger p = BigInteger.probablePrime(TAILLE_NOMBRE, rand);
		BigInteger q = BigInteger.probablePrime(TAILLE_NOMBRE, rand);
		while(q.equals(p))
		{
			q = BigInteger.probablePrime(TAILLE_NOMBRE, rand);
		}

		BigInteger n = calculPublicKey(p,q);
		return n;
	}

	private BigInteger calculPublicKey(BigInteger p, BigInteger q) {
		Random rand = new Random();	
		BigInteger n = p.multiply(q);
		BigInteger m = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		boolean condition = false;
		BigInteger e = null;
		while(!condition)
		{
			e = BigInteger.probablePrime(8, rand);
			if(!(e.mod(new BigInteger("2")).equals(BigInteger.ZERO)) && m.gcd(e).equals(BigInteger.ONE))
			{
				condition = true;
			}
		}
		this.key_public = new Key(n,e);
		return n;
	}

	/**
	 * Calcul la clé privée
	 */
	private void generatePrivateKey(BigInteger n) {

	}

	public String toString() {
		return "Clé publique(" + this.key_public.first() + "," + this.key_public.second() + ")" + "\n";
	}

	public Key getKeyPublic() {
		return this.key_public;
	}

	public Key getKeyPrivate() {
		return this.key_private;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Keys keysVerif = new Keys(new BigInteger("53"), new BigInteger("97"));
		System.out.println(keysVerif.toString());

		Keys keys = new Keys();
		System.out.println(keys.toString());
	}
}