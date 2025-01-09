package org.example.controller;
import org.example.model.Usuario;
import org.example.service.DatabaseService;
import org.example.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class MainController {
    private final DatabaseService databaseService;
    private final PlaylistService playlistService;

    @Autowired
    public MainController(DatabaseService databaseService, PlaylistService playlistService) {
        this.databaseService = databaseService;
        this.playlistService = playlistService;
    }

    @PostMapping("/login")
    public Usuario login(@RequestParam String nombreUsuario, @RequestParam String contrasena) throws SQLException {
        return databaseService.iniciarSesion(nombreUsuario, contrasena);
    }

    @PostMapping("/playlist")
    public void createPlaylist(@RequestParam String nombre, @RequestParam int usuarioId) {
        playlistService.createPlaylist(nombre, usuarioId);
    }
}
