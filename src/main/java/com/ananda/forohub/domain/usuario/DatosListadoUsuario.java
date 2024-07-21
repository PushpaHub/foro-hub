package com.ananda.forohub.domain.usuario;

import com.ananda.forohub.domain.perfil.Perfil;

import java.util.Set;
import java.util.stream.Collectors;

public record DatosListadoUsuario(Long id, String nombre, String login, Set<String> perfiles) {

    public DatosListadoUsuario(Usuario usuario){
        this(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getLogin(),
            usuario.getPerfiles().stream()
                    .map(Perfil::getNombre)
                    .collect(Collectors.toSet()));
    }

}
