package org.example;

import java.util.*;

public class Usuario {

	private String nombre;
	private String contrasena;

	public Usuario(String nombre, String contrasena) {
		this.nombre = nombre;
		this.contrasena = contrasena;
	}

	public String getNombre() {
		return nombre;
	}

	public String getContrasena() {
		return contrasena;
	}
	public boolean verificarContrasena(String contrasena) {
		return this.contrasena.equals(contrasena);
	}

}