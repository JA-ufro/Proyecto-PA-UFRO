package org.example.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistManagerGUI {
    private JFrame frame;
    private JList<String> playlistView;
    private DefaultListModel<String> playlistModel;
    private Reproductor reproductor;

    public PlaylistManagerGUI(Reproductor reproductor) {
        this.reproductor = reproductor;
        initialize();
    }

    private void initialize() {
        // Crear ventana principal
        frame = new JFrame("Gestión de Playlist");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear modelo y vista para la playlist
        playlistModel = new DefaultListModel<>();
        playlistView = new JList<>(playlistModel);

        // Crear botones
        JButton addButton = new JButton("Agregar Canción");
        addButton.addActionListener(e -> agregarCancion());

        JButton playButton = new JButton("Reproducir Playlist");
        playButton.addActionListener(e -> reproducirPlaylist());

        JButton sortButton = new JButton("Ordenar Playlist");
        sortButton.addActionListener(e -> ordenarPlaylist());

        JButton showButton = new JButton("Mostrar Playlist");
        showButton.addActionListener(e -> mostrarPlaylist());

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(playButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(showButton);

        // Panel principal
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(playlistView), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Mostrar ventana
        frame.setVisible(true);
    }

    private void agregarCancion() {
        String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre de la canción:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            playlistModel.addElement(nombre);
            JOptionPane.showMessageDialog(frame, "Canción agregada: " + nombre);
        }
    }

    private void reproducirPlaylist() {
        if (playlistModel.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "La playlist está vacía.");
            return;
        }
        JOptionPane.showMessageDialog(frame, "Reproduciendo la primera canción...");
        // Lógica para reproducir desde el reproductor
        reproductor.reproducirPlaylist();
    }

    private void ordenarPlaylist() {
        if (playlistModel.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "La playlist está vacía.");
            return;
        }

        // Convertir el modelo en una lista y ordenarlo
        List<String> canciones = new ArrayList<>();
        for (int i = 0; i < playlistModel.size(); i++) {
            canciones.add(playlistModel.getElementAt(i));
        }
        canciones.sort(String::compareToIgnoreCase);

        // Limpiar el modelo y agregar los elementos ordenados
        playlistModel.clear();
        for (String cancion : canciones) {
            playlistModel.addElement(cancion);
        }

        JOptionPane.showMessageDialog(frame, "Playlist ordenada alfabéticamente.");
    }


    private void mostrarPlaylist() {
        if (playlistModel.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "La playlist está vacía.");
            return;
        }
        StringBuilder sb = new StringBuilder("Canciones en la playlist:\n");
        for (int i = 0; i < playlistModel.size(); i++) {
            sb.append(i + 1).append(". ").append(playlistModel.get(i)).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString());
    }

    public static void main(String[] args) {
        Reproductor reproductor = new Reproductor();
        new PlaylistManagerGUI(reproductor);
    }
}
