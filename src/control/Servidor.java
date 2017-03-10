package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import vista.Cliente;
import modelo.Desconfio;

public class Servidor {
	private int listos = 0;
	private boolean paloElegido = false;
	private Cliente cl = new Cliente();
	private Desconfio df = new Desconfio();
	private ServerSocket servidor;
	private ArrayList<Socket> conexiones = new ArrayList<Socket>();
	private ArrayList<ObjectInputStream> escuchadores = new ArrayList<ObjectInputStream>();
	private ArrayList<ObjectOutputStream> escritores = new ArrayList<ObjectOutputStream>();
	private Semaphore semaforo = new Semaphore(1);
	
	public Servidor() {
		this.obtenerTipo();
	}
	
	private void obtenerTipo() {
		new Thread(new Runnable() {
			public void run() {
				while (cl.getTipo() == 0) {
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (cl.getTipo() == 1)
					comenzarServidor();
			}
		}).start();
	}
	
	private void comenzarServidor() {
		try {
		servidor = new ServerSocket(cl.getPuerto());
		escucharClientes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void escucharClientes() {
		new Thread(new Runnable() {
			public void run() {
				while (conexiones.size() < 6 && df.getEstado() == Desconfio.PREPARANDO) {
					try{
					servidor.setSoTimeout(100);
					conexiones.add(servidor.accept());
					escritores.add(new ObjectOutputStream(conexiones.get(conexiones.size()-1).getOutputStream()));
					escucharMensajes();
					}catch (IOException e) {
					}
				}
			}
		}).start();
	}
	
	private void escucharMensajes() {
		new Thread(new Runnable() {
			public void run() {
				try {
				int indice = conexiones.size()-1;
				escuchadores.add(new ObjectInputStream(conexiones.get(indice).getInputStream()));
				while(true) {
					aceptarMensaje((Mensaje) escuchadores.get(indice).readObject());
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void aceptarMensaje(final Mensaje msg) {
		new Thread(new Runnable() {
			public void run() {
				Mensaje m = new Mensaje(msg.getTipo(),msg.getMensaje(),msg.getCartas());
				mensajeRecibido(m);
			}
		}).start();
	}
	
	private void mensajeRecibido(Mensaje m) {
		switch (m.getTipo()) {
		case Mensaje.MSG_CLIENTE_NOMBRE:
			df.cargarJugador(m.getMensaje());
			enviarMensaje(conexiones.size()-1,Mensaje.MSG_SERVIDOR_ID,String.valueOf(conexiones.size()-1));
			enviarMensajeATodos(Mensaje.MSG_SERVIDOR_CANTIDAD,String.valueOf(conexiones.size()));
			break;
		case Mensaje.MSG_CLIENTE_INICIAR:
			enviarMensajeATodos(Mensaje.MSG_SERVIDOR_JUGAR,"");
			df.iniciarJuego();
			df.repartirCartas();
			mostrarRonda();
			break;
		case Mensaje.MSG_CLIENTE_LISTO:
			listos++;
			if (listos == conexiones.size()) {
				listos = 0;
				desarrolloJuego();
			}
			break;
		case Mensaje.MSG_CLIENTE_DESCARTAR:
			descartar(m.getMensaje());
			break;
		case Mensaje.MSG_CLIENTE_PALOELEGIDO:
			establecerPalo(m.getMensaje());
			break;
		case Mensaje.MSG_CLIENTE_DESCONFIAR:
			desconfiando(m.getMensaje());
			break;
		case Mensaje.MSG_CLIENTE_REINICIAR:
			reiniciarPartida();
			break;
		case Mensaje.MSG_CLIENTE_DESCONEXION:
			enviarMensajeATodos(Mensaje.MSG_SERVIDOR_DESCONECTAR,m.getMensaje());
			break;
		}
	}
	
	private void reiniciarPartida() {
		enviarMensajeATodos(Mensaje.MSG_SERVIDOR_REINICIAR,"");
		Desconfio df2 = new Desconfio();
		for (int i=0;i<df.getCantidadJugadores();i++)
			df2.cargarJugador(df.getNombreJugador(i));
		df = df2;
		try {
		Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Mensaje m = new Mensaje(Mensaje.MSG_CLIENTE_INICIAR,"");
		mensajeRecibido(m);
	}
	
	private void desconfiando(String id) {
		int iJugDesconfiado = Integer.valueOf(id);
		int iJugSospechoso = df.getIndJugAnt();
		enviarMensajeATodos(Mensaje.MSG_SERVIDOR_DESVELAR,df.verCartaPozo());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String resultado = null;
		if (df.desconfiar()) {
			df.sumarCartas(iJugDesconfiado);
			df.setJugadorActual(iJugSospechoso);
			resultado = "El jugador "+df.getNombreJugador(iJugDesconfiado)+" desconfio de "+df.getNombreJugador(iJugSospechoso)+" pero lo juzgo mal.";
		} else {
			df.sumarCartas(iJugSospechoso);
			df.setJugadorActual(iJugDesconfiado);
			resultado = "¡El jugador "+df.getNombreJugador(iJugDesconfiado)+" desconfio de "+df.getNombreJugador(iJugSospechoso)+" y tuvo razon!";
		}
		enviarMensajeATodos(Mensaje.MSG_SERVIDOR_DESCONFIANZA,resultado);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		paloElegido = false;
		mostrarRonda();
		
	}
	
	private void establecerPalo(String numPalo) {
		int p = Integer.valueOf(numPalo);
		df.setPalo(p);
		paloElegido = true;
		enviarMensajeATodos(Mensaje.MSG_SERVIDOR_CAMBIOPALO,numPalo);
	}
	
	private void descartar(String carta) {
		int palo = getPalo(carta);
		int numero = getNumero(carta);
		int iCarta = df.obtenerIndiceCarta(palo, numero, df.getiJugActual());
		df.descartar(iCarta);
		if (df.getEstado() == Desconfio.FIN)
			finDelJuego();
		else {
			df.cambioJugador();
			desarrolloJuego();
		}
	}
	
	private void finDelJuego() {
		paloElegido = false;
		enviarMensajeATodos(Mensaje.MSG_SERVIDOR_FIN,df.getGanador());
	}
	
	private int getPalo(String carta) {
		return Integer.valueOf(carta.substring(0,1));
	}
	
	private int getNumero(String carta) {
		return Integer.valueOf(carta.substring(1));
	}
	
	private void enviarMensajeATodos(final String tipo, final String mensaje) {
		new Thread(new Runnable() {
			public void run() {
				for (int i=0;i<conexiones.size();i++)
					enviarMensaje(i, tipo, mensaje);
			}
		}).start();
	}
	
	private void realizarEnvio(int indiceJug, Mensaje msg) {
		try {
			Mensaje m = new Mensaje(msg.getTipo(),msg.getMensaje(),msg.getCartas());
			semaforo.acquire();
			escritores.get(indiceJug).writeObject(m);
			escritores.get(indiceJug).flush();
			semaforo.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private void mostrarRonda() {
		for (int i=0;i<conexiones.size();i++)
			enviarMensaje(i,Mensaje.MSG_SERVIDOR_CARTAS,"",df.verBarajaJugador(i));
	}
	
	private void enviarMensaje(int indiceJug, String tipo, String mensaje,int[][] cartas) {
		Mensaje m = new Mensaje(tipo,mensaje,cartas);
		realizarEnvio(indiceJug,m);
	}
	
	private void enviarMensaje(int indiceJug, String tipo, String mensaje) {
		Mensaje m = new Mensaje(tipo,mensaje);
		realizarEnvio(indiceJug,m);
	}
	
	private void enviarMensajeAlResto(final int indice, final String tipo, final String mensaje) {
		new Thread(new Runnable() {
			public void run() {
				for (int i=0;i<conexiones.size();i++) {
					Mensaje m = new Mensaje(tipo,mensaje);
					if (indice != i)
						realizarEnvio(i,m);
				}
				
			}
		}).start();
	}
	
	private void desarrolloJuego() {
			enviarMensaje(df.getiJugActual(),Mensaje.MSG_SERVIDOR_MOSTRAR,"");
			if (!paloElegido)
				enviarMensaje(df.getiJugActual(),Mensaje.MSG_SERVIDOR_PALO,"");
			if (df.puedeDesconfiar())
				enviarMensajeATodos(Mensaje.MSG_SERVIDOR_PUEDEDESCONFIAR,String.valueOf(df.getIndJugAnt()));
			enviarMensajeAlResto(df.getiJugActual(),Mensaje.MSG_SERVIDOR_ESPERAR,df.getNombreJugador(df.getiJugActual()));
			enviarMensajeATodos(Mensaje.MSG_SERVIDOR_POZO,String.valueOf(df.getTamañoPozo()));
	}
		
}
	