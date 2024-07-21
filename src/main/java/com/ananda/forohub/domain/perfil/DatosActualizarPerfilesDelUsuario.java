package com.ananda.forohub.domain.perfil;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarPerfilesDelUsuario(@NotNull Long usuarioId, @NotNull Long perfilId) {
}
