package com.lobobombeiros.ocorrencias.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private String solicitante;

    @Enumerated(EnumType.STRING)
    private Regiao regiao;

    private String cidade;

    @CreatedDate
    private LocalDateTime dataHoraAbertura;

    @LastModifiedDate
    private LocalDateTime dataHoraAtualizacao;

    private String status; // PENDENTE, EM_ANDAMENTO, CONCLUIDO

    @Enumerated(EnumType.STRING)
    private OcorrenciaTipo tipo;

    private Double latitude;
    private Double longitude;

    @ElementCollection
    @CollectionTable(name = "ocorrencia_historico", joinColumns = @JoinColumn(name = "ocorrencia_id"))
    @Column(name = "evento")
    private List<String> historico;

    @ElementCollection
    @CollectionTable(name = "ocorrencia_anexos", joinColumns = @JoinColumn(name = "ocorrencia_id"))
    @Column(name = "url_anexo")
    private List<String> anexos;

    @CreatedBy
    private String criadoPor;

    @LastModifiedBy
    private String atualizadoPor;
}
