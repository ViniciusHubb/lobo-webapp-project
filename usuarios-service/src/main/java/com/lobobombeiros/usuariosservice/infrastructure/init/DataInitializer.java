package com.lobobombeiros.usuariosservice.infrastructure.init;

import com.lobobombeiros.usuariosservice.domain.model.Perfil;
import com.lobobombeiros.usuariosservice.domain.model.Regiao;
import com.lobobombeiros.usuariosservice.domain.model.Usuario;
import com.lobobombeiros.usuariosservice.domain.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.clean-on-start:false}")
    private boolean cleanOnStart;

    @Value("${app.seed.admin.email:webapplobo@gmail.com}")
    private String adminEmail;

    @Value("${app.seed.admin.password:AdminPass123!}")
    private String adminPassword;

    @Value("${app.seed.chefe.email:chefe.rmr@cbm.pe.gov.br}")
    private String chefeEmail;

    @Value("${app.seed.chefe.password:ChefePass123!}")
    private String chefePassword;

    @Value("${app.seed.analista.email:analista.agre@cbm.pe.gov.br}")
    private String analistaEmail;

    @Value("${app.seed.analista.password:AnalistaPass123!}")
    private String analistaPassword;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (cleanOnStart) {
            LOGGER.info("app.seed.clean-on-start=true -> cleaning users table");
            usuarioRepository.deleteAll();
        }

        ensureUser(adminEmail, "Usuario Teste Postman", adminPassword, Perfil.ADMIN, null);
        ensureUser(chefeEmail, "Chefe RMR", chefePassword, Perfil.CHEFE, Regiao.RMR);
        ensureUser(analistaEmail, "Analista Agreste", analistaPassword, Perfil.ANALISTA, Regiao.AGRE);
    }

    private void ensureUser(String email, String nomeCompleto, String rawPassword, Perfil perfil, Regiao regiao) {
        String passwordToUse = rawPassword;
        if (passwordToUse == null || passwordToUse.isBlank()) {
            passwordToUse = UUID.randomUUID().toString();
            LOGGER.warn("No password provided for {} - generated random password (only for local/dev).", email);
        }

        final String finalPassword = passwordToUse; // effectively final for lambdas

        usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {
            usuario.setNomeCompleto(nomeCompleto);
            usuario.setSenha(passwordEncoder.encode(finalPassword));
            usuario.setPerfil(perfil);
            if (regiao != null) usuario.setRegiao(regiao);
            usuarioRepository.save(usuario);
            LOGGER.info("Updated existing user {}", email);
        }, () -> {
            Usuario user = new Usuario();
            user.setNomeCompleto(nomeCompleto);
            user.setEmail(email);
            user.setSenha(passwordEncoder.encode(finalPassword));
            user.setPerfil(perfil);
            if (regiao != null) user.setRegiao(regiao);
            usuarioRepository.save(user);
            LOGGER.info("Created user {}", email);
        });
    }
}
