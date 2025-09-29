package com.lobobombeiros.usuariosservice.application.dto;

import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@SuppressWarnings({"unused","UnusedDeclaration"})
@Data
@Schema(name = "InternalUserResponse", description = "DTO de resposta interna para autenticação e perfil.", example = "{\n  'email': 'admin@cbm.pe.gov.br',\n  'senha': 'SenhaForte123!',\n  'perfil': 'ADMIN'\n}")
public class InternalUserResponse {
    @Schema(description = "E-mail do usuário", example = "admin@cbm.pe.gov.br")
    private String email;
    @Schema(description = "Senha do usuário", example = "SenhaForte123!")
    private String senha;
    @Schema(description = "Perfil do usuário", example = "ADMIN")
    private Perfil perfil;
}
