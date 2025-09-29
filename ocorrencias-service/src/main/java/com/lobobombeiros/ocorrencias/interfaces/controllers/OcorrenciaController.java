package com.lobobombeiros.ocorrencias.interfaces.controllers;

import com.lobobombeiros.ocorrencias.application.dto.OcorrenciaDTO;
import com.lobobombeiros.ocorrencias.application.services.OcorrenciaService;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import com.lobobombeiros.ocorrencias.domain.Regiao;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    @PostMapping
    @Operation(description = "Criar ocorrência. Permissão: ADMIN, CHEFE, OPERADOR.")
    public ResponseEntity<OcorrenciaDTO> criar(@RequestBody OcorrenciaDTO dto) {
        OcorrenciaDTO created = ocorrenciaService.criar(dto);
        return ResponseEntity.created(URI.create("/api/ocorrencias/" + created.getId())).body(created);
    }

    @GetMapping
    @Operation(description = "Listar ocorrências. Permissão: ADMIN, CHEFE, ANALISTA.")
    public ResponseEntity<Page<OcorrenciaDTO>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Regiao regiao,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) OcorrenciaTipo tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            Pageable pageable) {
        return ResponseEntity.ok(ocorrenciaService.listar(status, regiao, cidade, tipo, dataInicio, dataFim, pageable));
    }

    @GetMapping("/dashboard")
    @Operation(description = "Visualizar dashboard de ocorrências. Permissão: ADMIN, CHEFE, ANALISTA.")
    public ResponseEntity<Map<String, Object>> getDashboardStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        // Converte para início e fim do dia
        var startDateTime = dataInicio.atStartOfDay();
        var endDateTime = dataFim.atTime(23, 59, 59);
        return ResponseEntity.ok(ocorrenciaService.getDashboardStats(startDateTime, endDateTime));
    }

    @GetMapping("/export/csv")
    @Operation(description = "Exportar ocorrências em CSV. Permissão: ADMIN, CHEFE, ANALISTA.")
    public ResponseEntity<String> exportarCsv() {
        String csv = ocorrenciaService.exportarCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "ocorrencias.csv");
        return ResponseEntity.ok().headers(headers).body(csv);
    }

    @GetMapping("/export/pdf")
    @Operation(description = "Exportar ocorrências em PDF. Permissão: ADMIN, CHEFE, ANALISTA.")
    public ResponseEntity<InputStreamResource> exportarPdf() {
        var bis = ocorrenciaService.exportarPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ocorrencias.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/{id}")
    @Operation(description = "Consultar ocorrência por ID. Permissão: ADMIN, CHEFE, ANALISTA.")
    public ResponseEntity<OcorrenciaDTO> buscarPorId(@PathVariable Long id) {
        OcorrenciaDTO dto = ocorrenciaService.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(description = "Editar ocorrência. Permissão: ADMIN, CHEFE.")
    public ResponseEntity<OcorrenciaDTO> atualizar(@PathVariable Long id, @RequestBody OcorrenciaDTO dto) {
        OcorrenciaDTO updatedDto = ocorrenciaService.atualizar(id, dto);
        return updatedDto != null ? ResponseEntity.ok(updatedDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Excluir ocorrência. Permissão: ADMIN.")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ocorrenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
