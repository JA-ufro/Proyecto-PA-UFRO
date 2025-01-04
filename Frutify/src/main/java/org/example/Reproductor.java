package org.example;

import javax.swing.*;
import java.io.*;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.util.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Clase Reproductor que implementa un reproductor MP3 con funcionalidades de manejo de playlist, reproducción,
 * pausa, eliminación de canciones, y control de progreso en la reproducción.
 * Utiliza la biblioteca {@code javazoom.jl.player.advanced.AdvancedPlayer} para la reproducción de archivos MP3.
 *
 * @author EstebanCancino
 */
public class Reproductor {

	/**
	 * Lista que contiene los archivos MP3 en la playlist.
	 */
	private List<File> playlist = new ArrayList<>();

	/**
	 * Objeto para la reproducción de archivos MP3.
	 */
	private AdvancedPlayer player;

	/**
	 * Hilo dedicado a la reproducción de la canción actual.
	 */
	private Thread playThread;

	/**
	 * Bandera para indicar si la reproducción está pausada.
	 */
	private boolean isPaused = false;

	/**
	 * Posición en frames en la que se pausó la reproducción.
	 */
	private int pausaFrame = 0;

	/**
	 * Archivo MP3 que se está reproduciendo actualmente.
	 */
	private File currentSong;

	/**
	 * Objeto para sincronizar el control de pausa y reanudación.
	 */
	private final Object lock = new Object();

	/**
	 * Temporizador para actualizar la barra de progreso durante la reproducción.
	 */
	private Timer progressTimer;

	/**
	 * Barra deslizante que muestra el progreso de la canción.
	 */
	private JSlider progressSlider;

	/**
	 * Etiqueta que muestra el tiempo restante de la canción actual.
	 */
	private JLabel timeRemainingLabel;

	/**
	 * Constructor de la clase Reproductor. Inicializa la interfaz de usuario.
	 */
	public Reproductor() {
		initializeUI();
	}

	/**
	 * Inicializa la interfaz gráfica de usuario para el reproductor MP3.
	 */
	private void initializeUI() {
		JFrame frame = new JFrame("Reproductor MP3");
		frame.setSize(600, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton openButton = new JButton("Agregar a Playlist");
		openButton.addActionListener(e -> agregarArchivoAPlaylist());

		JButton showPlaylistButton = new JButton("Mostrar Playlist");
		showPlaylistButton.addActionListener(e -> mostrarPlaylist());

		JButton playButton = new JButton("Reproducir Playlist");
		playButton.addActionListener(e -> reproducirPlaylist());

		JButton pauseButton = new JButton("Pausar/Reanudar");
		pauseButton.addActionListener(e -> pausarReanudar());

		JButton removeButton = new JButton("Eliminar Canción");
		removeButton.addActionListener(e -> eliminarCancion());

		progressSlider = new JSlider(0, 100, 0);
		progressSlider.setEnabled(false); // Deshabilitado inicialmente
		progressSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Manejar interacción del usuario con el slider aquí, si es necesario
			}
		});

		timeRemainingLabel = new JLabel("00:00");

		JPanel panel = new JPanel();
		panel.add(openButton);
		panel.add(showPlaylistButton);
		panel.add(playButton);
		panel.add(pauseButton);
		panel.add(removeButton);
		panel.add(progressSlider);
		panel.add(timeRemainingLabel);

		frame.add(panel);
		frame.setVisible(true);
	}

	/**
	 * Agrega un archivo MP3 seleccionado por el usuario a la playlist.
	 */
	public void agregarArchivoAPlaylist() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".mp3");
			}

			@Override
			public String getDescription() {
				return "Archivos de audio MP3 (*.mp3)";
			}
		});

		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			playlist.add(selectedFile);
			JOptionPane.showMessageDialog(null, "Archivo agregado: " + selectedFile.getName());
		}
	}

	/**
	 * Muestra los archivos MP3 en la playlist en un cuadro de diálogo.
	 */
	public void mostrarPlaylist() {
		StringBuilder sb = new StringBuilder("Playlist:\n");
		if (playlist.isEmpty()) {
			sb.append("La playlist está vacía.");
		} else {
			for (File file : playlist) {
				sb.append(file.getName()).append("\n");
			}
		}
		JOptionPane.showMessageDialog(null, sb.toString());
	}

	/**
	 * Reproduce la playlist, comenzando desde la primera canción de la lista.
	 */
	public void reproducirPlaylist() {
		if (playlist.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La playlist está vacía.");
			return;
		}

		currentSong = playlist.get(0); // Reproducir primera canción
		pausaFrame = 0; // Reiniciar el contador de pausa

		playThread = new Thread(() -> reproducirCancion(currentSong));
		playThread.start();
	}

	/**
	 * Reproduce una canción específica.
	 *
	 * @param cancion Archivo MP3 a reproducir.
	 */
	private void reproducirCancion(File cancion) {
		try (FileInputStream fileInputStream = new FileInputStream(cancion)) {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			player = new AdvancedPlayer(bufferedInputStream);

			int duration = calcularDuracionEnSegundos(cancion); // Duración de la canción
			progressSlider.setMaximum(duration);
			progressSlider.setValue(0);

			progressTimer = new Timer(1000, e -> actualizarProgreso(duration));
			progressTimer.start();

			player.play(pausaFrame, Integer.MAX_VALUE); // Comenzar desde pausaFrame
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al reproducir el archivo.");
		}
	}

	/**
	 * Actualiza la barra de progreso y el tiempo restante durante la reproducción.
	 *
	 * @param duration Duración total de la canción en segundos.
	 */
	private void actualizarProgreso(int duration) {
		if (!isPaused && progressSlider.getValue() < duration) {
			int currentValue = progressSlider.getValue() + 1;
			progressSlider.setValue(currentValue);

			int remainingTime = duration - currentValue;
			timeRemainingLabel.setText(formatearTiempo(remainingTime));
		}
	}

	/**
	 * Formatea el tiempo en segundos a un formato mm:ss.
	 *
	 * @param segundos Tiempo en segundos.
	 * @return Tiempo formateado como cadena en el formato mm:ss.
	 */
	private String formatearTiempo(int segundos) {
		int minutos = segundos / 60;
		int segRestantes = segundos % 60;
		return String.format("%02d:%02d", minutos, segRestantes);
	}

	/**
	 * Calcula la duración de un archivo MP3 en segundos.
	 *
	 * @param file Archivo MP3.
	 * @return Duración en segundos (se utiliza un valor fijo como ejemplo).
	 */
	private int calcularDuracionEnSegundos(File file) {
		return 240; // Duración fija como ejemplo
	}

	/**
	 * Pausa o reanuda la reproducción actual según el estado.
	 */
	public void pausarReanudar() {
		synchronized (lock) {
			if (isPaused) {
				isPaused = false;
				lock.notifyAll();
				progressTimer.start(); // Reanudar el temporizador
			} else {
				isPaused = true;
				progressTimer.stop(); // Pausar el temporizador
			}
		}
	}

	/**
	 * Elimina la primera canción de la playlist.
	 */
	public void eliminarCancion() {
		if (playlist.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La playlist está vacía.");
			return;
		}

		File removedSong = playlist.remove(0);
		JOptionPane.showMessageDialog(null, "Canción eliminada: " + removedSong.getName());
	}
}
