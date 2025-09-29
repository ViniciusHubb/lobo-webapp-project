package com.lobobombeiros.ocorrencias.infrastructure.init;

import com.lobobombeiros.ocorrencias.domain.Ocorrencia;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaRepository;
import com.lobobombeiros.ocorrencias.domain.OcorrenciaTipo;
import com.lobobombeiros.ocorrencias.domain.Regiao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer {
    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @PostConstruct
    public void init() {
        if (ocorrenciaRepository.count() == 0) {
            Ocorrencia o1 = new Ocorrencia();
            o1.setTitulo("Incêndio em residência");
            o1.setDescricao("Incêndio de grandes proporções em casa no bairro Boa Vista.");
            o1.setSolicitante("Morador");
            o1.setRegiao(Regiao.RMR);
            o1.setCidade("Recife");
            o1.setDataHoraAbertura(LocalDateTime.now().minusDays(2));
            o1.setStatus("ABERTA");
            o1.setTipo(OcorrenciaTipo.INCENDIO);
            o1.setLatitude(-8.047562);
            o1.setLongitude(-34.876964);
            ocorrenciaRepository.save(o1);

            Ocorrencia o2 = new Ocorrencia();
            o2.setTitulo("Acidente de trânsito");
            o2.setDescricao("Colisão entre ônibus e carro na Av. Caxangá.");
            o2.setSolicitante("Polícia Militar");
            o2.setRegiao(Regiao.RMR);
            o2.setCidade("Recife");
            o2.setDataHoraAbertura(LocalDateTime.now().minusDays(1));
            o2.setStatus("EM_ANDAMENTO");
            o2.setTipo(OcorrenciaTipo.ACIDENTE_DE_TRANSITO);
            o2.setLatitude(-8.035);
            o2.setLongitude(-34.928);
            ocorrenciaRepository.save(o2);
        }
    }
}

