package com.ananda.forohub.domain.usuario;

import java.util.Set;
import java.util.stream.Collectors;

import com.ananda.forohub.domain.perfil.Perfil;

public record DatosRespuestaUsuario(
        Long id,
        String nombre,
        String login,
        Set<String> perfiles
) {

    public DatosRespuestaUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getLogin(),
                usuario.getPerfiles().stream()
                        .map(Perfil::getNombre)
                        .collect(Collectors.toSet())
        );
    }
}

