package org.example;

import org.example.service.Reproductor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReproductorTest {
    private Reproductor reproductor;
    private List<File> playlist;

    @BeforeEach
    public void setUp() {
        reproductor = new Reproductor();
        playlist = new ArrayList<>();
        // Simulamos archivos para las pruebas (canciones de ejemplo)
        playlist.add(new File("CancionesForTest/cancion1.mp3"));
        playlist.add(new File("CancionesForTest/cancion2.mp3"));
        // Establecemos la playlist del reproductor directamente
        reproductor.playlist = playlist;
    }

    @Test
    public void testReproducirPlaylist_ConCanciones() {
        // Simulamos agregar canciones a la playlist
        assertFalse(reproductor.playlist.isEmpty(), "La playlist no debe estar vacía.");

        // Llamamos al método de reproducción
        reproductor.reproducirPlaylist();

        // Aseguramos que la canción actual no es nula y que se ha seleccionado la primera canción
        assertNotNull(reproductor.currentSong, "La canción actual no debe ser nula.");
        assertEquals(playlist.get(0), reproductor.currentSong, "La primera canción debe ser la que se reproduce.");
    }

    @Test
    public void testReproducirPlaylist_SiPlaylistEstaVacia() {
        reproductor.playlist.clear(); // Vaciamos la playlist
        reproductor.currentSong = null; // Nos aseguramos de que no haya ninguna canción seleccionada

        reproductor.reproducirPlaylist(); // Intentamos reproducir con la lista vacía

        // Comprobamos que no se ha seleccionado ninguna canción para reproducir
        assertNull(reproductor.currentSong, "No debe haber canción seleccionada si la playlist está vacía.");
    }

    @Test
    public void testEliminarCancion() {
        // Añadimos una canción a la playlist y luego la eliminamos
        File songToRemove = playlist.get(0);

        reproductor.eliminarCancion(); // Eliminamos la canción actual (debería ser la primera)

        // Comprobamos que la canción ha sido eliminada de la lista
        assertFalse(reproductor.playlist.contains(songToRemove), "La canción eliminada no debe estar en la lista.");
    }

    @Test
    public void testPausarReanudar() {
        // Verificamos el comportamiento de pausar y reanudar la canción
        boolean wasPaused = reproductor.isPaused; // Estado inicial de pausa

        reproductor.pausarReanudar(); // Pausamos o reanudamos la canción
        assertNotEquals(wasPaused, reproductor.isPaused, "El estado de pausa debe cambiar al ejecutar pausarReanudar.");
    }

    @Test
    public void testPausarReanudar_SinReproduccion() {
        // Aseguramos que el estado de pausa no cambie si no se está reproduciendo
        reproductor.isPaused = true;
        reproductor.pausarReanudar(); // Intentamos pausar/reanudar sin reproducción activa

        assertTrue(reproductor.isPaused, "El reproductor debería seguir pausado si no está reproduciendo.");
    }
}
