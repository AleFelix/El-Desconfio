package control;

import java.io.Serializable;

public class Mensaje implements Serializable, IMensaje {
	
	private static final long serialVersionUID = 1L;

	private String tipo;
	private String mensaje;
	private int[][] cartas = null;
	
	public Mensaje(String t, String m) {
		this.tipo = t;
		this.mensaje = m;
	}
	
	public Mensaje(String t, String m, int[][] c) {
		this.tipo = t;
		this.mensaje = m;
		this.cartas = c;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public int[][] getCartas() {
		return cartas;
	}
	
}
