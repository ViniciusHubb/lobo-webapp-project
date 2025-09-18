package com.lobobombeiros.usuariosservice.application.service;

import com.lobobombeiros.usuariosservice.domain.model.Usuario;
import com.lobobombeiros.usuariosservice.domain.repository.UsuarioRepository;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse criarUsuario(UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(usuarioRequest.getNomeCompleto());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        usuario.setPerfil(usuarioRequest.getPerfil());
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return toResponse(savedUsuario);
    }

    public Optional<UsuarioResponse> getUsuarioById(Long id) {
        return usuarioRepository.findById(id).map(this::toResponse);
    }

    public List<UsuarioResponse> getAllUsuarios() {
        return usuarioRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNomeCompleto(usuario.getNomeCompleto());
        response.setEmail(usuario.getEmail());
        response.setPerfil(usuario.getPerfil());
        return response;
    }
}
