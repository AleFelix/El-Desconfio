package vista;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import control.Mensaje;

public class Cliente {
	private boolean desconectado = false;
	private String id;
	private int tipo = 0;
	private int puerto = 0;
	private int cantJug = 1;
	private boolean partidaIniciada = false;
	private String nombre;
	private String ip;
	private Ventana v = new Ventana();
	private Socket conexion;
	private ObjectInputStream escuchador;
	private ObjectOutputStream escritor;
	private Semaphore semaforo = new Semaphore(1);
	
	public Cliente() {
		v.setCliente(this);
		v.setVisible(true);
	}
	
	public int getTipo() {
		return this.tipo;
	}
	
	public int getPuerto() {
		return puerto;
	}
	
	public String getId () {
		return this.id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getCantJug() {
		return this.cantJug;
	}
	
	private void setCantJug(int cantJug) {
		if (this.cantJug != cantJug)
			this.cantJug = cantJug;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
		iniciarCliente();
	}

	public void establecerTipo(String t, String p) {
		if (t.equals("s"))
			this.tipo = 1;
		else
			this.tipo = 2;
		this.puerto = Integer.valueOf(p);
	}
	
	public void establecerIP(String ip) {
		this.ip = ip;
	}
	
	private void iniciarCliente() {
		try {
		conexion = new Socket(ip,puerto);
		conexion.setSoTimeout(1000);
		iniciarStreamers();
		conexion.setSoTimeout(0);
		enviarMensaje(Mensaje.MSG_CLIENTE_NOMBRE,nombre);
		actualizarCantidadJugadores();
		escucharMensajes();
		} catch (Exception e) {
			e.printStackTrace();
			v.noHayConexion();
		}
	}

	private void enviarMensaje(String tipo, String msg) {
		try {
		Mensaje m = new Mensaje(tipo,msg);
		semaforo.acquire();
		escritor.writeObject(m);
		escritor.flush();
		semaforo.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void iniciarStreamers() {
		try {
		escuchador = new ObjectInputStream(conexion.getInputStream());
		escritor = new ObjectOutputStream(conexion.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			v.noHayConexion();
		}
	}
	
	private void actualizarCantidadJugadores() {
		new Thread(new Runnable() {
			public void run() {
				while (true && !partidaIniciada) {
				v.setLblCantjug(String.valueOf(getCantJug()));
				}
			}
		}).start();
	}
	
	private void escucharMensajes() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
					Object aux = escuchador.readObject();
					if (aux != null) {
						aceptarMensaje((Mensaje)aux);
					}
					} catch (Exception e) {
						e.printStackTrace();
						if (!desconectado)
							v.desconexion();
					}
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
	
	private void mensajeRecibido(Mensaje msg) {
		switch (msg.getTipo()) {
		case Mensaje.MSG_SERVIDOR_CANTIDAD:
			setCantJug(Integer.valueOf(msg.getMensaje()));
			break;
		case Mensaje.MSG_SERVIDOR_ID:
			setId(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_JUGAR:
			partidaIniciada = true;
			v.cambiarVentana();
			break;
		case Mensaje.MSG_SERVIDOR_CARTAS:
			v.setCartas(msg.getCartas());
			break;
		case Mensaje.MSG_SERVIDOR_MOSTRAR:
			v.ventanaActiva();
			break;
		case Mensaje.MSG_SERVIDOR_ESPERAR:
			v.setJugadorActual(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_PUEDEDESCONFIAR:
			v.setBotonDesconfiar(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_POZO:
			v.setPozo(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_PALO:
			v.setPalo();
			break;
		case Mensaje.MSG_SERVIDOR_CAMBIOPALO:
			v.mostrarPalo(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_DESVELAR:
			v.mostrarPozo(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_DESCONFIANZA:
			v.mostrarDesconfianza(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_FIN:
			v.finalDelJuego(msg.getMensaje());
			break;
		case Mensaje.MSG_SERVIDOR_REINICIAR:
			v.reiniciarPartida();
			break;
		case Mensaje.MSG_SERVIDOR_DESCONECTAR:
			desconectado = true;
			try {
				conexion.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			v.desconectar(msg.getMensaje());
			break;
		}
	}
	
	private void setId(String n) {
		this.id = n;
	}
	
	public void iniciarPartida() {
		enviarMensaje(Mensaje.MSG_CLIENTE_INICIAR, "");
	}
	
	public void preparado() {
		enviarMensaje(Mensaje.MSG_CLIENTE_LISTO,"");
	}
	
	public void descartar(String carta) {
		enviarMensaje(Mensaje.MSG_CLIENTE_DESCARTAR,carta);
	}
	
	public void enviarpalo(String numPalo) {
		enviarMensaje(Mensaje.MSG_CLIENTE_PALOELEGIDO,numPalo);
	}
	
	public void desconfiar() {
		enviarMensaje(Mensaje.MSG_CLIENTE_DESCONFIAR,id);
	}
	
	public void reiniciarPartida() {
		enviarMensaje(Mensaje.MSG_CLIENTE_REINICIAR,"");
	}
	
	public void ventanaCerrada() {
		enviarMensaje(Mensaje.MSG_CLIENTE_DESCONEXION,nombre);
	}
	
}
