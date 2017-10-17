package com.almundo.callcenter.model;

import java.util.Date;

/**
 * @author gwfernandez
 * 
 *         Clase encargada de modelar una llamada efectuada por un cliente.
 */
public class Call {

	// numero de identificacion para la llamada-
	private Long id;
	// cliente que realiza la llamada.
	private Cliente cliente;
	// fecha y hora de ingreso la llamada al callcenter
	private Date inicio;
	// fecha y hora de inicio de atencion de la llamada
	private Date asignada;
	// fecha y hora de finalizacion de la llamada
	private Date fin;
	// operador que atendio la llamada
	private Empleado operador;
	// estado de la callada.
	private CallStatus estado;

	public Call(Long id, Date inicio) {
		this.id = id;
		this.inicio = inicio;
		// el estado inicial de una llamada es "llamada entrante".
		this.estado = CallStatus.ENTRANTE;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getAsignada() {
		return asignada;
	}

	public void setAsignada(Date asignada) {
		this.asignada = asignada;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public Empleado getOperador() {
		return operador;
	}

	public void setOperador(Empleado operador) {
		this.operador = operador;
	}

	public CallStatus getEstado() {
		return estado;
	}

	public void setEstado(CallStatus estado) {
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Call other = (Call) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}