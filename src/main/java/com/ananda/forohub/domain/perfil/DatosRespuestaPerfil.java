package com.ananda.forohub.domain.perfil;

import com.ananda.forohub.domain.usuario.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public record DatosRespuestaPerfil(
        Long id,
        String nombre) {

    public DatosRespuestaPerfil(Perfil perfil) {
        this(
                perfil.getId(),
                perfil.getNombre());
    }
}

