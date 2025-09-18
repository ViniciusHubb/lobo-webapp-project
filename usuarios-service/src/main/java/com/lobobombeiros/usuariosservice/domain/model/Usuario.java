package com.lobobombeiros.usuariosservice.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    public Usuario(String nomeCompleto, String email, String senha, Perfil perfil) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }
}
