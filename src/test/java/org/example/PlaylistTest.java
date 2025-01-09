package org.example;

import org.example.model.Cancion;
import org.example.model.Playlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para la clase Playlist.
 */
class PlaylistTest {
    private Playlist playlist;
    private Cancion cancion1;
    private Cancion cancion2;
    private Cancion cancion3;

    @BeforeEach
    public void setUp() {
        playlist = new Playlist();
        cancion1 = mock(Cancion.class);
        cancion2 = mock(Cancion.class);
        cancion3 = mock(Cancion.class);

        when(cancion1.getNombre()).thenReturn("Amanecer");
        when(cancion2.getNombre()).thenReturn("Zafiro");
        when(cancion3.getNombre()).thenReturn("Brisas");

        playlist.agregarCancion(cancion1);
        playlist.agregarCancion(cancion2);
        playlist.agregarCancion(cancion3);
    }

    @Test
    public void testObtenerCancionAleatoria() {
        System.out.println("Probando obtenerCancionAleatoria con canciones disponibles en la playlist...");
        Cancion cancionAleatoria = playlist.obtenerCancionAleatoria();
        assertNotNull(cancionAleatoria, "La canción aleatoria no debe ser null.");
        assertTrue(playlist.getCanciones().contains(cancionAleatoria), "La canción aleatoria debe estar en la playlist.");
        System.out.println("Canción aleatoria obtenida: " + cancionAleatoria.getNombre());
    }

    @Test
    public void testObtenerCancionAleatoriaListaVacia() {
        System.out.println("Probando obtenerCancionAleatoria con una playlist vacía...");
        playlist.getCanciones().clear();
        Cancion cancionAleatoria = playlist.obtenerCancionAleatoria();
        assertNull(cancionAleatoria, "La canción aleatoria debe ser null si la playlist está vacía.");
        System.out.println("Resultado esperado: null");
    }

    @Test
    public void testOrdenarAlfabeticamente() {
        System.out.println("Probando ordenarAlfabeticamente...");
        playlist.ordenarAlfabeticamente();

        List<Cancion> cancionesOrdenadas = playlist.getCanciones();

        assertEquals("Amanecer", cancionesOrdenadas.get(0).getNombre(), "La primera canción debe ser 'Amanecer'.");
        assertEquals("Brisas", cancionesOrdenadas.get(1).getNombre(), "La segunda canción debe ser 'Brisas'.");
        assertEquals("Zafiro", cancionesOrdenadas.get(2).getNombre(), "La tercera canción debe ser 'Zafiro'.");

        System.out.println("Ordenación correcta: " + cancionesOrdenadas);
    }
}
