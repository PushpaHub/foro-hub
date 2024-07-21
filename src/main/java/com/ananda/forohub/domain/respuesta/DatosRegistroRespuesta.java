package com.ananda.forohub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record DatosRegistroRespuesta(

        @NotBlank String mensaje,
        @NotNull Long topico,
        String solucion) {

}
