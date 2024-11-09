package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlaylistTest {
    private Playlist playlist;
    private Cancion cancion1;
    private Cancion cancion2;
    private Cancion cancion3;

    @BeforeEach
    public void setUp() throws Exception {
        playlist = new Playlist();
        cancion1 = mock(Cancion.class);
        cancion2 = mock(Cancion.class);
        cancion3 = mock(Cancion.class);

        playlist.cancions.add(cancion1);
        playlist.cancions.add(cancion2);
        playlist.cancions.add(cancion3);

        when(cancion1.getNombre()).thenReturn("Amanecer");
        when(cancion2.getNombre()).thenReturn("Zafiro");
        when(cancion3.getNombre()).thenReturn("Brisas");

        playlist.cancions.add(cancion1);
        playlist.cancions.add(cancion2);
        playlist.cancions.add(cancion3);
    }
    @Test
    public void testObtenerCancionAleatoria() {
        assertNotNull(playlist.obtenerCancionAleatoria());
        assertTrue(playlist.cancions.contains(playlist.obtenerCancionAleatoria()));
    }

    @Test
    public void testObtenerCancionAleatoriaListaVacia() {
        playlist.cancions.clear();
        assertNull(playlist.obtenerCancionAleatoria());
    }



}