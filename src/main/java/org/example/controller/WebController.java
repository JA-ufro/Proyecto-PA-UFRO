package org.example.controller;

import org.example.model.Playlist;
import org.example.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlist")
public class WebController {

    @Autowired
    private PlaylistService playlistService;

    @PostMapping("/create")
    public Playlist createPlaylist(@RequestParam String nombre, @RequestParam int usuarioId) {
        return playlistService.createPlaylist(nombre, usuarioId);
    }
}
