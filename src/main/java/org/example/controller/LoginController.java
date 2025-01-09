package org.example.controller;

import org.example.model.Usuario;
import org.example.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login"; // Renderiza login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String nombre, @RequestParam String contrasena, Model model) {
        Usuario usuario = databaseService.iniciarSesion(nombre, contrasena);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            System.out.println("Redirigiendo al home con usuario: " + usuario.getNombre());
            return "redirect:/home?usuarioId=" + usuario.getId(); // Redirige al home con el ID del usuario
        }
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login"; // Vuelve al formulario de login en caso de error
    }

}


