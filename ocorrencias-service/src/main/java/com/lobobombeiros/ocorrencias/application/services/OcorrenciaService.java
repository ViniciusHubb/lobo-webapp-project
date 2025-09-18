package com.lobobombeiros.ocorrencias.application.services;

import com.lobobombeiros.ocorrencias.application.dto.OcorrenciaDTO;
import com.lobobombeiros.ocorrencias.domain.*;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    public OcorrenciaDTO criar(OcorrenciaDTO dto) {
        Ocorrencia ocorrencia = toEntity(dto);
        // A data de abertura e o usuário de criação são definidos pela auditoria
        ocorrencia = ocorrenciaRepository.save(ocorrencia);
        return toDTO(ocorrencia);
    }

    public Page<OcorrenciaDTO> listar(String status, String regiao, OcorrenciaTipo tipo, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        Specification<Ocorrencia> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (regiao != null && !regiao.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("regiao"), regiao));
            }
            if (tipo != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipo"), tipo));
            }
            if (dataInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraAbertura"), dataInicio));
            }
            if (dataFim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraAbertura"), dataFim));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return ocorrenciaRepository.findAll(spec, pageable).map(this::toDTO);
    }

    public OcorrenciaDTO buscarPorId(Long id) {
        return ocorrenciaRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public OcorrenciaDTO atualizar(Long id, OcorrenciaDTO dto) {
        return ocorrenciaRepository.findById(id).map(ocorrencia -> {
            ocorrencia.setTitulo(dto.getTitulo());
            ocorrencia.setDescricao(dto.getDescricao());
            ocorrencia.setStatus(dto.getStatus());
            ocorrencia.setTipo(dto.getTipo());
            ocorrencia.setLatitude(dto.getLatitude());
            ocorrencia.setLongitude(dto.getLongitude());
            ocorrencia.setSolicitante(dto.getSolicitante());
            ocorrencia.setRegiao(dto.getRegiao());
            // A data de atualização e o usuário são definidos pela auditoria
            ocorrencia = ocorrenciaRepository.save(ocorrencia);
            return toDTO(ocorrencia);
        }).orElse(null);
    }

    public void deletar(Long id) {
        ocorrenciaRepository.deleteById(id);
    }

    public Map<String, Object> getDashboardStats(LocalDateTime dataInicio, LocalDateTime dataFim) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOcorrencias", ocorrenciaRepository.countByDataHoraAberturaBetween(dataInicio, dataFim));
        stats.put("contagemPorTipo", ocorrenciaRepository.countOcorrenciasByTipo());
        stats.put("contagemPorRegiao", ocorrenciaRepository.countOcorrenciasByRegiao());
        stats.put("contagemPorTurno", ocorrenciaRepository.countOcorrenciasByTurno());
        return stats;
    }

    public String exportarCsv() {
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            pw.println("ID,Titulo,Tipo,Status,Regiao,Data Abertura");
            ocorrencias.forEach(o ->
                pw.printf("%d,%s,%s,%s,%s,%s\n",
                    o.getId(), o.getTitulo(), o.getTipo(), o.getStatus(), o.getRegiao(), o.getDataHoraAbertura())
            );
        }
        return sw.toString();
    }

    public ByteArrayInputStream exportarPdf() {
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Relatorio de Ocorrencias"));
        document.add(new Paragraph(" ")); // Linha em branco

        for (Ocorrencia o : ocorrencias) {
            document.add(new Paragraph("ID: " + o.getId()));
            document.add(new Paragraph("Titulo: " + o.getTitulo()));
            document.add(new Paragraph("Tipo: " + o.getTipo()));
            document.add(new Paragraph("Status: " + o.getStatus()));
            document.add(new Paragraph("--------------------------------------------------"));
        }

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private OcorrenciaDTO toDTO(Ocorrencia ocorrencia) {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(ocorrencia.getId());
        dto.setTitulo(ocorrencia.getTitulo());
        dto.setDescricao(ocorrencia.getDescricao());
        dto.setSolicitante(ocorrencia.getSolicitante());
        dto.setRegiao(ocorrencia.getRegiao());
        dto.setDataHoraAbertura(ocorrencia.getDataHoraAbertura());
        dto.setDataHoraAtualizacao(ocorrencia.getDataHoraAtualizacao());
        dto.setStatus(ocorrencia.getStatus());
        dto.setTipo(ocorrencia.getTipo());
        dto.setLatitude(ocorrencia.getLatitude());
        dto.setLongitude(ocorrencia.getLongitude());
        dto.setHistorico(ocorrencia.getHistorico());
        dto.setAnexos(ocorrencia.getAnexos());
        dto.setCriadoPor(ocorrencia.getCriadoPor());
        dto.setAtualizadoPor(ocorrencia.getAtualizadoPor());
        return dto;
    }

    private Ocorrencia toEntity(OcorrenciaDTO dto) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(dto.getId());
        ocorrencia.setTitulo(dto.getTitulo());
        ocorrencia.setDescricao(dto.getDescricao());
        ocorrencia.setSolicitante(dto.getSolicitante());
        ocorrencia.setRegiao(dto.getRegiao());
        ocorrencia.setStatus(dto.getStatus());
        ocorrencia.setTipo(dto.getTipo());
        ocorrencia.setLatitude(dto.getLatitude());
        ocorrencia.setLongitude(dto.getLongitude());
        ocorrencia.setHistorico(dto.getHistorico());
        ocorrencia.setAnexos(dto.getAnexos());
        return ocorrencia;
    }
}
