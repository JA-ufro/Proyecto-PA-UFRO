package org.example.controller;
import org.example.model.Usuario;
import org.example.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping
    public String home(@RequestParam Long usuarioId, Model model) {
        Usuario usuario = databaseService.obtenerUsuarioPorId(usuarioId);
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "error"; // Renderiza error.html si el usuario no existe
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("funciones", List.of("Crear Playlist", "Ver Playlist", "Reproducir Playlist")); // Lista de funciones
        return "index"; // Renderiza index.html
    }
}
