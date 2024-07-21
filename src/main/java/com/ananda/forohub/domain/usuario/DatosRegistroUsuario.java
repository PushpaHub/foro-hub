package com.ananda.forohub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(

        @NotBlank String nombre,
        @NotBlank @Email String login,
        @NotBlank String clave) {

}
