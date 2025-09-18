package com.lobobombeiros.ocorrencias.interfaces.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lobobombeiros.ocorrencias.application.dto.OcorrenciaDTO;
import com.lobobombeiros.ocorrencias.application.services.OcorrenciaService;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OcorrenciaController.class)
public class OcorrenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OcorrenciaService ocorrenciaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCriarOcorrencia() throws Exception {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setTitulo("Incêndio");

        OcorrenciaDTO savedDto = new OcorrenciaDTO();
        savedDto.setId(1L);
        savedDto.setTitulo("Incêndio");

        when(ocorrenciaService.criar(any(OcorrenciaDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/ocorrencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Incêndio"));
    }

    @Test
    void testListarTodos() throws Exception {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(1L);
        dto.setTitulo("Incêndio");

        Pageable pageable = PageRequest.of(0, 1);
        Page<OcorrenciaDTO> page = new PageImpl<>(Collections.singletonList(dto), pageable, 1);

        when(ocorrenciaService.listar(isNull(), isNull(), isNull(), isNull(), isNull(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/ocorrencias?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testListarComFiltros() throws Exception {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(1L);
        dto.setTitulo("Incêndio");
        dto.setStatus("PENDENTE");
        dto.setRegiao("Centro");

        Pageable pageable = PageRequest.of(0, 5);
        Page<OcorrenciaDTO> page = new PageImpl<>(Collections.singletonList(dto), pageable, 1);
        when(ocorrenciaService.listar(eq("PENDENTE"), eq("Centro"), eq(OcorrenciaTipo.INCENDIO), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/ocorrencias")
                        .param("status", "PENDENTE")
                        .param("regiao", "Centro")
                        .param("tipo", "INCENDIO")
                        .param("dataInicio", "2025-09-18T00:00:00")
                        .param("dataFim", "2025-09-18T23:59:59")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetDashboardStats() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOcorrencias", 5L);
        stats.put("contagemPorTipo", List.of());

        when(ocorrenciaService.getDashboardStats(any(), any())).thenReturn(stats);

        mockMvc.perform(get("/api/ocorrencias/dashboard")
                        .param("dataInicio", "2025-09-18T00:00:00")
                        .param("dataFim", "2025-09-18T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOcorrencias").value(5));
    }

    @Test
    void testExportarCsv() throws Exception {
        String csvData = "ID,Titulo,Tipo,Status,Regiao,Data Abertura\n1,Incendio,INCENDIO,PENDENTE,Centro,2025-09-18T10:00:00";
        when(ocorrenciaService.exportarCsv()).thenReturn(csvData);

        mockMvc.perform(get("/api/ocorrencias/export/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain"))
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"ocorrencias.csv\""))
                .andExpect(content().string(csvData));
    }

    @Test
    void testExportarPdf() throws Exception {
        byte[] pdfBytes = "fake-pdf-content".getBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
        when(ocorrenciaService.exportarPdf()).thenReturn(bis);

        mockMvc.perform(get("/api/ocorrencias/export/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("Content-Disposition", "inline; filename=ocorrencias.pdf"))
                .andExpect(content().bytes(pdfBytes));
    }

    @Test
    void testBuscarPorId() throws Exception {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(1L);
        dto.setTitulo("Incêndio");

        when(ocorrenciaService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/ocorrencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Incêndio"));
    }

    @Test
    void testAtualizar() throws Exception {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setTitulo("Incêndio Atualizado");

        OcorrenciaDTO updatedDto = new OcorrenciaDTO();
        updatedDto.setId(1L);
        updatedDto.setTitulo("Incêndio Atualizado");

        when(ocorrenciaService.atualizar(eq(1L), any(OcorrenciaDTO.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/ocorrencias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Incêndio Atualizado"));
    }

    @Test
    void testDeletar() throws Exception {
        mockMvc.perform(delete("/api/ocorrencias/1"))
                .andExpect(status().isNoContent());
    }
}
