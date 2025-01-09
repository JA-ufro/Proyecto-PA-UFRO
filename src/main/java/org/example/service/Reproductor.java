package org.example.service;

import javax.swing.*;
import java.io.*;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.util.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Reproductor {

	public List<File> playlist = new ArrayList<>();
	private AdvancedPlayer player;
	private Thread playThread;
	public boolean isPaused = false;
	private int pausaFrame = 0; // Posición de pausa en frames
	public File currentSong; // Canción actual
	private final Object lock = new Object(); // Bloqueo para pausa/reanudar
	private Timer progressTimer; // Temporizador para actualizar la barra de progreso
	private JSlider progressSlider;
	private JLabel timeRemainingLabel;

	public Reproductor() {
		initializeUI();
	}

	private void initializeUI() {
		if (java.awt.GraphicsEnvironment.isHeadless()) {
			System.out.println("Entorno sin interfaz gráfica detectado. No se puede inicializar la GUI.");
			return;
		}
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

	private void actualizarProgreso(int duration) {
		if (!isPaused && progressSlider.getValue() < duration) {
			int currentValue = progressSlider.getValue() + 1;
			progressSlider.setValue(currentValue);

			int remainingTime = duration - currentValue;
			timeRemainingLabel.setText(formatearTiempo(remainingTime));
		}
	}

	private String formatearTiempo(int segundos) {
		int minutos = segundos / 60;
		int segRestantes = segundos % 60;
		return String.format("%02d:%02d", minutos, segRestantes);
	}

	private int calcularDuracionEnSegundos(File file) {
		// Implementar lógica para calcular duración (puede necesitar una biblioteca externa)
		return 240; // Duración fija como ejemplo
	}

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

	public void eliminarCancion() {
		if (playlist.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La playlist está vacía.");
			return;
		}

		File removedSong = playlist.remove(0);
		JOptionPane.showMessageDialog(null, "Canción eliminada: " + removedSong.getName());
	}
}
