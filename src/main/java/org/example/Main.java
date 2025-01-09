package org.example;

import org.example.model.Cancion;
import org.example.model.Playlist;
import org.example.model.Usuario;
import org.example.service.DatabaseService;
import org.example.service.Reproductor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
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
        System.out.println("Aplicación iniciada. Accede a http://localhost:8080/login para usar la aplicación.");
    }
}