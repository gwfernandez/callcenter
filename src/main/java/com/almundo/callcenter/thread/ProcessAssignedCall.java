package com.almundo.callcenter.thread;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import com.almundo.callcenter.dispatcher.Dispatcher;
import com.almundo.callcenter.model.Call;
import com.almundo.callcenter.model.CallStatus;
import com.almundo.callcenter.model.Empleado;

/**
 * @author gwfernandez
 * 
 *         Clase encargada de simular la atención de una llamada que fue
 *         asignada a un operador. <br/>
 *         La duracion de la llamada puede variar entre 5 y 10 segundos.
 */
public class ProcessAssignedCall extends Thread {

	// defino un log para los mensajes de la clase.
	private static Logger log = Logger.getLogger(ProcessAssignedCall.class);

	// este objeto contiene la informacion de la llamada.
	private Call call = null;
	// este objeto sera el operador que recibe la llamada.
	private Empleado operador = null;

	public ProcessAssignedCall(Call call, Empleado operador) {
		this.call = call;
		this.operador = operador;
	}

	@Override
	public void run() {
		try {
			// indico el inicio de la comunicación.
			this.call.setAsignada(new Date());
			// indico que operador atienda la llamada.
			this.call.setOperador(this.operador);
			// indico que la llamada fue asignada al operador y estan en curso
			this.call.setEstado(CallStatus.ASIGNADA);

			// simulo un tiempo de aleatorio de llamada
			int tiempoLlamadaSegundos = (new Random().nextInt(6) + 5);
			sleep(tiempoLlamadaSegundos * 1000);

			log.info(String.format("[Call-ID: %s] Llamada de %s atendida por %s en %s segundos",
					String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono(),
					call.getOperador().getLegajo(), tiempoLlamadaSegundos));

			// indico que la llamada fue atendia exitosamente.
			call.setEstado(CallStatus.ATENDIDA);
			// indico el momento de finalizacion de la llamada
			call.setFin(new Date());

			// incremento indicador de llamadas atendidas por el operador.
			call.getOperador().incrementarCantidadLlamadasAtendidas();

		} catch (Exception e) {
			// indico el momento de finalizacion de llamada
			call.setFin(new Date());
			// cambio el estado a "llamada finalizco con error"
			call.setEstado(CallStatus.FINALIZADA_CON_ERROR);
			log.error(String.format(
					"[Call-ID: %s] Se produjo un error en el procesamiento de la llamada. Cliente: %s, Operador asignado: %s ",
					String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono(),
					call.getOperador().getLegajo()));
		} finally {
			// indico que el operador esta disponible para atender una nueva llamada.
			Dispatcher.getInstance().agregarOperador(operador);
		}
	}
}
