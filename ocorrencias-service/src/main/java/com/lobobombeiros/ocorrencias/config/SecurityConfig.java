package com.lobobombeiros.ocorrencias.config;

import com.lobobombeiros.ocorrencias.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String OCORRENCIAS_PATH = "/api/ocorrencias/**";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_CHEFE = "CHEFE";
    private static final String ROLE_ANALISTA = "ANALISTA";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**","/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/help", "/actuator/info").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, OCORRENCIAS_PATH).hasAnyRole(ROLE_ADMIN, ROLE_CHEFE, ROLE_ANALISTA)
                        .requestMatchers(HttpMethod.POST, "/api/ocorrencias").hasAnyRole(ROLE_ADMIN, ROLE_CHEFE)
                        .requestMatchers(HttpMethod.PUT, OCORRENCIAS_PATH).hasAnyRole(ROLE_ADMIN, ROLE_CHEFE)
                        .requestMatchers(HttpMethod.DELETE, OCORRENCIAS_PATH).hasRole(ROLE_ADMIN)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
