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
	// Lista que contiene las canciones de la playlist.
	List<Cancion> cancions= new ArrayList<>();

	// Nombre de la playlist.
	private String nombre;

	/**
	 * Obtiene el nombre de la playlist.
	 *
	 * @return el nombre de la playlist.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre de la playlist.
	 *
	 * @param nombre el nombre de la playlist.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Este método se encarga de ordenar la playlist de manera alfabética
	 * según el nombre de las canciones utilizando la clase Collections.
	 * El orden es insensible a mayúsculas y minúsculas.
	 *
	 * @autor Francisco Ceballos
	 */

	public void ordenarAlfabeticamente(){
		Collections.sort(cancions, new Comparator<Cancion>(){
			@Override
			public int compare(Cancion o1, Cancion o2) {
				return o1.getNombre().compareToIgnoreCase(o2.getNombre());
			}
		});
	}

	/**
	 * Este método selecciona una canción aleatoria de la playlist.
	 * Si la lista de canciones está vacía, retorna null.
	 *
	 * @return una canción aleatoria de la playlist, o null si la lista está vacía.
	 */
	public Cancion obtenerCancionAleatoria() {
		if (cancions.isEmpty()) {
			return null; // Si la lista está vacía, retorna null
		}
		Random random = new Random();
		int indiceAleatorio = random.nextInt(cancions.size());
		return cancions.get(indiceAleatorio);
	}


}