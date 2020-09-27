package Dominio;

/**
 * Clase Probabilidad que contiene todos los porcentajes de cada estado
 * de una persona, asi como las probabilidades de contagio.
 * 
 * @author Eduardo Garcia Aparicio
 *
 */
public class Probabilidad {
	double muerte;
	double inmune;
	double igual;
	
	double contagio_vecino_fila;
	double contagio_vecino_diag;

	/**
	 * Constructor vacio con las probabilidades por defecto.
	 */
	public Probabilidad() {
		this.muerte = 0.2;
		this.inmune = 0.3;
		this.igual = 0.5;
		this.contagio_vecino_fila = 0.8;
		this.contagio_vecino_diag = 0.3;
	}
	
	/**
	 * Constructor para introducir otros porcentajes que no son los que estan 
	 * por defecto.
	 * @param muerte
	 * @param inmune
	 * @param igual
	 * @param contagio_vecino_fila
	 * @param contagio_vecino_diag
	 */
	public Probabilidad(double muerte, double inmune, double igual, double contagio_vecino_fila,
			double contagio_vecino_diag) {
		this.muerte = muerte;
		this.inmune = inmune;
		this.igual = igual;
		this.contagio_vecino_fila = contagio_vecino_fila;
		this.contagio_vecino_diag = contagio_vecino_diag;
	}

	public double getMuerte() {
		return muerte;
	}

	public void setMuerte(double muerte) {
		this.muerte = muerte;
	}

	public double getInmune() {
		return inmune;
	}

	public void setInmune(double inmune) {
		this.inmune = inmune;
	}

	public double getIgual() {
		return igual;
	}

	public void setIgual(double igual) {
		this.igual = igual;
	}

	public double getContagio_vecino_fila() {
		return contagio_vecino_fila;
	}

	public void setContagio_vecino_fila(double contagio_vecino_fila) {
		this.contagio_vecino_fila = contagio_vecino_fila;
	}

	public double getcontagio_vecino_diag() {
		return contagio_vecino_diag;
	}

	public void setcontagio_vecino_diag(double contagio_vecino_diag) {
		this.contagio_vecino_diag = contagio_vecino_diag;
	}
	
	
}
