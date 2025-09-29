package com.lobobombeiros.ocorrencias.application.dto;

import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import com.lobobombeiros.ocorrencias.domain.Regiao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "OcorrenciaDTO", description = "DTO para registro e consulta de ocorrências.", example = "{\n  'titulo': 'Acidente de Trânsito',\n  'descricao': 'Colisão entre carro e moto na BR-232.',\n  'solicitante': 'Polícia Rodoviária',\n  'regiao': 'AGRE',\n  'cidade': 'Caruaru',\n  'status': 'EM_ANDAMENTO',\n  'tipo': 'ACIDENTE_DE_TRANSITO',\n  'latitude': -8.057838,\n  'longitude': -34.882796\n}")
public class OcorrenciaDTO {
    @Schema(description = "ID da ocorrência", example = "1")
    private Long id;
    @Schema(description = "Título da ocorrência", example = "Acidente de Trânsito")
    private String titulo;
    @Schema(description = "Descrição detalhada", example = "Colisão entre carro e moto na BR-232.")
    private String descricao;
    @Schema(description = "Nome do solicitante", example = "Polícia Rodoviária")
    private String solicitante;
    @Schema(description = "Região da ocorrência", example = "AGRE")
    private Regiao regiao;
    @Schema(description = "Cidade da ocorrência", example = "Caruaru")
    private String cidade;
    @Schema(description = "Data/hora de abertura", example = "2025-09-29T08:30:00")
    private LocalDateTime dataHoraAbertura;
    @Schema(description = "Data/hora de atualização", example = "2025-09-29T09:00:00")
    private LocalDateTime dataHoraAtualizacao;
    @Schema(description = "Status da ocorrência", example = "EM_ANDAMENTO")
    private String status;
    @Schema(description = "Tipo da ocorrência", example = "ACIDENTE_DE_TRANSITO")
    private OcorrenciaTipo tipo;
    @Schema(description = "Latitude", example = "-8.057838")
    private Double latitude;
    @Schema(description = "Longitude", example = "-34.882796")
    private Double longitude;
    @Schema(description = "Histórico de status", example = "['ABERTA', 'EM_ANDAMENTO']")
    private List<String> historico;
    @Schema(description = "URLs dos anexos", example = "['http://exemplo.com/anexo1.jpg']")
    private List<String> anexos;
    @Schema(description = "Usuário que criou", example = "admin@cbm.pe.gov.br")
    private String criadoPor;
    @Schema(description = "Usuário que atualizou", example = "chefe@cbm.pe.gov.br")
    private String atualizadoPor;
}
