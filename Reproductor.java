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
	private Cancion cancion;
	private List<String> listaCancion;
	private int indice;
	private boolean isPaused;

	/**
	 * Inicia la reproducción de la canción actual en la lista de reproducción.
	 * Si no hay ninguna canción para reproducir, este método no hace nada.
	 */
	public void iniciarCancion() {
		// TODO - implement Reproductor.iniciarCancion
		if (cancion != null){
			if (indice < listaCancion.size()) {
				cancion(listaCancion.get(indice));
			}
		}
		throw new UnsupportedOperationException();
	}



	/**
	 * Salta a la siguiente canción en la lista de reproducción.
	 * Si la canción actual es la última, este método no hace nada.
	 */
	public void saltarCancion() {
		// TODO - implement Reproductor.saltarCancion
		if(cancion != null){
			if (indice < listaCancion.size() - 1) {
				indice++;
				cancion(listaCancion.get(indice));
			}
		}
		throw new UnsupportedOperationException();
	}
	/**
	 * Reproduce la canción especificada desde la ruta de archivo dada.
	 * Este método se ejecuta en un hilo separado para permitir la reproducción asíncrona.
	 *
	 * @param s la ruta del archivo de la canción que se va a reproducir
	 */
	private void cancion(String s) {
		try {
			FileInputStream fileInputStream = new FileInputStream(s);
			//cancion = new cancion(fileInputStream);
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
	 * Pausa la canción que se está reproduciendo actualmente.
	 * Si no hay ninguna canción reproduciéndose, este método no hace nada.
	 */
	public void pausarCancion() {
		// TODO - implement Reproductor.pausarCancion
		if(cancion != null ){
			isPaused = true;
			//cancion.close();
		}
		throw new UnsupportedOperationException();
	}
	/**
	 * Reanuda la reproducción de la canción que está pausada actualmente.
	 * Si no hay ninguna canción en pausa o el índice está fuera de límites, este método no hace nada.
	 */
	public void ReanudadCancion(){
		if (isPaused && indice< listaCancion.size()) {
			isPaused = false;
			cancion(listaCancion.get(indice));
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Reproduce la canción anterior en la lista de reproducción.
	 * Si la canción actual es la primera, este método no hace nada.
	 */
	public void cancionAnterior() {
		// TODO - implement Reproductor.cancionAnterior
		if (cancion != null){
			if (indice > 0) {
				indice--;
				cancion(listaCancion.get(indice));
			}
		}
		throw new UnsupportedOperationException();
	}

}