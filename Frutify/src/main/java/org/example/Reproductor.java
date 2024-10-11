package org.example;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Reproductor {
	private Player player;
	private Cancion cancion;
	private List<String> listaCancion;
	private int indice;
	private boolean isPaused;
	public void iniciarCancion() {
		// TODO - implement Reproductor.iniciarCancion
		if (cancion != null){
			if (indice < listaCancion.size()) {
				cancion(listaCancion.get(indice));
			}
		}
		throw new UnsupportedOperationException();
	}

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

	public void pausarCancion() {
		// TODO - implement Reproductor.pausarCancion
		if(cancion != null ){
			isPaused = true;
			//cancion.close();
		}
		throw new UnsupportedOperationException();
	}

	public void ReanudadCancion(){
		if (isPaused && indice< listaCancion.size()) {
			isPaused = false;
			cancion(listaCancion.get(indice));
		}
		throw new UnsupportedOperationException();
	}

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