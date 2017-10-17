package com.almundo.callcenter.simulation;

import org.apache.log4j.Logger;

import com.almundo.callcenter.exception.CallCenterException;
import com.almundo.callcenter.model.Director;
import com.almundo.callcenter.model.Operador;
import com.almundo.callcenter.model.Supervisor;
import com.almundo.callcenter.thread.ProcessIncomingCall;

/**
 * @author gwfernandez
 * 
 *         clase encargada de simular las llamadas concurrentes y ver validar el
 *         comportamiento del callcenter
 */
public class CallCenterSimulator {

	// defino un log para los mensajes de la clase.
	private static Logger log = Logger.getLogger(CallCenterSimulator.class);
	// defino la cantidad de llamadas a realizar en la sumulacion.
	private static int CANTIDAD_LLAMADAS_SIMULACION = 10;

	public static void main(String[] args) {
		try {
			// inicializo los operadores disponibles para atender llamadas en el callcenter.
			CallCenterLoader.getInstance().inicializar();

			// simulo el ingreso de N llamadas concurrentes.
			for (int indice = 1; indice <= CANTIDAD_LLAMADAS_SIMULACION; indice++) {
				// disparo un hilo para atender a la llamada entrante
				ProcessIncomingCall incomingCall = new ProcessIncomingCall(indice);
				incomingCall.start();
			}
		} catch (CallCenterException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("Error al simular la atencion de llamadas!", e);
		}

	}

	/**
	 * Metodo que se encarga se generar el reporte de estadisticas de atencion del
	 * call center.
	 */
	protected static void generarEstadisticasAtencion() {
		log.info("Estadisticas OPERADORES");
		for (Operador empleado : CallCenterLoader.getInstance().getOperadores()) {
			log.info(String.format("Operador: %s, llamadas atendidas: %s", empleado.getLegajo(),
					empleado.getCantidadLlamadasAtendidas()));
		}

		log.info("Estadisticas SUPERVISORES");
		for (Supervisor empleado : CallCenterLoader.getInstance().getSupervisores()) {
			log.info(String.format("Supervisor: %s, llamadas atendidas: %s", empleado.getLegajo(),
					empleado.getCantidadLlamadasAtendidas()));
		}

		log.info("Estadisticas DIRECTORES");
		for (Director empleado : CallCenterLoader.getInstance().getDirectores()) {
			log.info(String.format("Director: %s, llamadas atendidas: %s", empleado.getLegajo(),
					empleado.getCantidadLlamadasAtendidas()));
		}
	}
}