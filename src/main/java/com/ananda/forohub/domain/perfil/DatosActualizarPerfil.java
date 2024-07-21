package com.ananda.forohub.domain.perfil;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarPerfil(@NotNull Long id, @NotNull String nombre) {
}
