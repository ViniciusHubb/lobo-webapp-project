package com.lobobombeiros.ocorrencias.application.services;

import com.lobobombeiros.ocorrencias.application.dto.OcorrenciaDTO;
import com.lobobombeiros.ocorrencias.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OcorrenciaServiceTest {

    @Mock
    private OcorrenciaRepository ocorrenciaRepository;

    @InjectMocks
    private OcorrenciaService ocorrenciaService;

    @Test
    void testCriarOcorrencia() {
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setTitulo("Incêndio no centro");
        dto.setTipo(OcorrenciaTipo.INCENDIO);
        dto.setRegiao("Centro");

        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(1L);
        ocorrencia.setTitulo("Incêndio no centro");
        ocorrencia.setTipo(OcorrenciaTipo.INCENDIO);
        ocorrencia.setRegiao("Centro");
        ocorrencia.setDataHoraAbertura(LocalDateTime.now());

        when(ocorrenciaRepository.save(any(Ocorrencia.class))).thenReturn(ocorrencia);

        OcorrenciaDTO result = ocorrenciaService.criar(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Incêndio no centro", result.getTitulo());
    }

    @Test
    void testBuscarPorId() {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(1L);
        ocorrencia.setTitulo("Acidente na avenida");

        when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));

        OcorrenciaDTO result = ocorrenciaService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Acidente na avenida", result.getTitulo());
    }

    @Test
    void testListarComFiltrosPaginado() {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(1L);
        ocorrencia.setStatus("PENDENTE");
        ocorrencia.setRegiao("Zona Sul");
        ocorrencia.setTipo(OcorrenciaTipo.ACIDENTE);

        Page<Ocorrencia> page = new PageImpl<>(List.of(ocorrencia));
        when(ocorrenciaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<OcorrenciaDTO> result = ocorrenciaService.listar("PENDENTE", "Zona Sul", OcorrenciaTipo.ACIDENTE, null, null, Pageable.unpaged());

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("PENDENTE", result.getContent().get(0).getStatus());
    }

    @Test
    void testGetDashboardStats() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fim = LocalDateTime.now();

        when(ocorrenciaRepository.countByDataHoraAberturaBetween(inicio, fim)).thenReturn(10L);
        when(ocorrenciaRepository.countOcorrenciasByTipo()).thenReturn(List.of(new ContagemPorTipo(OcorrenciaTipo.INCENDIO, 5L)));
        when(ocorrenciaRepository.countOcorrenciasByRegiao()).thenReturn(List.of(new ContagemPorRegiao("Centro", 8L)));
        when(ocorrenciaRepository.countOcorrenciasByTurno()).thenReturn(List.of(new ContagemPorTurno("MANHA", 2L)));

        Map<String, Object> stats = ocorrenciaService.getDashboardStats(inicio, fim);

        assertNotNull(stats);
        assertEquals(10L, stats.get("totalOcorrencias"));
        assertFalse(((List<?>) stats.get("contagemPorTipo")).isEmpty());
        assertFalse(((List<?>) stats.get("contagemPorRegiao")).isEmpty());
        assertFalse(((List<?>) stats.get("contagemPorTurno")).isEmpty());
    }

    @Test
    void testExportarCsv() {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(1L);
        ocorrencia.setTitulo("Teste CSV");
        ocorrencia.setTipo(OcorrenciaTipo.COMUNICACAO);
        ocorrencia.setStatus("CONCLUIDO");
        ocorrencia.setRegiao("Norte");
        ocorrencia.setDataHoraAbertura(LocalDateTime.now());

        when(ocorrenciaRepository.findAll()).thenReturn(List.of(ocorrencia));

        String csv = ocorrenciaService.exportarCsv();

        assertTrue(csv.contains("ID,Titulo,Tipo,Status,Regiao,Data Abertura"));
        assertTrue(csv.contains("1,Teste CSV,COMUNICACAO,CONCLUIDO,Norte"));
    }

    @Test
    void testExportarPdf() {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(1L);
        ocorrencia.setTitulo("Teste PDF");
        ocorrencia.setTipo(OcorrenciaTipo.RESGATE);
        ocorrencia.setStatus("EM_ANDAMENTO");

        when(ocorrenciaRepository.findAll()).thenReturn(List.of(ocorrencia));

        ByteArrayInputStream pdfStream = ocorrenciaService.exportarPdf();

        assertNotNull(pdfStream);
        assertTrue(pdfStream.available() > 0);
    }
}
