package com.lobobombeiros.ocorrencias.domain;

import java.util.Arrays;
import java.util.List;

public enum Regiao {
    RMR("Recife", "Olinda", "Camaragibe"),
    ZDMT("Carpina", "Goiana", "Vitória de Santo Antão"),
    AGRE("Caruaru", "Garanhuns", "Belo Jardim"),
    SERT("Petrolina", "Serra Talhada", "Afogados da Ingazeira");

    private final List<String> cidades;

    Regiao(String... cidades) {
        this.cidades = Arrays.asList(cidades);
    }

    public List<String> getCidades() {
        return cidades;
    }
}

