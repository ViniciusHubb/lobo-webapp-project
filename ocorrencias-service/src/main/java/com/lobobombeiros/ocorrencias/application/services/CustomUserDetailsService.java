package com.lobobombeiros.ocorrencias.application.services;

import com.lobobombeiros.ocorrencias.application.client.UserClient;
import com.lobobombeiros.ocorrencias.application.client.dto.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userClient.findByEmail(username);
        if (userDTO == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username);
        }
        return new User(userDTO.getEmail(), userDTO.getSenha(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userDTO.getPerfil())));
    }
}
