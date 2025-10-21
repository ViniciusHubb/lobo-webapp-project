package com.lobobombeiros.apigateway.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> {}) // 👈 mantém o CORS ativo, usando o CorsConfigurationSource
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());
        return http.build();
    }
}