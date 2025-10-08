package com.lobobombeiros.ocorrencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
public class OcorrenciasServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcorrenciasServiceApplication.class, args);
    }

}
