package com.lobobombeiros.ocorrencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OcorrenciasServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcorrenciasServiceApplication.class, args);
    }

}

