package com.ananda.forohub.domain.respuesta;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarRespuesta(@NotNull Long id, String mensaje, String solucion) {

}
