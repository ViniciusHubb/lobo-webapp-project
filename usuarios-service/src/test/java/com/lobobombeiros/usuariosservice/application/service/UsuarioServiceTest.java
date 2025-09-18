package com.lobobombeiros.usuariosservice.application.service;

import com.lobobombeiros.usuariosservice.application.dto.UsuarioRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioResponse;
import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import com.lobobombeiros.usuariosservice.domain.model.Usuario;
import com.lobobombeiros.usuariosservice.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNomeCompleto("Usuario Teste");
        usuario.setEmail("teste@example.com");
        usuario.setSenha("senhaCriptografada");
        usuario.setPerfil(Perfil.USUARIO);

        usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNomeCompleto("Usuario Teste");
        usuarioRequest.setEmail("teste@example.com");
        usuarioRequest.setSenha("senha123");
        usuarioRequest.setPerfil(Perfil.USUARIO);
    }

    @Test
    void criarUsuario_deveSalvarUsuarioComSenhaCriptografada() {
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponse response = usuarioService.criarUsuario(usuarioRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(usuario.getId());
        assertThat(response.getNomeCompleto()).isEqualTo(usuario.getNomeCompleto());
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void getUsuarioById_quandoUsuarioExistir_deveRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponse> response = usuarioService.getUsuarioById(1L);

        assertThat(response).isPresent();
        assertThat(response.get().getId()).isEqualTo(usuario.getId());
    }

    @Test
    void getUsuarioById_quandoUsuarioNaoExistir_deveRetornarOptionalVazio() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UsuarioResponse> response = usuarioService.getUsuarioById(1L);

        assertThat(response).isNotPresent();
    }

    @Test
    void getAllUsuarios_deveRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

        List<UsuarioResponse> responses = usuarioService.getAllUsuarios();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(usuario.getId());
    }

    @Test
    void deleteUsuario_deveChamarDeleteByIdNoRepositorio() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
