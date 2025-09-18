package com.lobobombeiros.ocorrencias.application.dto;

import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OcorrenciaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String solicitante;
    private String regiao;
    private LocalDateTime dataHoraAbertura;
    private LocalDateTime dataHoraAtualizacao;
    private String status;
    private OcorrenciaTipo tipo;
    private Double latitude;
    private Double longitude;
    private List<String> historico;
    private List<String> anexos;
    private String criadoPor;
    private String atualizadoPor;
}
