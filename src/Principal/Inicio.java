package Principal;

import Dominio.Probabilidad;
import Presentacion.VentanaPrincipalController;

/**
 * Clase principal. Se usa unicamente para construir el objeto probabilidades e
 * instanciar la ventana de la simulacion.
 * 
 * @author Eduardo Garcia Aparicio
 *
 */
public class Inicio {

	public static void main(String[] args) {
		// Llamando al contructor vacio de probabilidades, obtenemos un objeto con las
		// probabilidades que vienen en el enunciado (por defecto)
		Probabilidad probabilidades = new Probabilidad();
		VentanaPrincipalController control = new VentanaPrincipalController(probabilidades);
		control.mostrarVentana();
	}

}
