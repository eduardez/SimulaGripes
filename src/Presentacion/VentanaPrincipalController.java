package Presentacion;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Dominio.Persona;
import Dominio.Probabilidad;
import Principal.Simulacion;


/**
 * Clase controladora de la clase VentanaPrincipal. Contiene toda la logica 
 * relativa a la interfaz, pero no hace cambios sobre las clases
 * principales del programa (Persona y Simulacion), asi que no es demasiado importante
 * en ese aspecto.
 * 
 * @author Eduardo Garcia Aparicio
 *
 */
public class VentanaPrincipalController extends VentanaPrincipal {
	private boolean existeSimulacion = false;
	private Simulacion simulacion;
	private Thread hilo_simulacion;
	private Probabilidad probs;

	/**
	 * Inicializar la ventana. Se le pasa un objeto de la clase Probabilidad para
	 * luego instanciar correctamente un objeto de la clase Simulacion.
	 * 
	 * @param probabilidades
	 */
	public VentanaPrincipalController(Probabilidad probabilidades) {
		super();
		this.initialize();
		this.probs = probabilidades;
		setAcciones();
		setDatos();
	}

	public void mostrarVentana() {
		this.frame.setTitle("Simulador de Gripes");
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}

	// ------------------- Acciones y Datos -------------------
	/**
	 * Añadir acciones a los botones de la ventana.
	 */
	private void setAcciones() {
		this.btnEmpezar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!existeSimulacion) {
					empezarSimulacion();
				} else {
					reiniciar();
				}
			}
		});
		this.btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleSimulacion();
			}
		});
		this.btn_menosvel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disminuirVelocidad();
			}
		});
		this.btn_masvel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aumentarVelocidad();
			}
		});
		addListener(this.txt_contagiodiag);
		addListener(this.txt_contagiofc);
		addListener(this.txt_igual);
		addListener(this.txt_inmune);
		addListener(this.txt_morir);
	}

	/**
	 * Colocar los datos del objeto de la clase Probabilidad en sus respectivos
	 * lugares de la interfaz, para luego poder modificarlos en tiempo de ejecucion.
	 */
	private void setDatos() {
		this.txt_morir.setText(String.valueOf(this.probs.getMuerte() * 100));
		this.txt_inmune.setText(String.valueOf(this.probs.getInmune() * 100));
		this.txt_igual.setText(String.valueOf(this.probs.getIgual() * 100));
		this.txt_contagiodiag.setText(String.valueOf(this.probs.getcontagio_vecino_diag() * 100));
		this.txt_contagiofc.setText(String.valueOf(this.probs.getContagio_vecino_fila() * 100));
	}

	/**
	 * Listeners de los objetos donde estan los porcentajes. Usado para modificar
	 * los porcentajes y para controlar que no se introducen caracteres no permitidos.
	 * @param txt_field
	 */
	private void addListener(JTextField txt_field) {
		KeyListener listener = (new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				String value = txt_field.getText();
				if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getKeyCode() == KeyEvent.VK_BACK_SPACE
						|| (ke.getKeyChar() == '.' && !value.contains("."))) {
					txt_field.setEditable(true);

				} else {
					txt_field.setEditable(false);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				actualizarPorcentajes();
			}
		});
		txt_field.addKeyListener(listener);
	}

	/**
	 * Modificar los porcentajes contenidos en el objeto de clase Probabilidad
	 */
	protected void actualizarPorcentajes() {
		try {
			this.probs.setContagio_vecino_fila(Double.valueOf(this.txt_contagiofc.getText()));
			this.probs.setcontagio_vecino_diag(Double.valueOf(this.txt_contagiodiag.getText()));
			this.probs.setIgual(Double.valueOf(this.txt_igual.getText()));
			this.probs.setInmune(Double.valueOf(this.txt_inmune.getText()));
			this.probs.setMuerte(Double.valueOf(this.txt_morir.getText()));
		} catch (NumberFormatException e) {
			System.out.println("Error en los porcentajes.");
		}
	}

	// ---------------- Acciones de los botones ---------------------
	/**
	 * Cambiar el estado de la simulacion (En pausa, Ejecutandose).
	 */
	private void toggleSimulacion() {
		if (!this.simulacion.togglePararSimulacion()) {
			this.btnParar.setText("Pausar");
		} else {
			this.btnParar.setText("Reanudar");
		}
	}

	/**
	 * Crear un nuevo hilo para ejecutar la simulacion.
	 */
	private void empezarSimulacion() {
		this.btnEmpezar.setText("Reiniciar");
		this.simulacion = new Simulacion(this, this.probs, Integer.valueOf(this.txt_numeroFilas.getText()),
				(int) this.spinner_focos.getValue());
		this.hilo_simulacion = new Thread(this.simulacion);
		this.hilo_simulacion.start();
		existeSimulacion = true;
		btn_masvel.setEnabled(true);
		btn_menosvel.setEnabled(true);
	}

	/**
	 * Cerrar la ventana actual e instanciar una nueva con las probabilidades por defecto.
	 */
	private void reiniciar() {
		VentanaPrincipalController control = new VentanaPrincipalController(new Probabilidad());
		control.mostrarVentana();
		this.frame.dispose();
	}

	/**
	 * Disminuir la velocidad de la simulacion.
	 */
	protected void disminuirVelocidad() {
		this.simulacion.disminuirVel();
	}
	/**
	 * Aumentar la velocidad de la simulacion.
	 */
	protected void aumentarVelocidad() {
		this.simulacion.aumentarVel();
	}

	// ---------------------- Actualizaciones -----------
	/**
	 * Cambiar el layout del panel para luego añadir todas las representaciones
	 * de las persona.
	 * @param filas
	 */
	public void actualizarPaneLayout(int filas) {
		GridBagLayout layout = new GridBagLayout();
		double[] pesos = new double[filas];
		Arrays.fill(pesos, 1.0);
		layout.columnWidths = new int[filas];
		layout.rowHeights = new int[filas];
		layout.columnWeights = pesos;
		layout.rowWeights = pesos;
		this.panel_poblacion.setLayout(layout);
	}

	/**
	 * Recorrer la matriz que contiene a todas las personas y añadirlas al panel.
	 * @param poblacion
	 */
	public void addPersonasToPanel(Persona[][] poblacion) {
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < poblacion[i].length; j++) {
				GridBagConstraints posicion = new GridBagConstraints();
				posicion.fill = GridBagConstraints.BOTH;
				posicion.gridx = i;
				posicion.gridy = j;
				this.panel_poblacion.add(poblacion[i][j].getRepresentacion(), posicion);
			}
		}
	}
	
	/**
	 * Actualizar tabla que contiene los porcentajes y el numero de personas en 
	 * cada uno de los estados disponibles.
	 * @param muertos
	 * @param na
	 * @param inmunes
	 * @param enfermos
	 * @param total
	 */
	public void actualizarTabla(int muertos, int na, int inmunes, int enfermos, int total) {
		double porc_enfermos = porcentaje(enfermos, total);
		double porc_na = porcentaje(na, total);
		double porc_inmunes = porcentaje(inmunes, total);
		double porc_muertos = porcentaje(muertos, total);
		DefaultTableModel model = (DefaultTableModel) this.tabla_estadisticas.getModel();
		this.tabla_estadisticas.setRowHeight(45);
		model.setValueAt(na, 0, 0);
		model.setValueAt(enfermos, 1, 0);
		model.setValueAt(inmunes, 2, 0);
		model.setValueAt(muertos, 3, 0);
		model.setValueAt(String.format("%.2f", porc_na), 0, 1);
		model.setValueAt(String.format("%.2f", porc_enfermos), 1, 1);
		model.setValueAt(String.format("%.2f", porc_inmunes), 2, 1);
		model.setValueAt(String.format("%.2f", porc_muertos), 3, 1);
		this.tabla_estadisticas.setModel(model);
	}

	/**
	 * Calcular el porcentaje de un numero sobre el total.
	 * @param numero
	 * @param total
	 * @return
	 */
	public double porcentaje(int numero, int total) {
		return ((double) numero / (double) total) * (double) 100;
	}

	/**
	 * Actualizar el label de dias.
	 * @param numero
	 */
	public void sumarDia(int numero) {
		this.lblTiempotranscurrido.setText("  "+ numero + " dias  ");
	}



}
