package modelo;

public class Carta implements ICarta {
	private int palo;
	private int numero;
	
	public Carta(int p, int n) {
		this.palo = p;
		this.numero = n;
	}
	
	public int getPalo() {
		return palo;
	}
	
	public int getNumero() {
		return numero;
	}
}
