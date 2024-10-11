package org.example;
import com.mpatric.mp3agic.*;

import java.io.File;
import java.text.SimpleDateFormat;

public class Cancion {
	private String nombre;
	private String autor;
	private String album;
	private String fechaLanzamiento;

	public Cancion(File archivo) throws Exception {
		Mp3File mp3file = new Mp3File(archivo);

		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			this.nombre = id3v2Tag.getTitle();
			this.autor = id3v2Tag.getArtist();
			this.album = id3v2Tag.getAlbum();
			this.fechaLanzamiento = id3v2Tag.getYear();
		}
	}

	// Getters para obtener los atributos
	public String getNombre() {
		return nombre;
	}

	public String getAutor() {
		return autor;
	}

	public String getAlbum() {
		return album;
	}

	public String getFechaLanzamiento() {
		return fechaLanzamiento;
	}
}