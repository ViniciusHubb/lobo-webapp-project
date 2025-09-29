package com.lobobombeiros.usuariosservice.application.controller;

import com.lobobombeiros.usuariosservice.application.dto.EmailRequest;
import com.lobobombeiros.usuariosservice.application.dto.InternalUserResponse;
import com.lobobombeiros.usuariosservice.application.dto.RedefinirSenhaRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioResponse;
import com.lobobombeiros.usuariosservice.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "Criar usuário. Permissão: ADMIN.")
    public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse usuario = usuarioService.criarUsuario(usuarioRequest);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(description = "Editar usuário. Permissão: ADMIN, CHEFE.")
    public ResponseEntity<UsuarioResponse> editarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest) {
        return usuarioService.editarUsuario(id, usuarioRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/solicitar-redefinicao-senha")
    @Operation(description = "Solicitar redefinição de senha. Permissão: público (não autenticado).")
    public ResponseEntity<Void> solicitarRedefinicaoSenha(@RequestBody EmailRequest request) {
        usuarioService.solicitarRedefinicaoSenha(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    @Operation(description = "Redefinir senha. Permissão: público (não autenticado, via token).")
    public ResponseEntity<Void> redefinirSenha(@RequestBody RedefinirSenhaRequest request) {
        boolean sucesso = usuarioService.redefinirSenha(request.getToken(), request.getNovaSenha());
        return sucesso ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    @Operation(description = "Consultar usuário por ID. Permissão: ADMIN, CHEFE, ANALISTA.")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(description = "Listar todos os usuários. Permissão: ADMIN, CHEFE, ANALISTA.")
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Excluir usuário. Permissão: ADMIN.")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/internal/by-email/{email}")
    @Operation(description = "Consulta interna por e-mail. Permissão: serviços internos.")
    public ResponseEntity<InternalUserResponse> getInternalUserByEmail(@PathVariable String email) {
        return usuarioService.getInternalUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
