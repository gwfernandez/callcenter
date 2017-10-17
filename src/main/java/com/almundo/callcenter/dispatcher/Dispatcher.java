package com.almundo.callcenter.dispatcher;

import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.almundo.callcenter.exception.CallCenterException;
import com.almundo.callcenter.model.Call;
import com.almundo.callcenter.model.CallStatus;
import com.almundo.callcenter.model.Empleado;
import com.almundo.callcenter.thread.ProcessAssignedCall;

/**
 * @author gwfernandez
 * 
 *         Clase Singleton encargada de gestionar la derivacion de las llamadas
 *         entrantes en el callcenter. <br/>
 *         La derivacion de una llamada tiene en cuenta que: en primera
 *         instancia debe ser atendida por un operador, si no hay ninguno libre
 *         debe ser atendida por un supervisor, y de no haber tampoco
 *         supervisores libres debe ser atendida por un director.
 */
public class Dispatcher {
	// defino la cantidad maxima de llamas en espera a soportar.
	private static final int MAXIMO_LLAMADAS_EN_ESPERA = 5;

	// defino un log para los mensajes de la clase.
	private static Logger log = Logger.getLogger(Dispatcher.class);

	// defino la unica instancia de la clase
	private static Dispatcher instance = null;

	// defino la cola con prioridad para los operadores del callcenter.
	private PriorityQueue<Empleado> operadoresQueue = null;
	// defino la cola para las llamadas en espera.
	private Queue<Call> llamadasEnEsperaQueue = null;

	// defino el numero de orden que tendran las operadores dentro de cola de
	// prioridad
	private long numeroOrden = 0;

	private Dispatcher() {
		this.operadoresQueue = new PriorityQueue<Empleado>();
		this.llamadasEnEsperaQueue = new LinkedList<Call>();
	}

	/**
	 * metodo encargado de generar la unica instancia de la clase.
	 * 
	 * @return {@link Dispatcher}
	 */
	public static synchronized Dispatcher getInstance() {
		if (instance == null) {
			instance = new Dispatcher();
		}
		return instance;
	}

	/**
	 * Metodo que deriva la llamada entrante a un operador disponible. <br>
	 * Si no hay operador disponible, la llamada queda en espera hasta que un
	 * operador pueda tomarla.
	 * 
	 * @param call
	 * @throws CallCenterException
	 */
	public synchronized void dispatchCall(Call call) throws CallCenterException {
		try {
			// verifico existan operadores para antender o no existan llamadas en espera.
			if (!this.operadoresQueue.isEmpty()) {
				log.info(String.format("[Call-ID: %s] Derivando llamada entrante %s",
						String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono()));

				// derivo la llamada con el primer operador disponible
				dispatchCall(call, this.operadoresQueue.remove());
			} else {
				log.warn(String.format(
						"[Call-ID: %s] No hay operadores disponibles para atender la llamada del cliente %s. La llamada sera intentara dejarla en espera.",
						String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono()));
				// si no se supera la cantidad maxima de llamadas en espera. Dejo en espera a la
				// llamada actual. Caso contrario, descarto la llamada.
//				if (this.llamadasEnEsperaQueue.size() <= MAXIMO_LLAMADAS_EN_ESPERA) {
					// cambio el estado de la llamada a "llamada en espera".
					call.setEstado(CallStatus.EN_ESPERA);
					this.llamadasEnEsperaQueue.add(call);
//				} else {
//					// cambio el estado a "llamada rechazada"
//					call.setEstado(CallStatus.RECHAZADA);
//					// indico el momento de finalizacion de llamada
//					call.setFin(new Date());
//					log.warn(String.format(
//							"[Call-ID: %s] Se supero la capacidad maxima de llamadas en espera. Se descarta la llamada %s",
//							String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono()));
//				}

				log.info(String.format("Hay %s llamadas en espera", this.llamadasEnEsperaQueue.size()));
			}
		} catch (Exception e) {
			// indico el momento de finalizacion de llamada
			call.setFin(new Date());
			// cambio el estado a "llamada finalizco con error"
			call.setEstado(CallStatus.FINALIZADA_CON_ERROR);
			throw new CallCenterException(String.format("[Call-ID: %s] Error al derivar la llamada del cliente ",
					String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono()));
		}
	}

	private synchronized void dispatchCall(Call call, Empleado operador) throws CallCenterException {
		// asigno la llamada al operador y simula la atencion.
		ProcessAssignedCall assignedCall = new ProcessAssignedCall(call, operador);
		assignedCall.start();
	}

	/**
	 * Metodo que se encarga de agregar el Operador a la QueuePriority para que
	 * pueda atender llamadas. <br>
	 * Adicionalmente si existen llamas en la cola de espera se asigna la primer
	 * llamada en espera para su atencion.
	 * 
	 * @param operador
	 */
	public synchronized void agregarOperador(Empleado operador) {
		try {
			// indico un orden de disponibilidad en la queue priority para el operador
			operador.setOrden(++this.numeroOrden);

			// validar si existen llamadas en espera.
			if (!this.llamadasEnEsperaQueue.isEmpty()) {

				Call call = this.llamadasEnEsperaQueue.remove();
				log.info(String.format("[Call-ID: %s] Derivando llamada en espera %s",
						String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono()));

				// asigno la primera llamada en espera al operador
				dispatchCall(call, operador);

				log.info(String.format("Quedan %s llamadas en espera ", this.llamadasEnEsperaQueue.size()));

			} else {
				// agrego al empleado a la queue de operadores disponibles.
				this.operadoresQueue.add(operador);
				log.debug(String.format("Empleado %s listo para recibir llamadas [prioridad: %s, orden: %s]",
						operador.getLegajo(), operador.getPrioridadAtencion(), operador.getOrden()));
			}
		} catch (CallCenterException e) {
			log.error(String.format("Error al agregar el operador %s a la cola de atencion!", operador.getLegajo()));
		}
	}
}
