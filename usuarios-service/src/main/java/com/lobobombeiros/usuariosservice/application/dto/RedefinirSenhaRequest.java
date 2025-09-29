package com.lobobombeiros.usuariosservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "RedefinirSenhaRequest", description = "DTO para redefinição de senha.", example = "{\n  'token': 'abc123token',\n  'novaSenha': 'NovaSenhaForte456!'\n}")
public class RedefinirSenhaRequest {
    @Schema(description = "Token de redefinição de senha", example = "abc123token")
    private String token;
    @Schema(description = "Nova senha do usuário", example = "NovaSenhaForte456!")
    private String novaSenha;
}
