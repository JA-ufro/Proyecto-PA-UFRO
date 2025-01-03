package org.example;

import java.util.*;
import java.util.stream.Collectors;
/**
 * La clase Playlist representa una lista de canciones que puede ser manipulada mediante diversas operaciones.
 * Permite agregar, ordenar y seleccionar canciones de manera aleatoria.
 *
 * @autor Francisco Ceballos
 */


public class Playlist {
	private List<Cancion> canciones = new ArrayList<>();
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void ordenarAlfabeticamente() {
		canciones.sort(Comparator.comparing(Cancion::getNombre, String::compareToIgnoreCase));
	}

	public Cancion obtenerCancionAleatoria() {
		if (canciones.isEmpty()) return null;
		Collections.shuffle(canciones);
		return canciones.get(0);
	}

	public void agregarCancion(Cancion cancion) {
		canciones.add(cancion);
	}
	/**
	 * Obtiene la lista de canciones de la playlist.
	 *
	 * @return La lista de canciones.
	 */
	public List<Cancion> getCancions() {
		return canciones;
	}

}
