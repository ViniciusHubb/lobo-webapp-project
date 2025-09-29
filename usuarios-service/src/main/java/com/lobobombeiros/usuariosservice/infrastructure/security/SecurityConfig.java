package com.lobobombeiros.usuariosservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String USUARIOS_PATH = "/usuarios/**";
    private static final String ROLE_ADMIN = "ADMIN";

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita o CSRF, comum em APIs stateless
                .csrf(AbstractHttpConfigurer::disable)
                // Configura as regras de autorização para os endpoints HTTP
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuarios/internal/**").permitAll()
                        // Exige a role 'ADMIN' para GET, PUT, DELETE em /usuarios/**
                        .requestMatchers(HttpMethod.GET, USUARIOS_PATH).hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, USUARIOS_PATH).hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, USUARIOS_PATH).hasRole(ROLE_ADMIN)

                        .requestMatchers(HttpMethod.POST, "/usuarios").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/usuarios/solicitar-redefinicao-senha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/redefinir-senha").permitAll()
                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                // Habilita a autenticação básica (usuário e senha)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o BCrypt como o algoritmo para criptografar senhas
        return new BCryptPasswordEncoder();
    }
}