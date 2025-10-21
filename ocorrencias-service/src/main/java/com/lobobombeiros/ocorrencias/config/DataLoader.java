package com.lobobombeiros.ocorrencias.config;

import com.lobobombeiros.ocorrencias.domain.Ocorrencia;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaRepository;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import com.lobobombeiros.ocorrencias.domain.Regiao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final OcorrenciaRepository repository;
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        for (Regiao regiao : Regiao.values()) {
            for (String cidade : regiao.getCidades()) {
                for (int i = 0; i < 3; i++) {
                    Ocorrencia o = new Ocorrencia();
                    o.setTitulo("Ocorrência " + (i + 1) + " em " + cidade);
                    o.setDescricao("Descrição da ocorrência " + (i + 1) + " na cidade de " + cidade);
                    o.setSolicitante("Solicitante " + (i + 1));
                    o.setRegiao(regiao);
                    o.setCidade(cidade);

                    String[] statusPossiveis = {"ABERTA", "PENDENTE", "EM_ANDAMENTO", "CONCLUIDO", "CANCELADO"};
                    o.setStatus(statusPossiveis[new Random().nextInt(statusPossiveis.length)]);

                    OcorrenciaTipo[] tipos = OcorrenciaTipo.values();
                    o.setTipo(tipos[new Random().nextInt(tipos.length)]);

                    o.setDataHoraAbertura(LocalDateTime.now().minusDays(new Random().nextInt(30)));
                    o.setLatitude(-8.0 + new Random().nextDouble());
                    o.setLongitude(-35.0 + new Random().nextDouble());

                    repository.save(o);
                }
            }
        }
        System.out.println("✅ Novas ocorrências adicionadas!");
    }
}