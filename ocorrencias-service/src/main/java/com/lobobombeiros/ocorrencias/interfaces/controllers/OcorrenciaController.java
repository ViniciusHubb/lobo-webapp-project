package com.lobobombeiros.ocorrencias.interfaces.controllers;

import com.lobobombeiros.ocorrencias.application.dto.OcorrenciaDTO;
import com.lobobombeiros.ocorrencias.application.services.OcorrenciaService;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    @PostMapping
    public ResponseEntity<OcorrenciaDTO> criar(@RequestBody OcorrenciaDTO dto) {
        return ResponseEntity.ok(ocorrenciaService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<OcorrenciaDTO>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String regiao,
            @RequestParam(required = false) OcorrenciaTipo tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            Pageable pageable) {
        return ResponseEntity.ok(ocorrenciaService.listar(status, regiao, tipo, dataInicio, dataFim, pageable));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return ResponseEntity.ok(ocorrenciaService.getDashboardStats(dataInicio, dataFim));
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportarCsv() {
        String csv = ocorrenciaService.exportarCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "ocorrencias.csv");
        return ResponseEntity.ok().headers(headers).body(csv);
    }

    @GetMapping("/export/pdf")
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
    public ResponseEntity<OcorrenciaDTO> buscarPorId(@PathVariable Long id) {
        OcorrenciaDTO dto = ocorrenciaService.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> atualizar(@PathVariable Long id, @RequestBody OcorrenciaDTO dto) {
        OcorrenciaDTO updatedDto = ocorrenciaService.atualizar(id, dto);
        return updatedDto != null ? ResponseEntity.ok(updatedDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ocorrenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
