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
}