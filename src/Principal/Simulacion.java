package Principal;

import java.util.ArrayList;

import Dominio.Persona;
import Dominio.Probabilidad;
import Presentacion.VentanaPrincipalController;

/**
 * Esta clase contiene todo lo relativo a lo que es la simulacion del avance del
 * virus, es decir, el objeto de la clase Probabilidad, la matriz que contiene
 * la poblacion, cada cuanto avanza el virus y la velocidad de la simulacion.
 * 
 * @author Eduardo Garcia Aparicio
 *
 */
public class Simulacion implements Runnable {
	private Persona[][] poblacion;
	private VentanaPrincipalController controlador;
	private Probabilidad probs;
	private boolean parar_tiempo = false;
	private double divisor_tiempo = 1.0;
	private int num_dia = 0, total_inmunes, total_enfermos, total_muertos, total_na, total_poblacion;
	private int numero_filas, num_focos;

	/**
	 * Constructor de la simulacion.
	 * 
	 * @param controlador:  Controlador de la interfaz.
	 * @param probs:        Probabilidades de cada estado.
	 * @param numero_filas: Numero de personas por fila de la matriz.
	 * @param num_focos:    Cantidad de personas enfermas el dia 0.
	 */
	public Simulacion(VentanaPrincipalController controlador, Probabilidad probs, int numero_filas, int num_focos) {
		this.controlador = controlador;
		this.probs = probs;
		this.numero_filas = numero_filas;
		this.num_focos = num_focos;
	}

	@Override
	public void run() {
		iniciarSimulacion();
	}

	/**
	 * Inicializa la matriz de poblacion, asi como el layout de la interfaz y añade
	 * sus representaciones. Ademas, inicia los focos de infeccion asi como el bucle
	 * de ejecucion.
	 */
	public void iniciarSimulacion() {
		this.total_poblacion = this.numero_filas * this.numero_filas;
		this.controlador.actualizarPaneLayout(this.numero_filas);
		this.poblacion = generarPersonas(this.numero_filas);
		this.controlador.addPersonasToPanel(this.poblacion);
		for (int i = 0; i < this.num_focos; i++) {
			Persona paciente_cero = getPacienteCero(this.numero_filas);
			paciente_cero.setPacienteCero();
		}
		setVecinos();
		empezarTiempo();
	}

	/**
	 * Inicio del bucle de la simulacion. Cada 0.8s se suma un dia, se actualiza el
	 * estado de la poblacion y la tabla de porcentajes.
	 */
	public void empezarTiempo() {
		while (true) {
			try {
				if (!this.parar_tiempo) {
					this.num_dia += 1;
					actualizarPoblacion();
					this.controlador.actualizarTabla(this.total_muertos, this.total_na, this.total_inmunes,
							this.total_enfermos, this.total_poblacion);
					if (this.total_enfermos != 0)
						this.controlador.sumarDia(this.num_dia);
				}
				long tiempo = (long) (800 / this.divisor_tiempo);
				Thread.sleep(tiempo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Actualiza los datos de la tabla que contiene la informacion relativa al
	 * estado actual de la poblacion.
	 */
	private void actualizarPoblacion() {
		this.total_enfermos = 0;
		this.total_inmunes = 0;
		this.total_muertos = 0;
		this.total_na = 0;
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < poblacion[i].length; j++) {
				poblacion[i][j].siguienteDia();
				poblacion[i][j].actualizarEstado();
				actualizarNumeros(poblacion[i][j].getsiguienteEstado());
			}
		}
	}

	/**
	 * Actualiza los contadores en base al estado de la persona.
	 * 
	 * @param estado
	 */
	private void actualizarNumeros(Persona.Estado estado) {
		switch (estado) {
		case ENFERMO:
			this.total_enfermos += 1;
			break;
		case INMUNE:
			this.total_inmunes += 1;
			break;
		case NO_AFECTADO:
			this.total_na += 1;
			break;
		case MUERTO:
			this.total_muertos += 1;
			break;
		default:
			break;
		}
	}

	/**
	 * Devuelve una persona al azar de la matriz
	 */
	private Persona getPacienteCero(int max) {
		int posX = (int) Math.floor(Math.random() * (max));
		int posY = (int) Math.floor(Math.random() * (max));
		return this.poblacion[posX][posY];
	}

	/**
	 * Llama al metodo {@link #setVecinos(Persona)} para cada uno de los elementos
	 * de la matriz poblacion para calcular sus vecinos.
	 */
	private void setVecinos() {
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < poblacion[i].length; j++) {
				setVecinos(poblacion[i][j]);
			}
		}
	}

