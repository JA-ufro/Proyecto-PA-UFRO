package org.example;

/**
 * La clase {@code Usuario} representa un usuario con un nombre y una contraseña.
 * Permite almacenar y recuperar el nombre y la contraseña, así como verificar
 * la autenticidad de la contraseña.
 *
 * <p> Esta clase puede ser utilizada para gestionar la autenticación de usuarios en sistemas donde se requiera
 * almacenar y verificar información básica del usuario. </p>
 *
 * @author JA-Ufro
 * @version 1.0
 */
public class Usuario {

	private String nombre;
	private String contrasena;

	/**
	 * Constructor que inicializa un nuevo objeto {@code Usuario} con un nombre y una contraseña.
	 *
	 * @param nombre El nombre del usuario.
	 * @param contrasena La contraseña del usuario.
	 */
	public Usuario(String nombre, String contrasena) {
		this.nombre = nombre;
		this.contrasena = contrasena;
	}


	/**
	 * Devuelve el nombre del usuario.
	 *
	 * @return El nombre del usuario.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve la contraseña del usuario.
	 *
	 * @return La contraseña del usuario.
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * Verifica si la contraseña proporcionada coincide con la contraseña del usuario.
	 *
	 * @param contrasena La contraseña a verificar.
	 * @return {@code true} si la contraseña es correcta, {@code false} en caso contrario.
	 */
	public boolean verificarContrasena(String contrasena) {
		return this.contrasena.equals(contrasena);
	}

}