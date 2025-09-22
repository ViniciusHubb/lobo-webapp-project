package com.lobobombeiros.usuariosservice.domain.repository;

import com.lobobombeiros.usuariosservice.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetPasswordToken(String token);
}
