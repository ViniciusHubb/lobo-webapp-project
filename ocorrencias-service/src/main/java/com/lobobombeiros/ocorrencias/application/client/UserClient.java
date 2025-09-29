package com.lobobombeiros.ocorrencias.application.client;

import com.lobobombeiros.ocorrencias.application.client.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios-service")
public interface UserClient {

    @GetMapping("/usuarios/internal/by-email/{email}")
    UserDTO findByEmail(@PathVariable("email") String email);
}

