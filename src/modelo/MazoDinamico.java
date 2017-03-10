package modelo;

public class MazoDinamico extends Mazo implements IMazoDinamico {
	
	public int getPosicionUltimaCarta() {
		return cartas.size()-1;
	}
	
	public void setCartaAlFinal(Carta cart) {
		cartas.add(cart);
	}
	
	public Carta getCartaFinal() {
		return cartas.get(cartas.size()-1);
	}
	
	public void vaciar() {
		cartas.clear();
	}

}
