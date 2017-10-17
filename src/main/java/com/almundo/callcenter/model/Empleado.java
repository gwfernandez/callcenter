package com.almundo.callcenter.model;

/**
 * @author gwfernandez
 *
 *         Clase encargada se modelar a un empleado del callcenter
 * 
 */
public class Empleado implements Comparable<Empleado> {

	// numero de legajo del empleado
	private String legajo;
	// nombre y apellido del empleado
	private String nombre;
	// prioridad de atencion asignada al empleado de acuerdo a su rol en la empresa.
	private PrioridadAtencion prioridadAtencion;

	// numero de orden de disponiblidad que se le asigna al empleado el cola de
	// atencion.
	private long orden;
	// cantidad de llamadas atendidas en la ultima jornada
	private int cantidadLlamadasAntendidas;

	public Empleado() {
		super();
		this.cantidadLlamadasAntendidas = 0;
	}

	public Empleado(String legajo) {
		this.legajo = legajo;
	}

	public String getLegajo() {
		return legajo;
	}

	public void setLegajo(String legajo) {
		this.legajo = legajo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public PrioridadAtencion getPrioridadAtencion() {
		return prioridadAtencion;
	}

	public void setPrioridadAtencion(PrioridadAtencion prioridadAtencion) {
		this.prioridadAtencion = prioridadAtencion;
	}

	public void incrementarCantidadLlamadasAtendidas() {
		this.cantidadLlamadasAntendidas++;
	}

	public int getCantidadLlamadasAtendidas() {
		return cantidadLlamadasAntendidas;
	}

	public long getOrden() {
		return orden;
	}

	public void setOrden(long orden) {
		this.orden = orden;
	}

	@Override
	public int compareTo(Empleado o) {
		if (o != null && this.prioridadAtencion != null && o.getPrioridadAtencion() != null) {
			if (this.prioridadAtencion.ordinal() < o.getPrioridadAtencion().ordinal()) {
				return -1;
			} else if (this.prioridadAtencion.ordinal() > o.getPrioridadAtencion().ordinal()) {
				return 1;
			} else {
				if (this.orden < o.getOrden()) {
					return 1;
				} else if (this.orden > o.getOrden()) {
					return -1;
				} else {
					return 0;
				}
			}
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((legajo == null) ? 0 : legajo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empleado other = (Empleado) obj;
		if (legajo == null) {
			if (other.legajo != null)
				return false;
		} else if (!legajo.equals(other.legajo))
			return false;
		return true;
	}
}