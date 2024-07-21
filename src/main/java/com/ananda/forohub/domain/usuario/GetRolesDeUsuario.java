package com.ananda.forohub.domain.usuario;

import com.ananda.forohub.domain.perfil.Perfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetRolesDeUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Set<String> getRoles(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        Set<Perfil> perfiles = usuario.getPerfiles();

        return perfiles.stream()
                .map(perfil -> "ROLE_" + perfil.getNombre())
                .collect(Collectors.toSet());
    }

}
