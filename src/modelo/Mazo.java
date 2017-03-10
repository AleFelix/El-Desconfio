package modelo;
import java.util.ArrayList;

public class Mazo {
	protected ArrayList<Carta> cartas = new ArrayList<Carta>();
	
	public Carta getCarta(int indice) {
		return cartas.get(indice);
	}
	
	public void setCarta(int indice, Carta cart) {
		cartas.add(indice, cart);
	}
	
	public int getTamaño() {
		return cartas.size();
	}
	
	public void removerCarta(int iCarta) {
		cartas.remove(iCarta);
	}
}
