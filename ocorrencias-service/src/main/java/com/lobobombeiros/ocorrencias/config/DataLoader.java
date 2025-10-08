package com.lobobombeiros.ocorrencias.config;

import com.lobobombeiros.ocorrencias.domain.Ocorrencia;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaRepository;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import com.lobobombeiros.ocorrencias.domain.Regiao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final OcorrenciaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() < 1) {
            Ocorrencia ocorrencia1 = new Ocorrencia();
            ocorrencia1.setTitulo("Incêndio em residência");
            ocorrencia1.setDescricao("Fogo em uma casa no bairro Central.");
            ocorrencia1.setSolicitante("João da Silva");
            ocorrencia1.setRegiao(Regiao.AGRE);
            ocorrencia1.setCidade("Cidade A");
            ocorrencia1.setStatus("PENDENTE");
            ocorrencia1.setTipo(OcorrenciaTipo.INCENDIO);
            ocorrencia1.setLatitude(-23.550520);
            ocorrencia1.setLongitude(-46.633308);
            ocorrencia1.setHistorico(Collections.singletonList("Ocorrência criada."));
            repository.save(ocorrencia1);

            Ocorrencia ocorrencia2 = new Ocorrencia();
            ocorrencia2.setTitulo("Gato em árvore");
            ocorrencia2.setDescricao("Um gato está preso em uma árvore alta.");
            ocorrencia2.setSolicitante("Maria Oliveira");
            ocorrencia2.setRegiao(Regiao.RMR);
            ocorrencia2.setCidade("Cidade B");
            ocorrencia2.setStatus("PENDENTE");
            ocorrencia2.setTipo(OcorrenciaTipo.RESGATE);
            ocorrencia2.setLatitude(-22.906847);
            ocorrencia2.setLongitude(-43.172896);
            ocorrencia2.setHistorico(Collections.singletonList("Ocorrência criada."));
            repository.save(ocorrencia2);
        }
    }
}

