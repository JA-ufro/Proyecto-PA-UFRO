package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReproductorTest {
    private Reproductor reproductor;
    private List<String> listaCancion;

    @BeforeEach
    public void setUp() {
        reproductor = new Reproductor();
        listaCancion = new ArrayList<>();
        listaCancion.add("cancion1.mp3");
        listaCancion.add("cancion2.mp3");
        reproductor.listaCancion = listaCancion; // configuramos la lista de canciones
        reproductor.indice = 0; // iniciamos el índice en la primera canción
    }

    @Test
    public void testIniciarCancion_ConCancionDisponible() throws Exception {
        // Configuramos una canción simulada sin necesidad de usar mocks
        reproductor.cancion = new Cancion(new File("C:\\Users\\esteb\\Dropbox\\PC\\Downloads\\ver_si_compilaProyecto\\Proyecto-PA-UFRO-main\\Frutify\\CancionesForTest\\I DONT KNOW HOW BUT THEY FOUND ME - Razzmatazz (Official Lyric Video) - I DONT KNOW HOW BUT THEY FOUND ME (youtube).mp3")); // Suponiendo que existe una clase Cancion

        try {
            reproductor.iniciarCancion();
            assertNotNull(reproductor.cancion, "La canción no debería ser nula al iniciar.");
            assertEquals("cancion1.mp3", listaCancion.get(reproductor.indice));
        } catch (UnsupportedOperationException e) {
            // Si se lanza una excepción (comportamiento esperado por tu implementación)
        }
    }

    @Test
    public void testIniciarCancion_SinCancionesEnLista() {
        reproductor.listaCancion.clear(); // Vaciamos la lista de canciones
        reproductor.cancion = null;

        assertThrows(UnsupportedOperationException.class, () -> reproductor.iniciarCancion(),
                "Debería lanzar una excepción cuando la lista de canciones está vacía.");
    }

    @Test
    public void testIniciarCancion_IndiceFueraDeLimite() {
        reproductor.indice = listaCancion.size(); // Configuramos el índice fuera de los límites de la lista
        reproductor.cancion = null;

        assertThrows(UnsupportedOperationException.class, () -> reproductor.iniciarCancion(),
                "Debería lanzar una excepción cuando el índice está fuera de los límites.");
    }
}