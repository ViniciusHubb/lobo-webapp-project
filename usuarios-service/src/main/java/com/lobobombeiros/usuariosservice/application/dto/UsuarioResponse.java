package com.lobobombeiros.usuariosservice.application.dto;

import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import com.lobobombeiros.usuariosservice.domain.model.Regiao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UsuarioResponse", description = "DTO de resposta para consulta de usuário.", example = "{\n  'id': 1,\n  'nomeCompleto': 'João da Silva',\n  'email': 'joao.silva@cbm.pe.gov.br',\n  'perfil': 'ADMIN',\n  'regiao': 'RMR'\n}")
public class UsuarioResponse {
    @Schema(description = "ID do usuário", example = "1")
    private Long id;
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String nomeCompleto;
    @Schema(description = "E-mail do usuário", example = "joao.silva@cbm.pe.gov.br")
    private String email;
    @Schema(description = "Perfil do usuário", example = "ADMIN")
    private Perfil perfil;
    @Schema(description = "Região de atuação", example = "RMR")
    private Regiao regiao;
}
