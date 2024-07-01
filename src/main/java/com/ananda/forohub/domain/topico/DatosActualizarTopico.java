package com.ananda.forohub.domain.topico;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(@NotNull Long id, String titulo, String mensaje,
                                    String estatus, String curso) {

}
