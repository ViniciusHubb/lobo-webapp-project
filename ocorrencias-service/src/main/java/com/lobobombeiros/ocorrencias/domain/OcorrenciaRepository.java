package com.lobobombeiros.ocorrencias.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long>, JpaSpecificationExecutor<Ocorrencia> {
    List<Ocorrencia> findByStatus(String status);

    long countByDataHoraAberturaBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.lobobombeiros.ocorrencias.domain.ContagemPorTipo(o.tipo, COUNT(o)) FROM Ocorrencia o GROUP BY o.tipo")
    List<ContagemPorTipo> countOcorrenciasByTipo();

    @Query("SELECT new com.lobobombeiros.ocorrencias.domain.ContagemPorRegiao(o.regiao, COUNT(o)) FROM Ocorrencia o GROUP BY o.regiao")
    List<ContagemPorRegiao> countOcorrenciasByRegiao();

    @Query("SELECT new com.lobobombeiros.ocorrencias.domain.ContagemPorTurno(" +
            "CASE " +
            "WHEN HOUR(o.dataHoraAbertura) BETWEEN 6 AND 11 THEN 'MANHA' " +
            "WHEN HOUR(o.dataHoraAbertura) BETWEEN 12 AND 17 THEN 'TARDE' " +
            "ELSE 'NOITE' " +
            "END, COUNT(o)) " +
            "FROM Ocorrencia o GROUP BY CASE " +
            "WHEN HOUR(o.dataHoraAbertura) BETWEEN 6 AND 11 THEN 'MANHA' " +
            "WHEN HOUR(o.dataHoraAbertura) BETWEEN 12 AND 17 THEN 'TARDE' " +
            "ELSE 'NOITE' " +
            "END")
    List<ContagemPorTurno> countOcorrenciasByTurno();
}
