package com.ananda.forohub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record DatosRegistroTopico(
        @NotNull Long autor,
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull Curso curso) {


}
