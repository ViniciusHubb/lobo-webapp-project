package com.lobobombeiros.usuariosservice.infrastructure.security;

import com.lobobombeiros.usuariosservice.application.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                // Desabilita o CSRF, comum em APIs stateless
                .csrf(csrf -> csrf.disable())
                .authenticationManager(authenticationManager)
                // Configura as regras de autorização para os endpoints HTTP
                .authorizeHttpRequests(auth -> auth
                        // Exige a role 'ADMIN' para GET, PUT, DELETE em /usuarios/**
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMIN")
                        // Permite requisições POST para /usuarios sem autenticação (para criar novos usuários)
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
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