	/**
	 * Calcula cuales son los vecinos de una persona dada dentro de la matriz de
	 * poblacion y los clasifica segun esten posicionados (en la misma fila/columna
	 * que la persona o en diagonal). Una vez calculados, se modifican los atributos
	 * de esa persona para asignarle dichos vecinos.
	 * 
	 * @param persona
	 */
	private void setVecinos(Persona persona) {
		ArrayList<Persona> vecinos_fila = new ArrayList<Persona>();
		ArrayList<Persona> vecinos_diag = new ArrayList<Persona>();
		int xPersona = persona.getPosX(), yPersona = persona.getPosY();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				int posX = xPersona + x;
				int posY = yPersona + y;
				if (dentroMatriz(posX, posY)) { // Comprobar que esten dentro de la matriz las coordenadas
					if (vecinoFila(posX, posY, xPersona, yPersona) && (posX != xPersona || posY != yPersona)) {
						// Comprobar si es diagonal o en fila y comprobar que no sea el mismo
						vecinos_fila.add(this.poblacion[posX][posY]);
					} else {
						vecinos_diag.add(this.poblacion[posX][posY]);
					}
				}
			}
		}
		persona.setVecinos_diag(vecinos_diag);
		persona.setVecinos_fila(vecinos_fila);
	}

	/**
	 * Comprueba si un posible vecino esta en la misma fila o columna que la persona
	 * a la que se le estan asignando los vecinos.
	 * 
	 * @param x:        posicion en x consultada
	 * @param y:        posicion en y consultada
	 * @param xPersona: posicion x de la persona de la que se buscan los vecinos
	 * @param yPersona: posicion x de la persona de la que se buscan los vecinos
	 * @return
	 */
	private boolean vecinoFila(int x, int y, int xPersona, int yPersona) {
		return (x == xPersona) || (y == yPersona);
	}

	/**
	 * Comprobar que unas coordenadas dadas estan dentro de la matriz.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean dentroMatriz(int x, int y) {
		boolean dentroX = (x > -1) && (x < this.poblacion.length);
		boolean dentroY = (y > -1) && (y < this.poblacion.length);
		return dentroX && dentroY;
	}

	/**
	 * Genera una matriz de NxN, donde N = Filas introducidas.
	 * 
	 * @param filas
	 * @return
	 */
	private Persona[][] generarPersonas(int filas) {
		Persona[][] poblacion = new Persona[filas][filas];
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < poblacion[i].length; j++) {
				poblacion[i][j] = new Persona(i, j, this.probs);
			}
		}
		return poblacion;
	}

	/**
	 * Aumentar velocidad de la simulacion.
	 */
	public void aumentarVel() {
		this.divisor_tiempo += 0.4;
	}

	/**
	 * Disminuir velocidad de la simulacion.
	 */
	public void disminuirVel() {
		if (this.divisor_tiempo > 0.4)
			this.divisor_tiempo -= 0.4;
	}

	/**
	 * Alterna el estado de la simulacion (Parada o en curso).
	 * 
	 * @return
	 */
	public boolean togglePararSimulacion() {
		this.parar_tiempo = !this.parar_tiempo;
		return this.parar_tiempo;
	}
}
