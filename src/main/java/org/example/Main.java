package org.example;

import org.example.model.Cancion;
import org.example.model.Playlist;
import org.example.model.Usuario;
import org.example.service.DatabaseService;
import org.example.service.Reproductor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * Clase principal para ejecutar el programa que utiliza las clases Database, Playlist, Usuario y Reproductor.
 * Permite realizar operaciones como iniciar sesión, crear playlists, agregar canciones y reproducirlas.
 *
 * @author JA-Ufro
 */
@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DatabaseService database = new DatabaseService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Conectando a la base de datos...");
        database.getConnection();

        Usuario usuarioActual = null;
        while (usuarioActual == null) {
            System.out.print("Ingrese su nombre de usuario: ");
            String nombreUsuario = scanner.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String contrasena = scanner.nextLine();

            usuarioActual = database.iniciarSesion(nombreUsuario, contrasena);
            if (usuarioActual == null) {
                System.out.println("Usuario o contraseña incorrectos. Intente nuevamente.");
            }
        }

        System.out.println("Bienvenido, " + usuarioActual.getNombre() + "!");

        Playlist playlist = new Playlist();
        Reproductor fileChooser = new Reproductor();

        boolean salir = false;
        while (!salir) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Crear nueva playlist");
            System.out.println("2. Ordenar playlist alfabéticamente");
            System.out.println("3. Agregar canciones a la playlist desde archivos");
            System.out.println("4. Mostrar playlist");
            System.out.println("5. Reproducir playlist");
            System.out.println("6. Obtener canción aleatoria");
            System.out.println("7. Salir");

            System.out.print("Opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1 -> {
                    System.out.print("Ingrese el nombre de la nueva playlist: ");
                    String nombrePlaylist = scanner.nextLine();
                    database.crearPlaylist(nombrePlaylist, usuarioActual);
                    playlist.setNombre(nombrePlaylist);
                }
                case 2 -> {
                    playlist.ordenarAlfabeticamente();
                    System.out.println("Playlist ordenada alfabéticamente.");
                }
                case 3 -> fileChooser.agregarArchivoAPlaylist();
                case 4 -> fileChooser.mostrarPlaylist();
                case 5 -> fileChooser.reproducirPlaylist();
                case 6 -> {
                    Cancion cancionAleatoria = playlist.obtenerCancionAleatoria();
                    if (cancionAleatoria != null) {
                        System.out.println("Canción aleatoria: " + cancionAleatoria.getNombre());
                    } else {
                        System.out.println("La playlist está vacía.");
                    }
                }
                case 7 -> {
                    salir = true;
                    System.out.println("Saliendo del programa. Hasta luego!");
                }
                default -> System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

        database.desconectar();
        scanner.close();
    }
}