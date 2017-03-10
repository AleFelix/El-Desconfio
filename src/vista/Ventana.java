package vista;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;

import javax.swing.JTextField;

import java.awt.CardLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;

import java.awt.Component;
import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Ventana extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int CANTCARTAS = 50;
	private JPanel contentPane;
	private Cliente c;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JTextField txtPuerto;
	private JTextField textField;
	private JLabel lblCantjug;
	private ArrayList<JButton> botones = new ArrayList<JButton>();
	private JPanel palo1;
	private JPanel palo2;
	private JPanel palo3;
	private JPanel palo4;
	private JPanel palo5;
	private JButton btnDesconfiar;
	private JLabel lblTurno;
	private JLabel lblPozo;
	private JPanel panel_9;
	private JLabel lblPaloactual;
	private JLabel lblCartaactual;
	private JPanel panel_4;
	private JLabel lblComodines;
	private JPanel panel_5;
	private JTextField txtIp;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnServidor;
	private JRadioButton rdbtnCliente;
	private JLabel lblNombreganador;
	private JTabbedPane tabbedPane;
	private String[] nombrePalos = {"Espada","Basto","Oro","Copa","Comodin"};
	private JPanel[] panelesPalos;
	private int iPaloActual = -1;
	private int paloCartaActual;
	private int numCartaActual;
	private String paloElegido;
	private BufferedImage[][] imagenes = new BufferedImage[5][12];
	private Semaphore semaforo = new Semaphore(1);

	public Ventana() {
		setBackground(new Color(255, 255, 224));
		cargarImagenes();
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        c.ventanaCerrada();
		    }
		});
		setTitle("El Desconfio");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 319);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 224));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		panel = new JPanel();
		panel.setBackground(new Color(255, 255, 224));
		contentPane.add(panel);
		
		rdbtnServidor = new JRadioButton("Crear una partida");
		rdbtnServidor.setBackground(new Color(255, 255, 224));
		rdbtnServidor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonGroup.add(rdbtnServidor);
		rdbtnServidor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				txtIp.setEnabled(false);
			}
		});
		
		JLabel lblTituloinicial = new JLabel("Puede crear una partida o unirse a una ya creada.");
		lblTituloinicial.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTituloinicial.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblIp = new JLabel("IP:");
		lblIp.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtIp = new JTextField();
		txtIp.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtIp.setText("localhost");
		txtIp.setColumns(10);
		
		rdbtnCliente = new JRadioButton("Unirse a una partida");
		rdbtnCliente.setBackground(new Color(255, 255, 224));
		rdbtnCliente.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonGroup.add(rdbtnCliente);
		rdbtnCliente.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				txtIp.setEnabled(true);
			}
		});
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtPuerto = new JTextField();
		txtPuerto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtPuerto.setColumns(10);
		
		JButton btnIniciarpartida = new JButton("Iniciar");
		btnIniciarpartida.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnIniciarpartida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtPuerto.getText().equals(""))
					JOptionPane.showMessageDialog(contentPane,"Debe ingresar un numero de puerto");
				else {
					if (rdbtnServidor.isSelected()) {
						c.establecerTipo("s",txtPuerto.getText());
						c.establecerIP("localhost");
						CardLayout card = (CardLayout) contentPane.getLayout();
						card.next(contentPane);
					} else if (rdbtnCliente.isSelected()) {
						if (txtIp.getText().equals(""))
							JOptionPane.showMessageDialog(contentPane,"Debe ingresar un numero de ip");
						else {
							c.establecerTipo("c",txtPuerto.getText());
							c.establecerIP(txtIp.getText());
							CardLayout card = (CardLayout) contentPane.getLayout();
							card.next(contentPane);
						}
					} else {
						JOptionPane.showMessageDialog(contentPane,"Debe Seleccionar \"Crear\" o \"Unirse\"");
					}
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(6)
					.addComponent(lblTituloinicial, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addGap(8))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(6)
					.addComponent(rdbtnServidor, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
					.addGap(61)
					.addComponent(lblIp, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(txtIp, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
					.addGap(8))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(6)
					.addComponent(rdbtnCliente, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
					.addGap(31)
					.addComponent(lblPuerto, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addGap(13)
					.addComponent(txtPuerto, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
					.addGap(8))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(171)
					.addComponent(btnIniciarpartida, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(182))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(11)
					.addComponent(lblTituloinicial, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
					.addGap(34)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnServidor, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(1)
							.addComponent(lblIp))
						.addComponent(txtIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(45)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(rdbtnCliente, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(1)
							.addComponent(lblPuerto))
						.addComponent(txtPuerto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(35)
					.addComponent(btnIniciarpartida, GroupLayout.PREFERRED_SIZE, 23, Short.MAX_VALUE)
					.addGap(50))
		);
		panel.setLayout(gl_panel);
		
		panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 224));
		contentPane.add(panel_1);
		
		JButton btnNombre = new JButton("Aceptar");
		btnNombre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					c.setNombre(textField.getText());
					CardLayout card = (CardLayout) contentPane.getLayout();
					card.next(contentPane);
			}
		});
		
		JLabel label = new JLabel("Ingrese su nombre:");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(label, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(137)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
					.addGap(133))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(162)
					.addComponent(btnNombre, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
					.addGap(162))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(45)
					.addComponent(label, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
					.addGap(25)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(51)
					.addComponent(btnNombre, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(80))
		);
		panel_1.setLayout(gl_panel_1);
		
		panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 224));
		contentPane.add(panel_2);
		
		JLabel lblEsperando = new JLabel("Esperando a que se conecten otros jugadores...");
		lblEsperando.setHorizontalAlignment(SwingConstants.CENTER);
		lblEsperando.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JLabel lblNumjug = new JLabel("Numero de Jugadores:");
		lblNumjug.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JButton btnComenzar = new JButton("Comenzar partida");
		btnComenzar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnComenzar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.valueOf(lblCantjug.getText()) > 1)
					c.iniciarPartida();
				else
					JOptionPane.showMessageDialog(contentPane,"Se necesitan al menos 2 jugadores");
			}
		});
		
		lblCantjug = new JLabel("0");
		lblCantjug.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addComponent(lblEsperando, GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(89)
					.addComponent(lblNumjug, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblCantjug, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
					.addGap(194))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(158)
					.addComponent(btnComenzar, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
					.addGap(164))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(31)
					.addComponent(lblEsperando, GroupLayout.PREFERRED_SIZE, 19, Short.MAX_VALUE)
					.addGap(47)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNumjug, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
						.addComponent(lblCantjug, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
					.addGap(52)
					.addComponent(btnComenzar, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(75))
		);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(255, 255, 224));
		contentPane.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(new Color(255, 255, 224));
		tabbedPane.setPreferredSize(new Dimension(5, 90));
		panel_3.add(tabbedPane, BorderLayout.SOUTH);
		
		palo1 = new JPanel();
		palo1.setBackground(new Color(0, 128, 0));
		tabbedPane.addTab(nombrePalos[0], null, palo1, null);
		palo1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		palo2 = new JPanel();
		palo2.setBackground(new Color(0, 128, 0));
		tabbedPane.addTab(nombrePalos[1], null, palo2, null);
		palo2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		palo3 = new JPanel();
		palo3.setBackground(new Color(0, 128, 0));
		tabbedPane.addTab(nombrePalos[2], null, palo3, null);
		palo3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		palo4 = new JPanel();
		palo4.setBackground(new Color(0, 128, 0));
		tabbedPane.addTab(nombrePalos[3], null, palo4, null);
		palo4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		panel_9 = new JPanel();
		panel_9.setBackground(new Color(255, 255, 224));
		panel_3.add(panel_9, BorderLayout.CENTER);
		
		lblTurno = new JLabel("Turno de:");
		lblTurno.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		lblPozo = new JLabel("El pozo tiene:");
		lblPozo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		lblCartaactual = new JLabel("Carta Actual: ???");
		lblCartaactual.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		lblPaloactual = new JLabel("Palo actual:");
		lblPaloactual.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		btnDesconfiar = new JButton("Desconfiar");
		btnDesconfiar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDesconfiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDesconfiar.setEnabled(false);
				for (int i=0;i<botones.size();i++) {
					botones.get(i).setEnabled(false);
				}
				c.desconfiar();
			}
		});
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_9.createSequentialGroup()
							.addGap(134)
							.addComponent(lblPozo, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_9.createSequentialGroup()
							.addGap(134)
							.addComponent(lblCartaactual, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_9.createSequentialGroup()
							.addGap(151)
							.addComponent(btnDesconfiar, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_9.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblTurno, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_9.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPaloactual, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(100, Short.MAX_VALUE))
		);
		gl_panel_9.setVerticalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTurno)
					.addGap(7)
					.addComponent(lblPaloactual)
					.addGap(17)
					.addComponent(lblPozo, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addGap(19)
					.addComponent(lblCartaactual, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addGap(24)
					.addComponent(btnDesconfiar, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
		);
		panel_9.setLayout(gl_panel_9);
		
		panel_4 = new JPanel();
		panel_4.setBackground(new Color(255, 255, 224));
		panel_3.add(panel_4, BorderLayout.EAST);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		panel_5 = new JPanel();
		panel_5.setBackground(new Color(255, 255, 224));
		panel_4.add(panel_5, BorderLayout.NORTH);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		palo5 = new JPanel();
		palo5.setBackground(new Color(0, 128, 0));
		palo5.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_5.add(palo5);
		
		lblComodines = new JLabel("Comodines");
		lblComodines.setBackground(new Color(0, 128, 0));
		lblComodines.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_5.add(lblComodines, BorderLayout.NORTH);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBackground(new Color(255, 255, 224));
		contentPane.add(panel_10);
		
		JLabel lblFin = new JLabel("\u00A1Fin del Juego!");
		lblFin.setHorizontalAlignment(SwingConstants.CENTER);
		lblFin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JLabel lblGanador = new JLabel("El ganador es:");
		lblGanador.setHorizontalAlignment(SwingConstants.CENTER);
		lblGanador.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		lblNombreganador = new JLabel("NombreGanador");
		lblNombreganador.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombreganador.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNombreganador.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		JButton btnRestart = new JButton("Jugar de nuevo");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.reiniciarPartida();
			}
		});
		btnRestart.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addComponent(lblFin, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(lblGanador, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(lblNombreganador, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addGap(157)
					.addComponent(btnRestart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(160))
		);
		gl_panel_10.setVerticalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addGap(18)
					.addComponent(lblFin, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
					.addGap(21)
					.addComponent(lblGanador, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
					.addGap(21)
					.addComponent(lblNombreganador, GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE)
					.addGap(40)
					.addComponent(btnRestart)
					.addGap(55))
		);
		panel_10.setLayout(gl_panel_10);
		panelesPalos = new JPanel[]{palo1,palo2,palo3,palo4,palo5};
	}
		
	public void setCliente(Cliente cli) {
		this.c = cli;
	}

	public void setLblCantjug(String Cantjug) {
		lblCantjug.setText(Cantjug);
	}
	
	public void cambiarVentana() {
		CardLayout card = (CardLayout) contentPane.getLayout();
		card.next(contentPane);
	}
	
	public void setCartas(int [][] cartas) {
		if (botones.size() > 0)
			for (int i=0;i<botones.size();i++) {
				JPanel pan = (JPanel) botones.get(i).getParent();
				pan.remove(botones.get(i));
				pan.revalidate();
				pan.repaint();
			}
		botones = new ArrayList<JButton>();
		btnDesconfiar.setEnabled(false);
		for (int i=0;i<cartas.length;i++) {
			botones.add(new JButton(String.valueOf(cartas[i][0])));
			botones.get(i).addActionListener(this);
			botones.get(i).setName(String.valueOf(cartas[i][1])+String.valueOf(cartas[i][0]));
			botones.get(i).setEnabled(false);
			botones.get(i).setIcon(new ImageIcon(imagenes[cartas[i][1]-1][cartas[i][0]-1]));
			botones.get(i).setBorder(BorderFactory.createEmptyBorder());
			botones.get(i).setContentAreaFilled(false);
			botones.get(i).setText("");
			int indicePanel = cartas[i][1] - 1;
			panelesPalos[indicePanel].add(botones.get(i));
			panelesPalos[indicePanel].revalidate();
			panelesPalos[indicePanel].repaint();
		}
		cantidadPorPanel();
		c.preparado();
	}
	
	public void ventanaActiva() {
		for (int i=0;i<botones.size();i++)
			botones.get(i).setEnabled(true);
		lblTurno.setText("Mi turno");
	}
	
	public void setJugadorActual(String jugador) {
		lblTurno.setText("Turno de: "+jugador);
	}
	
	public void setPozo(String tamaño) {
		lblPozo.setText("El pozo tiene: "+tamaño+" cartas");
	}
	
	public void setPalo() {
		JPanel pregunta = new JPanel();
		JLabel lblPalo = new JLabel("¿Cual palo desea establecer?");
		pregunta.add(lblPalo);
		JButton[] botonesPalos = new JButton[nombrePalos.length-1];
		for (int i=0;i<nombrePalos.length-1;i++) {
			botonesPalos[i] = new JButton(nombrePalos[i],null);
			botonesPalos[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Window dialogo = SwingUtilities.getWindowAncestor((JButton)e.getSource());
					paloElegido = ((JButton)e.getSource()).getText();
					dialogo.dispose();
				}
			});
		}
		if (iPaloActual != -1)
				botonesPalos[iPaloActual].setEnabled(false);
		paloElegido = nombrePalos[0];
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JOptionPane.showOptionDialog(contentPane, pregunta, "Determinar Palo", 
				JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, botonesPalos , botonesPalos[0]);
		semaforo.release();
		int numPalo = 0;
		numPalo = Arrays.asList(nombrePalos).indexOf(paloElegido)+1;
		if (numPalo-1 == iPaloActual) {
			switch (iPaloActual) {
			case 0:
				numPalo = Arrays.asList(nombrePalos).indexOf(nombrePalos[1])+1;
				c.enviarpalo(String.valueOf(numPalo));
				break;
			default:
				numPalo = Arrays.asList(nombrePalos).indexOf(nombrePalos[0])+1;
				c.enviarpalo(String.valueOf(numPalo));
				break;
			}
		} else
			c.enviarpalo(String.valueOf(numPalo));
	}
	
	public void mostrarPalo(String p) {
		int indicePalo = Integer.valueOf(p) - 1;
		iPaloActual = indicePalo;
		String nomPalo = nombrePalos[indicePalo];
		lblPaloactual.setText("Palo actual: "+nomPalo);
		lblCartaactual.setText("Carta Actual: ???");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cartaElegida = ((JButton)e.getSource()).getName();
		c.descartar(cartaElegida);
		JPanel pan = (JPanel) ((JButton)e.getSource()).getParent();
		pan.remove(((JButton)e.getSource()));
		pan.validate();
		pan.repaint();
		botones.remove((JButton)e.getSource());
		cantidadPorPanel();
		for (int i=0;i<botones.size();i++) {
			botones.get(i).setEnabled(false);
		}
	}
	
	public void mostrarPozo(String carta) {
		String p = carta.substring(0, 1);
		String n = carta.substring(1);
		paloCartaActual = Integer.valueOf(p);
		numCartaActual = Integer.valueOf(n);
		int indicePalo = Integer.valueOf(p) - 1;
		p = nombrePalos[indicePalo];
		if (!p.equals(nombrePalos[4]))
			lblCartaactual.setText("Carta Actual: "+n+" de "+p);
		else
			lblCartaactual.setText("Carta Actual: Un "+p);
	}
	
	public void setBotonDesconfiar(String id) {
		if (id.equals(c.getId()))
			btnDesconfiar.setEnabled(false);
		else
			btnDesconfiar.setEnabled(true);
	}
	
	public void mostrarDesconfianza(String msg) {
		ImageIcon iconoCarta = new ImageIcon(imagenes[paloCartaActual-1][numCartaActual-1]);
		JLabel cartaActual = new JLabel(msg,iconoCarta,JLabel.CENTER);
		cartaActual.setVerticalTextPosition(JLabel.BOTTOM);
		cartaActual.setHorizontalTextPosition(JLabel.CENTER);
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(contentPane,cartaActual,"Resultado",JOptionPane.PLAIN_MESSAGE);
		semaforo.release();
	}
	
	public void finalDelJuego(String nombre) {
		cambiarVentana();
		lblNombreganador.setText(nombre);
	}
	
	public void noHayConexion() {
		JOptionPane.showMessageDialog(contentPane,"No se pudo conectar al servidor");
		System.exit(0);
	}
	
	public void desconexion() {
		JOptionPane.showMessageDialog(contentPane,"Se ha perdido la conexion con el servidor");
		System.exit(0);
	}
	
	private void cantidadPorPanel() {
		for (int i=0;i<4;i++) {
			tabbedPane.setTitleAt(i,nombrePalos[i]+" ("+((JPanel)tabbedPane.getComponentAt(i)).getComponentCount()+")");
		}
	}
	
	public void reiniciarPartida() {
		iPaloActual = -1;
		CardLayout card = (CardLayout) contentPane.getLayout();
		for (int i=0;i<2;i++)
			card.previous(contentPane);
	}
	
	public void desconectar(String n) {
		JOptionPane.showMessageDialog(contentPane,n+" abandono el juego, partida terminada");
		System.exit(0);
	}
	
	private void cargarImagenes() {
		int pal = 0;
		int num = 0;
		for (int i=0;i<CANTCARTAS;i++) {
			try {
				imagenes[pal][num] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(String.valueOf(pal+1)+String.valueOf(num+1)+".png"));
				num++;
				if (num == 12) {
					pal++;
					num = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
