package com.lobobombeiros.usuariosservice.application.dto;

import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import com.lobobombeiros.usuariosservice.domain.model.Regiao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UsuarioRequest", description = "DTO para cadastro de usuário.", example = "{\n  'nomeCompleto': 'João da Silva',\n  'email': 'joao.silva@cbm.pe.gov.br',\n  'senha': 'SenhaForte123!',\n  'perfil': 'ADMIN',\n  'regiao': 'RMR'\n}")
public class UsuarioRequest {
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String nomeCompleto;
    @Schema(description = "E-mail do usuário", example = "joao.silva@cbm.pe.gov.br")
    private String email;
    @Schema(description = "Senha do usuário", example = "SenhaForte123!")
    private String senha;
    @Schema(description = "Perfil do usuário", example = "ADMIN")
    private Perfil perfil;
    @Schema(description = "Região de atuação", example = "RMR")
    private Regiao regiao;
}
