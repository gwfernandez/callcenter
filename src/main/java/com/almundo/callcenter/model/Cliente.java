package com.almundo.callcenter.model;

/**
 * 
 * @author gwfernandez
 *
 *         Clase encargada de modelar al cliente que realizo la llamada
 *         telefonica
 *
 */
public class Cliente {
	// numero de telefono con el que se efectua la llamada
	private String numeroTelefono;

	public Cliente() {
		super();
	}

	public Cliente(String numeroTelefono) {
		super();
		this.numeroTelefono = numeroTelefono;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroTelefono == null) ? 0 : numeroTelefono.hashCode());
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
		Cliente other = (Cliente) obj;
		if (numeroTelefono == null) {
			if (other.numeroTelefono != null)
				return false;
		} else if (!numeroTelefono.equals(other.numeroTelefono))
			return false;
		return true;
	}

}