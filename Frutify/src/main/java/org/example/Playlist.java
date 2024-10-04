package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Playlist {

	List<Cancion> cancions= new ArrayList<>();
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void ordenarAlfabeticamente(){
		Collections.sort(cancions, new Comparator<Cancion>(){
			@Override
			public int compare(Cancion o1, Cancion o2) {
				return o1.getNombre().compareToIgnoreCase(o2.getNombre());
			}
		});
	}
	public Cancion obtenerCancionAleatoria() {
		if (cancions.isEmpty()) {
			return null; // Si la lista está vacía, retorna null
		}
		Random random = new Random();
		int indiceAleatorio = random.nextInt(cancions.size());
		return cancions.get(indiceAleatorio);
	}
	

}