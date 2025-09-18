package com.lobobombeiros.usuariosservice.application.dto;

import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import lombok.Data;

@Data
public class UsuarioRequest {
    private String nomeCompleto;
    private String email;
    private String senha;
    private Perfil perfil;
}
