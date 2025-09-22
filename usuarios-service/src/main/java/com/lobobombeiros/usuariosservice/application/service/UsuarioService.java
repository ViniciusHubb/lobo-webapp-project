package com.lobobombeiros.usuariosservice.application.service;

import com.lobobombeiros.usuariosservice.domain.model.Usuario;
import com.lobobombeiros.usuariosservice.domain.repository.UsuarioRepository;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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

    public Optional<UsuarioResponse> editarUsuario(Long id, UsuarioRequest usuarioRequest) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNomeCompleto(usuarioRequest.getNomeCompleto());
            usuario.setEmail(usuarioRequest.getEmail());
            usuario.setPerfil(usuarioRequest.getPerfil());
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return toResponse(updatedUsuario);
        });
    }

    public void solicitarRedefinicaoSenha(String email) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // Token válido por 1 hora
            usuarioRepository.save(usuario);
            emailService.sendPasswordResetEmail(usuario.getEmail(), token);
        });
    }

    public boolean redefinirSenha(String token, String novaSenha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByResetPasswordToken(token);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (usuario.getResetPasswordTokenExpiry().isAfter(LocalDateTime.now())) {
                usuario.setSenha(passwordEncoder.encode(novaSenha));
                usuario.setResetPasswordToken(null);
                usuario.setResetPasswordTokenExpiry(null);
                usuarioRepository.save(usuario);
                return true;
            }
        }
        return false;
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
