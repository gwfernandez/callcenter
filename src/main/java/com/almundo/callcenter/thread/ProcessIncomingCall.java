package com.almundo.callcenter.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import com.almundo.callcenter.dispatcher.Dispatcher;
import com.almundo.callcenter.model.Call;
import com.almundo.callcenter.model.CallStatus;
import com.almundo.callcenter.model.Cliente;

/**
 * @author gwfernandez
 * 
 *         Clase encargada de recibir la llamada entrante e invocar al
 *         Dispatcher para asignarle un operador.
 */
public class ProcessIncomingCall extends Thread {

	// defino un log para los mensajes de la clase.
	private static Logger log = Logger.getLogger(ProcessIncomingCall.class);
	// numero de identificacion para la llamada entrante.
	private long callID;

	// defino un constructor con el numero de identificacion de la llamada.
	public ProcessIncomingCall(int callID) {
		this.callID = callID;
	}

	@Override
	public void run() {
		// creo la objeto que representa la llamada en curso, indicando el numero de
		// identificación para la llamada y la fecha/hora de ingreso.
		Call call = new Call(callID, new Date());
		try {
			// asigno los datos del cliente a la llamada
			call.setCliente(getCliente());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
			log.info(String.format("[Call-ID: %s] Llamada entrante del cliente %s recibida %s ",
					String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono(),
					simpleDateFormat.format(call.getInicio())));

			// derivo la llamada a un operador disponible
			Dispatcher.getInstance().dispatchCall(call);

		} catch (Exception e) {
			// indico el momento de finalizacion de llamada
			call.setFin(new Date());
			// cambio el estado a "llamada finalizco con error"
			call.setEstado(CallStatus.FINALIZADA_CON_ERROR);
			log.error(String.format(
					"[Call-ID: %s] Se produjo un error en el procesamiento de la llamada entrante. Cliente: %s",
					String.format("%04d", call.getId()), call.getCliente().getNumeroTelefono()));
		}
	}

	/**
	 * Metodo que recupero todos los datos de cliente que esta efectuando la
	 * llamada.
	 * 
	 * @return {@link Cliente}
	 */
	private Cliente getCliente() {
		// genero aleatoriamente un numero de telefono.
		Random randomGenerator = new Random();
		String numeroTelefono = "01115" + String.format("%08d", (randomGenerator.nextInt(100000000) + 1));
		Cliente cliente = new Cliente();
		cliente.setNumeroTelefono(numeroTelefono);
		return cliente;
	}
}