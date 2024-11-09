package org.example;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * La clase {@code Reproductor} representa un reproductor de música simple que puede
 * reproducir, pausar y saltar canciones de una lista de reproducción.
 * <p>
 * Este reproductor utiliza la biblioteca JLayer para manejar la reproducción de MP3.
 * Mantiene una lista de canciones y realiza un seguimiento de la canción actual que se está reproduciendo.
 * </p>
 */
public class Reproductor {
	private Player player;
	public Cancion cancion;
	public List<String> listaCancion;
	public int indice;
	private boolean isPaused;

	/**
	 * Inicia la reproducción de la canción actual en la lista de reproducción.
	 * Si no hay ninguna canción para reproducir, este método no hace nada.
	 */
	public void iniciarCancion() {
		if (cancion != null && indice < listaCancion.size()) {
			reproducirCancionActual();
		} else {
			throw new UnsupportedOperationException("No hay canciones para reproducir o el índice está fuera de límites.");
		}
	}
	/**
	 * Método privado para reproducir la canción actual desde la lista de reproducción.
	 */
	private void reproducirCancionActual() {
		String rutaCancion = listaCancion.get(indice);
		try {
			FileInputStream fileInputStream = new FileInputStream(rutaCancion);
			new Thread(() -> {
				try {
					player.play();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Salta a la siguiente canción en la lista de reproducción.
	 * Si la canción actual es la última, este método no hace nada.
	 */
	public void saltarCancion() {
		if (cancion != null && indice < listaCancion.size() - 1) {
			indice++;
			reproducirCancionActual();
		} else {
			throw new UnsupportedOperationException("No se puede saltar la canción.");
		}
	}

	/**
	 * Pausa la canción que se está reproduciendo actualmente.
	 * Si no hay ninguna canción reproduciéndose, este método no hace nada.
	 */
	public void pausarCancion() {
		if (cancion != null) {
			isPaused = true;
			//cancion.close();
		} else {
			throw new UnsupportedOperationException("No hay canción en reproducción para pausar.");
		}
	}
	/**
	 * Reanuda la reproducción de la canción que está pausada actualmente.
	 * Si no hay ninguna canción en pausa o el índice está fuera de límites, este método no hace nada.
	 */
	public void ReanudadCancion(){
		if (isPaused && indice < listaCancion.size()) {
			isPaused = false;
			reproducirCancionActual();
		} else {
			throw new UnsupportedOperationException("No hay canción en pausa o el índice está fuera de límites.");
		}
	}

	/**
	 * Reproduce la canción anterior en la lista de reproducción.
	 * Si la canción actual es la primera, este método no hace nada.
	 */
	public void cancionAnterior() {
		if (cancion != null && indice > 0) {
			indice--;
			reproducirCancionActual();
		} else {
			throw new UnsupportedOperationException("No se puede retroceder a la canción anterior.");
		}
	}

}
