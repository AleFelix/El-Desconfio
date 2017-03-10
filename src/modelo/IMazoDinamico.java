package modelo;

public interface IMazoDinamico {

	int getPosicionUltimaCarta();
	
	void setCartaAlFinal(Carta cart);
	
	Carta getCartaFinal();
	
	void vaciar();
	
}
