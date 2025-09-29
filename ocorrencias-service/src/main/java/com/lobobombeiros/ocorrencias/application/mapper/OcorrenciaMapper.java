package com.lobobombeiros.ocorrencias.application.mapper;

import com.lobobombeiros.ocorrencias.application.dto.OcorrenciaDTO;
import com.lobobombeiros.ocorrencias.domain.Ocorrencia;
import org.springframework.stereotype.Component;

@Component
public class OcorrenciaMapper {

    public Ocorrencia toEntity(OcorrenciaDTO dto) {
        if (dto == null) return null;
        Ocorrencia o = new Ocorrencia();
        o.setId(dto.getId());
        o.setTitulo(dto.getTitulo());
        o.setDescricao(dto.getDescricao());
        o.setSolicitante(dto.getSolicitante());
        o.setRegiao(dto.getRegiao());
        o.setCidade(dto.getCidade());
        o.setStatus(dto.getStatus());
        o.setTipo(dto.getTipo());
        o.setLatitude(dto.getLatitude());
        o.setLongitude(dto.getLongitude());
        o.setHistorico(dto.getHistorico());
        o.setAnexos(dto.getAnexos());
        return o;
    }

    public OcorrenciaDTO toDTO(Ocorrencia o) {
        if (o == null) return null;
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(o.getId());
        dto.setTitulo(o.getTitulo());
        dto.setDescricao(o.getDescricao());
        dto.setSolicitante(o.getSolicitante());
        dto.setRegiao(o.getRegiao());
        dto.setCidade(o.getCidade());
        dto.setDataHoraAbertura(o.getDataHoraAbertura());
        dto.setDataHoraAtualizacao(o.getDataHoraAtualizacao());
        dto.setStatus(o.getStatus());
        dto.setTipo(o.getTipo());
        dto.setLatitude(o.getLatitude());
        dto.setLongitude(o.getLongitude());
        dto.setHistorico(o.getHistorico());
        dto.setAnexos(o.getAnexos());
        dto.setCriadoPor(o.getCriadoPor());
        dto.setAtualizadoPor(o.getAtualizadoPor());
        return dto;
    }
}

