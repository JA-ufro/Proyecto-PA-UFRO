package org.example.service;

import org.example.model.Cancion;
import org.example.model.Playlist;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para manejar la reproducción de canciones y playlists.
 * Esta clase reemplaza la funcionalidad de Swing por una lógica enfocada en Spring Boot.
 */
@Service
public class Reproductor {

	/**
	 * Reproduce una playlist completa.
	 *
	 * @param playlist La playlist a reproducir.
	 * @return Un mensaje indicando el resultado de la operación.
	 */
	public String reproducirPlaylist(Playlist playlist) {
		if (playlist == null || playlist.getCanciones() == null || playlist.getCanciones().isEmpty()) {
			return "La playlist está vacía o no existe.";
		}

		StringBuilder resultado = new StringBuilder("Reproduciendo playlist: " + playlist.getNombre() + "\n");
		for (Cancion cancion : playlist.getCanciones()) {
			resultado.append("Reproduciendo: ").append(cancion.getNombre()).append("\n");
		}

		return resultado.toString();
	}

	/**
	 * Reproduce una canción específica de una playlist.
	 *
	 * @param cancion La canción a reproducir.
	 * @return Un mensaje indicando el resultado de la operación.
	 */
	public String reproducirCancion(Cancion cancion) {
		if (cancion == null) {
			return "La canción no existe.";
		}

		return "Reproduciendo: " + cancion.getNombre();
	}

	/**
	 * Ordena las canciones de una playlist alfabéticamente por nombre.
	 *
	 * @param playlist La playlist a ordenar.
	 * @return Un mensaje indicando el resultado de la operación.
	 */
	public String ordenarPlaylist(Playlist playlist) {
		if (playlist == null || playlist.getCanciones() == null || playlist.getCanciones().isEmpty()) {
			return "La playlist está vacía o no existe.";
		}

		playlist.getCanciones().sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
		return "Playlist ordenada alfabéticamente.";
	}

	/**
	 * Obtiene una canción aleatoria de una playlist.
	 *
	 * @param playlist La playlist de donde se extraerá la canción.
	 * @return La canción seleccionada aleatoriamente o un mensaje indicando un error.
	 */
	public Cancion obtenerCancionAleatoria(Playlist playlist) {
		if (playlist == null || playlist.getCanciones() == null || playlist.getCanciones().isEmpty()) {
			return null;
		}

		List<Cancion> canciones = playlist.getCanciones();
		int randomIndex = (int) (Math.random() * canciones.size());
		return canciones.get(randomIndex);
	}
}
