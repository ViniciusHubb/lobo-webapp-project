package com.lobobombeiros.usuariosservice.application.dto;

import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String nomeCompleto;
    private String email;
    private Perfil perfil;
}
