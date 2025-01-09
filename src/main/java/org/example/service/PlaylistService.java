package org.example.service;
import org.example.model.Cancion;
import org.example.model.Playlist;
import org.example.model.Usuario;
import org.example.repository.PlaylistRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    public Playlist createPlaylist(String nombre, int usuarioId) {
        // Buscar al usuario por su ID
        Usuario usuario = usuarioRepository.findById((long) usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear nueva playlist
        Playlist playlist = new Playlist(nombre, usuario);

        // Guardar playlist en la base de datos
        return playlistRepository.save(playlist);
    }

    public Playlist ordenarAlfabeticamente(Playlist playlist) {
        playlist.ordenarAlfabeticamente();
        return playlist;
    }

    public Cancion obtenerCancionAleatoria(Playlist playlist) {
        return playlist.obtenerCancionAleatoria();
    }
    // Otros métodos de servicios para las playlists
}
