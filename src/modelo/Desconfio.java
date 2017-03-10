package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Desconfio {
	private int estado = 0;
	private int cantidadJugadores = 0;
	private int iJugActual;
	private int paloActual;
	private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
	private MazoDelJuego mazoCartas = new MazoDelJuego();
	private MazoDinamico pozo = new MazoDinamico();
	public static final int PREPARANDO = 0;
	public static final int JUGANDO = 1;
	public static final int FIN = 2;
	
	public void iniciarJuego() {
		iJugActual = 0;
		estado = JUGANDO;
	}
	
	public int getIndiceJugador(String nombre) {
		int indice = 0;
		for (int i=0;i<jugadores.size();i++)
			if (jugadores.get(i).getNombre().equals(nombre))
				indice = i;
		return indice;
	}
	
	public boolean puedeDesconfiar() {
		if (pozo.getTamaño() < 3)
			return false;
		else
			return true;
	}
	
	public String getNombreJugador(int indice) {
		return jugadores.get(indice).getNombre();
	}
	
	public int getEstado() {
		return estado;
	}
	
	public void cargarJugador(String nombre) {
		jugadores.add(new Jugador(nombre));
		cantidadJugadores++;
	}
	
	public void repartirCartas() {
		mazoCartas.mezclar();
		int tamañoBaraja = (int) mazoCartas.getTamaño()/cantidadJugadores;
		for (int i=0;i<cantidadJugadores;i++) {
			for (int j=0;j<tamañoBaraja;j++) {
				jugadores.get(i).getBaraja().setCartaAlFinal(mazoCartas.getCarta(0));
				mazoCartas.removerCarta(0);
			}
		}
		int cantidadRestante = mazoCartas.getTamaño();
		if (cantidadRestante > 0) {
			int j = 0;
			for (int i=0;i<cantidadRestante;i++) {
				if (j == jugadores.size())
					j = 0;
				jugadores.get(j).getBaraja().setCartaAlFinal(mazoCartas.getCarta(0));
				mazoCartas.removerCarta(0);
				j++;
			}
		}
	}
	
	public int obtenerIndiceCarta(int p, int num, int jug) {
		int indice = 0;
		for (int i=0;i<jugadores.get(jug).getBaraja().getTamaño();i++) {
			if (jugadores.get(jug).getBaraja().getCarta(i).getPalo() == p)
				if (jugadores.get(jug).getBaraja().getCarta(i).getNumero() == num)
					indice = i;
		}
		return indice;
	}
	
	public void descartar(int iCarta) {
		pozo.setCartaAlFinal(jugadores.get(iJugActual).getBaraja().getCarta(iCarta));
		jugadores.get(iJugActual).getBaraja().removerCarta(iCarta);
		verificarGanador();
	}
	
	public void setPalo(int p) {
		paloActual = p;
	}
	
	public boolean desconfiar() {
		if (pozo.getCartaFinal().getPalo() == paloActual || pozo.getCartaFinal().getPalo() == 5)
			return true;
		else
			return false;
	}
	
	public int getIndJugAnt() {
		int jugAnt = iJugActual-1;
		if (jugAnt == -1)
			jugAnt = jugadores.size()-1;
		return jugAnt;
	}
	
	public void cambioJugador() {
		iJugActual++;
		if (iJugActual == cantidadJugadores)
			iJugActual = 0;
	}
	
	public void sumarCartas(int indiceJug) {
		Jugador j = jugadores.get(indiceJug);
		for (int i=0;i<pozo.getTamaño();i++) {
			j.getBaraja().setCartaAlFinal(pozo.getCarta(i));
		}
		pozo.vaciar();
	}
	
	private int[][] ordenarBarajaVisible(int[][] baraja) {
		Arrays.sort(baraja, new Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return a[0] - b[0];
		    }
		});
		Arrays.sort(baraja, new Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return a[1] - b[1];
		    }
		});
		return baraja;
	}
	
	public int[][] verBarajaJugador(int indice) {
		Jugador j = jugadores.get(indice);
		int barajaCartas[][] = new int[j.getBaraja().getTamaño()][2];
		for (int i=0;i<j.getBaraja().getTamaño();i++) {
			barajaCartas[i][0] = j.getBaraja().getCarta(i).getNumero();
			barajaCartas[i][1] = j.getBaraja().getCarta(i).getPalo();
		}
		barajaCartas = ordenarBarajaVisible(barajaCartas);
		return barajaCartas;
	}
	
	Jugador obtenerJugador(String nombre) {
		Jugador j = null;
		for (int i=0;i<jugadores.size();i++) {
			if (jugadores.get(i).getNombre().equals(nombre))
				j = jugadores.get(i);
		}
		return j;
	}
	
	public int getCantidadJugadores() {
		return cantidadJugadores;
	}

	public int getiJugActual() {
		return iJugActual;
	}
	
	public void verificarGanador() {
		if (jugadores.get(iJugActual).getBaraja().getTamaño() == 0) {
			jugadores.get(iJugActual).setGanador();
			this.estado = FIN;
		}
	}
	
	public String getGanador() {
		Jugador j = null;
		for (int i=0;i<jugadores.size();i++)
			if (jugadores.get(i).getEstado())
				j = jugadores.get(i);
		return j.getNombre();
	}
	
	public int getTamañoPozo() {
		return pozo.getTamaño();
	}
	
	public void setJugadorActual(int indice) {
		iJugActual = indice;
	}
	
	public String verCartaPozo() {
		int p = pozo.getCartaFinal().getPalo();
		int n = pozo.getCartaFinal().getNumero();
		return (String.valueOf(p)+String.valueOf(n));
	}

}
