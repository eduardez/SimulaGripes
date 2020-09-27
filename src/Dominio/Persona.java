package Dominio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Clase Persona. Contiene la informacion de cada celda (persona) y puede estar
 * en 4 estados diferentes:
 * +NO_AFECTADO: La persona no ha sido contagiada todavia.
 * +ENFERMO: La persona ha sido contagiada
 * +MUERTO: La persona ha sido contagiada y ha muerto
 * +INMUNE: La persona ha sido contagiada y se ha inmunizado
 * 
 * El resto de atributos de la clase Persona son:
 * +estado: Estado actual de la persona.
 * +estado_siguiente: Estado de la persona el siguiente dia.
 * +representacion: JPanel utilizado para mostrar a la persona en la interfaz.
 * +letra_estado: JLabel utilizado para mostrar en que estado se encuentra la persona.
 * +prob: Objeto Probabilidad que almacena los porcentajes de cada estado.
 * +posX, posY: Posicion de la persona dentro de la matriz.
 * +vecinos_fila: Lista de vecinos que estan en la misma fila o columna.
 * +vecinos_diag: Lista de vecinos que estan en diagonal.
 * 
 * @author Eduardo Garcia Aparicio
 *
 */
public class Persona {

	public enum Estado {
		NO_AFECTADO, 
		ENFERMO, 
		MUERTO, 
		INMUNE
	}
	
	Estado estado, siguiente_estado = Estado.NO_AFECTADO;
	JPanel representacion;
	JLabel letra_estado;
	Probabilidad prob;
	int posX, posY;
	ArrayList<Persona> vecinos_fila = new ArrayList<Persona>();
	ArrayList<Persona> vecinos_diag = new ArrayList<Persona>();

	/**
	 * 
	 * @param posX: Posicion X en la matriz de poblacion
	 * @param posY: Posicion Y en la matriz de poblacion
	 * @param prob: Objeto de probabilidades
	 */
	public Persona(int posX, int posY, Probabilidad prob) {
		this.prob = prob;
		this.estado = Estado.NO_AFECTADO;
		this.posX = posX;
		this.posY = posY;
		initRep();
	}

	/**
	 * Actualizar el estado de la persona
	 */
	public void siguienteDia() {
		if (this.estado != Estado.MUERTO && this.estado != Estado.INMUNE) {
			actualizarEstadoEnfermo();
			contagiarVecinos();
		}
	}

	public void actualizarEstado() {
		this.estado = this.siguiente_estado;
		if (this.estado == Estado.ENFERMO) {
			this.enfermar();
		} else if (this.estado == Estado.MUERTO) {
			this.morir();
		} else if (this.estado == Estado.INMUNE) {
			this.inmune();
		} else if (this.estado == Estado.NO_AFECTADO) {
			noAfectado();
		}
	}

	/**
	 * Enfermar a este objeto de clase persona, el cual sera el origen del foco.
	 */
	public void setPacienteCero() {
		this.estado = Estado.ENFERMO;
		this.siguiente_estado = Estado.ENFERMO;
	}

	/**
	 * Programar el siguiente estado de la persona.
	 * 
	 * @param estado
	 */
	public void setSiguienteEstado(Estado estado) {
		if (this.estado == Estado.NO_AFECTADO)
			this.siguiente_estado = estado;
	}

	/***
	 * Para actualizar al enfermo se usa el metodo de la ruleta, es decir, sumaremos
	 * las probabilidades de muerte, inmunizarse y seguir igual y calcularemos un
	 * numero aleatorio desde 0 hasta la suma total de esos valores.
	 * __________________________________________ 
	 * |Rango Muerte |Rango Inmune |Rango igual |
	 * __________________________________________ 
	 * 0									 sumatorio
	 */
	private void actualizarEstadoEnfermo() {
		double rango_muerte = this.prob.getMuerte();
		double rango_inmune = rango_muerte + this.prob.getInmune();
		double rango_igual = rango_inmune + this.prob.getIgual();

		if (this.estado == Estado.ENFERMO) {
			double aleatorio = Math.random() * rango_igual;
			if (aleatorio <= rango_muerte) {
				this.siguiente_estado = Estado.MUERTO;
			} else if (aleatorio > rango_muerte && aleatorio <= rango_inmune) {
				this.siguiente_estado = Estado.INMUNE;
			} else if (aleatorio > rango_inmune) {
				this.siguiente_estado = this.getEstado();
			}
		}
	}

