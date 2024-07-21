package com.ananda.forohub.domain.perfil;

import com.ananda.forohub.domain.usuario.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public record DatosListadoPerfil(Long id, String nombre) {

    public DatosListadoPerfil(Perfil perfil){
        this(
            perfil.getId(),
            perfil.getNombre());
    }

}
