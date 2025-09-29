package com.lobobombeiros.usuariosservice.application.mapper;

import com.lobobombeiros.usuariosservice.application.dto.InternalUserResponse;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioRequest;
import com.lobobombeiros.usuariosservice.application.dto.UsuarioResponse;
import com.lobobombeiros.usuariosservice.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioResponse resp = new UsuarioResponse();
        resp.setId(usuario.getId());
        resp.setNomeCompleto(usuario.getNomeCompleto());
        resp.setEmail(usuario.getEmail());
        resp.setPerfil(usuario.getPerfil());
        resp.setRegiao(usuario.getRegiao());
        return resp;
    }

    public InternalUserResponse toInternalResponse(Usuario usuario) {
        if (usuario == null) return null;
        InternalUserResponse resp = new InternalUserResponse();
        resp.setEmail(usuario.getEmail());
        resp.setSenha(usuario.getSenha());
        resp.setPerfil(usuario.getPerfil());
        return resp;
    }

    public Usuario toEntity(UsuarioRequest req) {
        if (req == null) return null;
        Usuario u = new Usuario();
        u.setNomeCompleto(req.getNomeCompleto());
        u.setEmail(req.getEmail());
        u.setPerfil(req.getPerfil());
        u.setRegiao(req.getRegiao());
        // senha intentionally not mapped here - handled by service
        return u;
    }
}