	/**
	 * Se recorren las listas de vecino y por cada uno de ellos se calcula un numero 
	 * aleatorio. Si el numero esta en el rango [0, prob_contagio], el vecino se contagia.
	 * 
	 */
	private void contagiarVecinos() {
		if (this.estado == Estado.ENFERMO) {
			for (Persona prs : this.vecinos_diag) {
				double aleatorio = Math.random();
				if (aleatorio < this.prob.contagio_vecino_diag) {
					prs.setSiguienteEstado(Estado.ENFERMO);
				}
			}
			for (Persona prs : this.vecinos_fila) {
				double aleatorio = Math.random();
				if (aleatorio < this.prob.contagio_vecino_fila) {
					prs.setSiguienteEstado(Estado.ENFERMO);
				}
			}
		}
	}

	/**
	 * Inicializar los objetos que representa a la persona en la interfaz grafica.
	 */
	private void initRep() {
		this.representacion = new JPanel();
		this.representacion.setLayout(new BorderLayout());
		this.letra_estado = new JLabel();
		this.letra_estado.setHorizontalAlignment(SwingConstants.CENTER);
		this.letra_estado.setForeground(Color.black);
		this.representacion.add(this.letra_estado, BorderLayout.CENTER);
		noAfectado();
		this.representacion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				alternarEstadoClick();
			}
		});
		this.representacion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	// ---------------- Cambios de estado --------------------

	/**
	 * Cuando se hace click sobre una persona, esta pasa al siguiente estado.
	 */
	protected void alternarEstadoClick() {
		int indice = 0;
		Estado[] vect_estados = Estado.values();
		for (Estado est : vect_estados) {
			if (est == this.estado) {
				indice = (indice + 1) % 4;
				this.estado = vect_estados[indice];
				this.siguiente_estado = vect_estados[indice];
				break;
			}
			indice++;
		}
		actualizarEstado();
		this.representacion.getRootPane().repaint();
		this.representacion.getRootPane().revalidate();
	}

	/**
	 * Estado de la persona: NO_AFECTADO
	 * Color: Verde
	 * Posibles siguientes estados: [Enfermo]
	 */
	public void noAfectado() {
		this.representacion.setBackground(new Color(158, 255, 168)); // Sin contagiar
		this.letra_estado.setText("NA");
		this.letra_estado.setForeground(Color.black);
		this.representacion.setToolTipText("Persona no afectada");
	}

	/**
	 * Estado de la persona: ENFERMO
	 * Color: Rojo
	 * Posibles siguientes estados: [MUERTO, INMUNE]
	 */
	public void enfermar() {
		this.representacion.setBackground(new Color(255, 85, 79));
		this.letra_estado.setText("En");
		this.letra_estado.setForeground(Color.white);
		this.representacion.setToolTipText("Persona enferma");
	}

	/**
	 * Estado de la persona: MUERTO
	 * Color: Negro
	 * Posibles siguientes estados: []
	 */
	public void morir() {
		this.representacion.setBackground(new Color(38, 38, 38));
		this.letra_estado.setForeground(Color.white);
		this.letra_estado.setText("Mu");
		this.representacion.setToolTipText("Persona muerta");
	}

	/**
	 * Estado de la persona: INMUNE
	 * Color: Azul
	 * Posibles siguientes estados: []
	 */
	public void inmune() {
		this.representacion.setBackground(new Color(97, 150, 255));
		this.letra_estado.setText("In");
		this.letra_estado.setForeground(Color.black);
		this.representacion.setToolTipText("Persona inmune");
	}

	// ---------------- Getters and Setters ---------------------

	public JPanel getRepresentacion() {
		return representacion;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Estado getsiguienteEstado() {
		return this.siguiente_estado;
	}

	public ArrayList<Persona> getVecinos_fila() {
		return vecinos_fila;
	}

	public void setVecinos_fila(ArrayList<Persona> vecinos_fila) {
		this.vecinos_fila = vecinos_fila;
	}

	public ArrayList<Persona> getVecinos_diag() {
		return vecinos_diag;
	}

	public void setVecinos_diag(ArrayList<Persona> vecinos_diag) {
		this.vecinos_diag = vecinos_diag;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

}
