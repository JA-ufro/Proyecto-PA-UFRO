package org.example;
import com.mpatric.mp3agic.*;

import java.io.File;
import java.text.SimpleDateFormat;
/**
 * La clase {@code Cancion} representa una canción con sus metadatos extraídos
 * de un archivo MP3. Utiliza la biblioteca mp3agic para leer etiquetas ID3v2.
 *
 * <p>
 * Los atributos de la canción incluyen el nombre, autor, álbum y fecha de
 * lanzamiento, que se obtienen de las etiquetas ID3v2 del archivo MP3.
 * </p>
 *
 * <p>
 * Para leer los metadatos, es necesario proporcionar un archivo MP3 válido al
 * constructor de la clase {@code Cancion}.
 * </p>
 *
 * @see com.mpatric.mp3agic.Mp3File
 * @see com.mpatric.mp3agic.ID3v2
 */
public class Cancion {

	/** Nombre de la canción. */
	private String nombre;

	/** Autor o artista de la canción. */
	private String autor;

	/** Álbum al que pertenece la canción. */
	private String album;

	/** Fecha de lanzamiento de la canción. */
	private String fechaLanzamiento;

	/**
	 * Crea una instancia de {@code Cancion} a partir de un archivo MP3, extrayendo
	 * los metadatos de la etiqueta ID3v2.
	 *
	 * @param archivo El archivo MP3 del que se extraerán los metadatos.
	 * @throws Exception Si ocurre algún error al procesar el archivo MP3 o al leer
	 *                   las etiquetas ID3v2.
	 * @see com.mpatric.mp3agic.Mp3File
	 * @see com.mpatric.mp3agic.ID3v2
	 */
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

	/**
	 * Obtiene el nombre de la canción.
	 *
	 * @return El nombre de la canción.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Obtiene el autor o artista de la canción.
	 *
	 * @return El autor de la canción.
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * Obtiene el álbum al que pertenece la canción.
	 *
	 * @return El álbum de la canción.
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * Obtiene la fecha de lanzamiento de la canción.
	 *
	 * @return La fecha de lanzamiento de la canción.
	 */
	public String getFechaLanzamiento() {
		return fechaLanzamiento;
	}
}
