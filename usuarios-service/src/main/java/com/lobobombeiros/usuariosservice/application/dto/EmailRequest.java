package com.lobobombeiros.usuariosservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "EmailRequest", description = "DTO para requisição de e-mail.", example = "{\n  'email': 'usuario@cbm.pe.gov.br'\n}")
public class EmailRequest {
    @Schema(description = "E-mail do usuário", example = "usuario@cbm.pe.gov.br")
    private String email;
}
