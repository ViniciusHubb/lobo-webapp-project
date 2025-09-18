package com.lobobombeiros.usuariosservice.application.controller;

import com.lobobombeiros.usuariosservice.application.dto.UsuarioRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioResponse;
import com.lobobombeiros.usuariosservice.application.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse usuario = usuarioService.criarUsuario(usuarioRequest);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
