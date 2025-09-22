package com.lobobombeiros.usuariosservice.application.dto;

import lombok.Data;

@Data
public class RedefinirSenhaRequest {
    private String token;
    private String novaSenha;
}

