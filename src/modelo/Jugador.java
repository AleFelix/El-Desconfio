package modelo;

public class Jugador {
	private String nombre;
	private boolean ganador;
	private MazoDinamico baraja;
	
	public Jugador(String n) {
		this.nombre = n;
		this.ganador = false;
		baraja = new MazoDinamico();
	}
	
	public void setCarta(int indice, Carta cart) {
		baraja.setCarta(indice, cart);
	}
	
	public MazoDinamico getBaraja() {
		return baraja;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setGanador() {
		this.ganador = true;
	}
	
	public boolean getEstado() {
		return ganador;
	}
}
