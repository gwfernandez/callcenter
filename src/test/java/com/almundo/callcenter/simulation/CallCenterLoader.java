package com.almundo.callcenter.simulation;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.almundo.callcenter.dispatcher.Dispatcher;
import com.almundo.callcenter.exception.CallCenterException;
import com.almundo.callcenter.model.Director;
import com.almundo.callcenter.model.Operador;
import com.almundo.callcenter.model.PrioridadAtencion;
import com.almundo.callcenter.model.Supervisor;

public class CallCenterLoader {

	// defino la cantidad de operadores del callcenter
	private static final int CANTIDAD_OPERADORES = 4;
	// defino la cantidad de supervisores del callcenter
	private static final int CANTIDAD_SUPERVISORES = 2;
	// defino la cantidad de directores del callcenter
	private static final int CANTIDAD_DIRECTORES = 1;

	private final static Logger log = Logger.getLogger(CallCenterLoader.class);

	private static CallCenterLoader instance = null;

	private Set<Operador> operadores = null;
	private Set<Supervisor> supervisores = null;
	private Set<Director> directores = null;

	private CallCenterLoader() {
	}

	public static synchronized CallCenterLoader getInstance() {
		if (instance == null) {
			instance = new CallCenterLoader();
		}
		return instance;
	}

	public Set<Operador> getOperadores() {
		return operadores;
	}

	public Set<Supervisor> getSupervisores() {
		return supervisores;
	}

	public Set<Director> getDirectores() {
		return directores;
	}

	public void inicializar() throws CallCenterException {

		try {
			log.info("Inicializando la carga de empleados");

			directores = new HashSet<Director>(CANTIDAD_DIRECTORES);
			Director director = null;
			for (int indice = 1; indice <= CANTIDAD_DIRECTORES; indice++) {
				director = new Director("DI-" + String.format("%04d", indice));
				director.setPrioridadAtencion(PrioridadAtencion.BAJA);
				directores.add(director);

				Dispatcher.getInstance().agregarOperador(director);
			}

			supervisores = new HashSet<Supervisor>(CANTIDAD_SUPERVISORES);
			Supervisor supervisor = null;
			for (int indice = 1; indice <= CANTIDAD_SUPERVISORES; indice++) {
				supervisor = new Supervisor("SU-" + String.format("%04d", indice));
				supervisor.setPrioridadAtencion(PrioridadAtencion.MEDIA);
				supervisores.add(supervisor);
				Dispatcher.getInstance().agregarOperador(supervisor);
			}

			operadores = new HashSet<Operador>(CANTIDAD_OPERADORES);
			Operador operador = null;
			for (int indice = 1; indice <= CANTIDAD_OPERADORES; indice++) {
				operador = new Operador("OP-" + String.format("%04d", indice));
				operador.setPrioridadAtencion(PrioridadAtencion.ALTA);
				operadores.add(operador);
				Dispatcher.getInstance().agregarOperador(operador);
			}

		} catch (Exception e) {
			log.error("Error al inicializar la carga de empleados!", e);
			throw new CallCenterException("Error al inicializar la carga de empleados!");
		} finally {
			log.info("finalizo la carga de empleados");
		}
	}
}
