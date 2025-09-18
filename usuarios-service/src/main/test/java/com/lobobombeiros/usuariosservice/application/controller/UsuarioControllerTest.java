package com.lobobombeiros.usuariosservice.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioResponse;
import com.lobobombeiros.usuariosservice.application.service.UsuarioService;
import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNomeCompleto("usuario teste");
        usuarioRequest.setEmail("usuario@example.com");
        usuarioRequest.setSenha("senha123");
        usuarioRequest.setPerfil(Perfil.USUARIO);

        usuarioResponse = new UsuarioResponse();
        usuarioResponse.setId(1L);
        usuarioResponse.setNomeCompleto("usuario teste");
        usuarioResponse.setEmail("usuario@example.com");
        usuarioResponse.setPerfil(Perfil.USUARIO);
    }

    @Test
    void criarUsuario_deveRetornarUsuarioCriado() throws Exception {
        when(usuarioService.criarUsuario(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/usuarios")
                .with(user("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCompleto").value(usuarioResponse.getNomeCompleto()))
                .andExpect(jsonPath("$.email").value(usuarioResponse.getEmail()));
    }

    @Test
    void getUsuarioById_quandoUsuarioExistir_deveRetornarUsuario() throws Exception {
        when(usuarioService.getUsuarioById(1L)).thenReturn(Optional.of(usuarioResponse));

        mockMvc.perform(get("/usuarios/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCompleto").value(usuarioResponse.getNomeCompleto()));
    }

    @Test
    void getUsuarioById_quandoUsuarioNaoExistir_deveRetornarNotFound() throws Exception {
        when(usuarioService.getUsuarioById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsuarios_deveRetornarListaDeUsuarios() throws Exception {
        when(usuarioService.getAllUsuarios()).thenReturn(Collections.singletonList(usuarioResponse));

        mockMvc.perform(get("/usuarios")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeCompleto").value(usuarioResponse.getNomeCompleto()));
    }

    @Test
    void deleteUsuario_deveRetornarNoContent() throws Exception {
        doNothing().when(usuarioService).deleteUsuario(anyLong());

        mockMvc.perform(delete("/usuarios/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
