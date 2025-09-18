package com.lobobombeiros.ocorrencias.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // Em um sistema real, você obteria o usuário logado do Spring Security Context
        // SecurityContextHolder.getContext().getAuthentication().getName();
        return () -> Optional.of("sistema"); // Placeholder
    }
}

