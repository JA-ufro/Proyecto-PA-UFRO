package org.example.controller;

import org.example.model.Playlist;
import org.example.model.Usuario;
import org.example.service.DatabaseService;
import org.example.service.Reproductor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private Reproductor reproductor;

    // Página de inicio de sesión
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login"; // Renderiza login.html
    }

    // Procesar inicio de sesión
    @PostMapping("/login")
    public String login(@RequestParam String nombre, @RequestParam String contrasena, Model model) {
        Usuario usuario = databaseService.iniciarSesion(nombre, contrasena);
        if (usuario != null) {
            // Redirige al index con el usuario
            model.addAttribute("usuario", usuario);
            return "redirect:/playlist/index?usuarioId=" + usuario.getId();
        }
        System.out.println("Credenciales inválidas para usuario: " + nombre);
        // Credenciales incorrectas
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }


    // Mostrar el indice con las funciones disponibles
    @GetMapping("/index")
    public String index(@RequestParam Long usuarioId, Model model) {
        Usuario usuario = databaseService.obtenerUsuarioPorId(usuarioId);
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "error"; // Renderiza error.html si el usuario no existe
        }

        List<Playlist> playlists = databaseService.obtenerPlaylists(usuario);
        if (playlists == null || playlists.isEmpty()) {
            model.addAttribute("mensaje", "No hay playlists disponibles.");
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("playlists", databaseService.obtenerPlaylists(usuario));
        return "index"; // Renderiza index.html
    }



    // Formulario para crear nueva playlist
    @GetMapping("/crear")
    public String crearPlaylistForm(@RequestParam Long usuarioId, Model model) {
        model.addAttribute("usuarioId", usuarioId);
        return "playlist/crear"; // Renderiza playlist/crear.html
    }

    // Crear nueva playlist
    @PostMapping("/crear")
    public String crearPlaylist(@RequestParam Long usuarioId, @RequestParam String nombre) {
        Usuario usuario = databaseService.obtenerUsuarioPorId(usuarioId);
        if (usuario != null) {
            databaseService.crearPlaylist(nombre, usuario);
        }
        return "redirect:/playlist?usuarioId=" + usuarioId; // Redirige al listado de playlists
    }

    // Ordenar playlists alfabéticamente
    @PostMapping("/ordenar")
    public String ordenarPlaylist(@RequestParam Long usuarioId) {
        Usuario usuario = databaseService.obtenerUsuarioPorId(usuarioId);
        if (usuario != null) {
            databaseService.ordenarPlaylists(usuario);
        }
        return "redirect:/playlist?usuarioId=" + usuarioId;
    }

    // Reproducir playlist
    @PostMapping("/{id}/reproducir")
    public String reproducirPlaylist(@PathVariable Long id) {
        Playlist playlist = databaseService.obtenerPlaylistPorId(id);
        if (playlist != null) {
            reproductor.reproducirPlaylist(playlist);
        }
        return "redirect:/playlist/" + id; // Redirige a la página de la playlist
    }

    // Mostrar playlist específica
    @GetMapping("/{id}")
    public String mostrarPlaylist(@PathVariable Long id, Model model) {
        Playlist playlist = databaseService.obtenerPlaylistPorId(id);
        if (playlist == null) {
            model.addAttribute("error", "Playlist no encontrada");
            return "error";
        }
        model.addAttribute("playlist", playlist);
        return "playlist/detalle"; // Renderiza playlist/detalle.html
    }

    // Eliminar playlist
    @PostMapping("/{id}/eliminar")
    public String eliminarPlaylist(@PathVariable Long id, @RequestParam Long usuarioId) {
        databaseService.eliminarPlaylist(id);
        return "redirect:/playlist?usuarioId=" + usuarioId; // Redirige al listado de playlists
    }
    @GetMapping("/agregarCancion")
    public String agregarCancionForm(@RequestParam Long playlistId, Model model) {
        Playlist playlist = databaseService.obtenerPlaylistPorId(playlistId);
        if (playlist == null) {
            model.addAttribute("error", "Playlist no encontrada");
            return "error";
        }
        model.addAttribute("playlist", playlist);
        return "playlist/agregarCancion"; // Renderiza el formulario para agregar canción
    }

    @PostMapping("/agregarCancion")
    public String agregarCancion(@RequestParam Long playlistId, @RequestParam String nombreCancion, @RequestParam String rutaArchivo) {
        databaseService.agregarCancionAPlaylist(playlistId, nombreCancion, rutaArchivo);
        return "redirect:/playlist/detalle?playlistId=" + playlistId;
    }
    @GetMapping("/{playlistId}/reproducir")
    public String reproducirPlaylist(@PathVariable Long playlistId, Model model) {
        Playlist playlist = databaseService.obtenerPlaylistPorId(playlistId);
        if (playlist == null) {
            model.addAttribute("error", "Playlist no encontrada");
            return "error";
        }

        model.addAttribute("playlist", playlist);
        return "playlist/reproducir"; // Página para reproducir la playlist
    }
}