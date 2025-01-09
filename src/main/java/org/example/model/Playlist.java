package org.example.model;

import jakarta.persistence.*;

import java.util.*;

/**
 * La clase Playlist representa una lista de canciones que puede ser manipulada mediante diversas operaciones.
 * Permite agregar, ordenar y seleccionar canciones de manera aleatoria.
 *
 * @autor Francisco Ceballos
 */

@Entity
public class Playlist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	@ManyToMany
	@JoinTable(
			name = "cancion_has_playlist", // Nombre de la tabla intermedia
			joinColumns = @JoinColumn(name = "playlist_id"), // Columna de la playlist
			inverseJoinColumns = @JoinColumn(name = "cancion_id") // Columna de la canción
	)
	private List<Cancion> canciones;

	public Playlist(String nombre, Usuario usuario) {
		this.nombre = nombre;
		this.canciones = new ArrayList<>();
	}

	public Playlist() {

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void ordenarAlfabeticamente() {
		if (canciones == null || canciones.isEmpty()) {
			System.out.println("La playlist está vacía o no inicializada.");
			return;
		}
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
